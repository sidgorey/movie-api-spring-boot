package com.example.demo.controller;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.config.Properties;
import com.example.demo.models.Movie;
import com.example.demo.services.MovieClient;
import com.example.demo.services.MovieService;

import reactor.core.publisher.Flux;

import static java.util.Comparator.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;


@RestController
@RequestMapping("/api")
public class MovieController {

	private static final Logger logger = LoggerFactory.getLogger(MovieController.class);
	
	@Autowired
	private MovieService movieService;
	
	@Autowired
	private Properties props;
	
	@Autowired
	private MovieClient movieClient;
	
public boolean init(){
		
		logger.info("Enter init method.");
		
		Long allMovies = movieService.count();
		if(allMovies != 0)
		{
			logger.info("The database is already contains the movies. The API hit has been removed.");
			//The case where the tables already contain the movies in the database.
			return true;
		}
		//Hit the APIs and accumulate the data regarding the movies;
		String[] movies = props.getMoviesList();
		String wikiUrl = props.getWikiUrl();
		for(String movie: movies)
		{
			Movie movie_object = new Movie();
			movie_object.setTitle(movie);
			movieClient.getMovieDescriptionCats(movie_object);
			
			String completeUrl = wikiUrl + movie;
//			RestTemplate restTemplate = new RestTemplate();
//			JSONObject response = restTemplate.getForObject(completeUrl, JSONObject.class);
 
			Flux<JSONObject> responseFlux = WebClient.create()
					.get()
					.uri(completeUrl)
					.retrieve()
					.bodyToFlux(JSONObject.class);
			
			responseFlux.subscribe(response -> {
				if(response == null)
				{
					logger.info("The movie " + movie + " is not present in the WIki API.");
					return ;
					//DO nothign as the API did not contain any respose or get the response appropriately.
				}
				movie_object.parseJSON(response);
				if(movie_object.isIs_movie_in_API()) {
//					if(!atleast_one_movie_saved)
//					{
//						atleast_one_movie_saved = true;
//					}
						try {
						movieService.save(movie_object);
						
						}catch(Exception e)
						{
							e.printStackTrace();
						}							
				}
				else {
					logger.info("The movie " + movie + " Does not exist in the Wiki Database API.");
				}
			});
		}
		
		logger.info("Exit init method.");
		return true;
		
		
	}


	@GetMapping("/")
	public String getRoot()
	{
		return "Welcome to API.";
	}

	@RequestMapping(value = "/movies", method = RequestMethod.GET, produces= {"application/json", "application/xml;q=0.9"})
	public List<Movie> getAllMovies(@RequestParam(value="sortBy", required=false, defaultValue = "title") String sortBy, 
			@RequestParam(value="reverse", required = false, defaultValue = "false") boolean reverse
			) {
		logger.info("Enter getAllMovies");
		
		List<Movie> sorted = movieService.list();
		
		logger.info("The complete list of movies is" + sorted.toString() );
		
		
		switch(sortBy.toLowerCase())
		{
		case "title": sorted.sort(comparing(Movie::getTitle));
			break;
		case "release" :
			final String patternString = "(January|February|March|April|May|June|July|August|September|October|November|December)\\s+(\\d{1,2})\\s+(\\d{4})";
			sorted.sort((movie1, movie2) -> {
				Pattern pattern = Pattern.compile(patternString);
				Matcher matcher = pattern.matcher(movie1.getRelease_date().get(0));
				if(matcher.find())
				{
					String found = matcher.group(1);
					Matcher matcher2 = pattern.matcher(movie2.getRelease_date().get(0));
					if(matcher2.find())
					{
						String found2 = matcher2.group(1);
						SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");
						Date date1;
						Date date2;
						try {
							date1 = formatter.parse(found);
							date2 = formatter.parse(found2);
							return date1.compareTo(date2);
						} catch (ParseException e) {
							e.printStackTrace();
							return 0;
						}
					}
				}
				return 0;	
			});
			break;
		case "numcategories":
			
			Collections.sort(sorted, (movie1, movie2) -> Integer.compare( movie1.getCategories().size() , movie2.getCategories().size()));
			logger.info("numCategories " + sorted.get(0).toString());
			break;
		default : logger.info("Wrong sortBy param tried : " + sortBy);
		}
		if(reverse)
		{
			Collections.reverse(sorted);
		}
		
		logger.info("Exit getAllMovies");
		
		
		return sorted;
		
	}
	
	


	@GetMapping("/saveAll")
	public String saveToDb() {
		return (init())? "Saved" : "error encountered.";
	}
	
	@GetMapping("/movie/{title}")
	public Movie getMovie(@PathVariable("title") String title)
	{
		logger.info("Enter getMovie");
		
		List<Movie> movies = movieService.list();
		
		logger.info("All the movies stored in the database are..." + movies.toString());
		
		for(Movie mov : movies)
		{
			if(mov.getTitle().equals(title)) {
				return mov;
			}
		}
		logger.info("Exit getMovie");
		
		return new Movie();
	}
	@GetMapping("/movie")
	public List<Movie> getSimilarMovies(
			@RequestParam(value="similarTo", required=true) String similarTo,
			@RequestParam(value="matches", required=false,defaultValue="category") String matches,
			@RequestParam(value="limit", required=true) Integer limit)
	{
		List<Movie> movies = movieService.list();
		Movie req = null;
		for(Movie movie : movies)
		{
			if(movie.getTitle().equals(similarTo))
			{
				req = movie;
			}
		}
		
		if(req == null)
		{
			logger.info("The requested movie not found in the database.");
			return null;
		}
		for(Movie movie : movies)
		{
			movie.setCategoriesMatch(0);
			if(matches.equals("actor"))
			{
				
				for(String cat : req.getActors())
				{
					if(movie.getActors().contains(cat))
					{
						movie.setCategoriesMatch(movie.getCategoriesMatch() + 1);
					}
				}
			}
			else {
				for(String cat : req.getCategories())
				{
					if(movie.getCategories().contains(cat))
					{
						movie.setCategoriesMatch(movie.getCategoriesMatch() + 1);
					}
				}
			}
		}
		movies.sort(comparing(Movie::getCategoriesMatch));
		Collections.reverse(movies);
		
		limit = limit >= 1 ? limit : 1;
		
		return movies.subList(0, limit);
	}
}

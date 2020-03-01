package com.example.demo.services;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.config.Properties;
import com.example.demo.models.Movie;

@Service
public class MovieClient{

	private static Logger logger = LoggerFactory.getLogger(MovieClient.class);
	
	@Autowired
	private Properties props;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public String getMovieTitle() {
		return new String("Inception");
	}
	
	@Async
	public CompletableFuture< Movie > getMovieData(String movie){
		
		String completeUrl = props.getWikiUrl() + movie;
		
		JSONObject response = restTemplate.getForObject(completeUrl,JSONObject.class);
		
		Movie movie_obj = new Movie();
		movie_obj.setTitle(movie);
		
		try {
			movie_obj.parseJSON(response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("The movie " + movie + " does not have a valid JSON");
			return null;
		}
		
		return CompletableFuture.completedFuture(movie_obj);
		
	}
	
//	@Async
	public void getMovieDescriptionCats(Movie movie)
	{
		try {
			Document doc =  Jsoup.connect(props.getDescriptionUrl() + movie.getTitle()).timeout(3000).get();
			movie.parseDescription(doc);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public Properties getProps() {
		return props;
	}
}

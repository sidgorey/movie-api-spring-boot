package com.example.demo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import jdk.jfr.Description;

import org.slf4j.Logger;



@Entity
@JacksonXmlRootElement(localName="Movie")
public class Movie implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(Movie.class);
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(name="TITLE")
	@Valid
	@NotNull
	private String title;
	@Column(name="ACTOR", table="MOVIE_ACTORS")
	@ElementCollection(targetClass = String.class)
	@Valid
	@NotEmpty
	private List<String> actors;
	@Column(name="DIRECTOR", table="MOVIE_DIRECTORS")
	@ElementCollection(targetClass = String.class)
	private List<String> directors;
	@Column(name="PRODUCER", table="MOVIE_PRODUCERS")
	@ElementCollection(targetClass = String.class)
	private List<String> producers;
	@Column(name="COMPOSER", table="MOVIE_COMPOSERS")
	@ElementCollection(targetClass = String.class)
	private List<String> composers;
	@Column(name="PROD_HOUSE", table="MOVIE_PROD_HOUSES")
	@ElementCollection(targetClass = String.class)
	private List<String> prod_houses;
	@Column(name="RELEASE_DATE", table="MOVIE_RELEASE_DATES")
	@ElementCollection(targetClass = String.class)
	private List<String> release_dates;
	@Column
	private String duration;
	@Column(name="MOVIE_LANGUAGE")
	private String language;
	@Column
	private String budget;
	@Column
	private String box_collection;
	@Column 
	private String description;
	@Column(name="CATEGORY", table="MOVIE_CATEGORIES")
	@ElementCollection(targetClass = String.class)
	private List<String> categories;
	
	
	@Transient
	boolean is_movie_in_API = true;
	@Transient
	int categoriesMatch = 0;

	public Movie(Long id, String title, List<String> actors, List<String> director, List<String> producer,
			List<String> composer, List<String> prod_house, List<String> release_date, String duration, String language,
			String budget, String box_collection) {
		this();
		this.id = id;
		this.title = title;
		this.actors = actors;
		this.directors = director;
		this.producers = producer;
		this.composers = composer;
		this.prod_houses = prod_house;
		this.release_dates = release_date;
		this.duration = duration;
		this.language = language;
		this.budget = budget;
		this.box_collection = box_collection;
	}

	public Movie() {
		
		this.actors = new ArrayList<String>();
		this.directors = new ArrayList<String>();
		this.producers = new ArrayList<String>();
		this.composers = new ArrayList<String>();
		this.prod_houses = new ArrayList<String>();
		this.release_dates = new ArrayList<String>();
		
	}

	public Movie(Long id, String title) {
		this();
		this.id = id;
		this.title = title;
	}

	public List<String> getActors() {
		return actors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getDirector() {
		return directors;
	}

	public void setDirector(List<String> director) {
		this.directors = director;
	}

	public List<String> getProducer() {
		return producers;
	}

	public void setProducer(List<String> producer) {
		this.producers = producer;
	}

	public List<String> getComposer() {
		return composers;
	}

	public void setComposer(List<String> composer) {
		this.composers = composer;
	}

	public List<String> getProd_house() {
		return prod_houses;
	}

	public void setProd_house(List<String> prod_house) {
		this.prod_houses = prod_house;
	}

	public List<String> getRelease_date() {
		return release_dates;
	}

	public void setRelease_date(List<String> release_date) {
		this.release_dates = release_date;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getBox_collection() {
		return box_collection;
	}

	public void setBox_collection(String box_collection) {
		this.box_collection = box_collection;
	}

	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	public List<String> getCategories() {
		return categories;
	}
	
	public String getDescription() {
		return description;
	}
	
	
	public void setDescription(String description) {
		this.description = description;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
	
	@JsonIgnore
	public boolean isIs_movie_in_API() {
		return is_movie_in_API;
	}
	@JsonIgnore
	public int getCategoriesMatch() {
		return categoriesMatch;
	}
	public void setCategoriesMatch(int categoriesMatch) {
		this.categoriesMatch = categoriesMatch;
	}
	public void processTableRow(Element tableRow) {
		int num = tableRow.getElementsByTag("th").size();
		if(num == 0)
			return;
		
		Element th = tableRow.getElementsByTag("th").get(0);
		
		num = tableRow.select("td").size();
		
		if(num == 0)
			return;
		
		Element td = tableRow.select("td").get(0);
	
		String td_text = td.text().replace("\"", "'");
		String[] td_text_all_array = td_text.split("<{1}[a-zA-Z=\"'-/\\_ ]*>{1}");
		ArrayList<String> td_text_all = new ArrayList<String>();
		for(int i =0 ; i < td_text_all_array.length; i ++)
		{
			String text1 = td_text_all_array[i];
			if(text1 == null || text1.isBlank())
				continue;
			td_text_all.add(text1);
		}
		
		if (th.text().equals("Directed by")) {
		
			this.directors.addAll(td_text_all);
//			for(String data : td_text_all)
//			{
//				this.director.add(data);
//			}
			
		} else if (th.text().equals("Produced by")) {

			for(String data : td_text_all)
			{
				this.producers.add(data);
			}
			
		} 
		else if(th.text().equals("Starring")) {
		
			Elements lis = td.select("li");
			for(Element li : lis)
			{
				String[] texts = li.text().split("<{1}[a-zA-Z=\\\"'-/\\\\_ ]*>{1}");
				for(String text1 : texts)
				{
					if(!text1.isEmpty())
						this.actors.add(text1);
				}
			}
		}else if(th.text().equals("Music by")) {
			
			for(String tdt : td_text_all) {
				this.composers.add(tdt);
			}
		}
		else if(th.text().contains("Production")) {
			for(String tdt : td_text_all)
			{
				this.prod_houses.add(tdt);
			}
		}
		else if(th.text().contains("Release date")) {
		
			for(String tdt : td_text_all)
			{
				this.release_dates.add(tdt);
			}
				 
		}else if(th.text().contains("Running time")) {
			this.duration = td.text();
		}
		else if(th.text().equals("Language")) {
			this.language = td.text();
		}else if(th.text().equals("Budget")) {
			this.budget = td.text();
		}else if(th.text().equals("Box office")) {
			this.box_collection = td.text();
		}
	}

	
	
	public void parseJSON(JSONObject json) {

		logger.info("Enter parseJSON method");
		
		try {
		
		Map error = (Map) json.get("error");
		if(error != null)
		{
			this.is_movie_in_API = false;
			return ;
		}
		Map parse = (Map) json.get("parse");
		Map text =  (Map) parse.get("text");
		String star = (String) text.get("*");

		Document doc = Jsoup.parse(star);
		
		int tables_num = doc.getElementsByTag("table").size();
		if(tables_num == 0)
		{
			logger.info("No information table available for the movie.");
			this.is_movie_in_API = false;
			
			return;
		}
		Element table = doc.getElementsByTag("table").get(0);
			
		Elements table_rows = doc.getElementsByTag("tr");
		
		for (Element tableRow : table_rows) {
				processTableRow(tableRow);
			}	
		}catch(Exception e)
		{
			e.printStackTrace();
			this.is_movie_in_API = false;
		}
		
	}
	
	
	public void parseDescription(Document doc)
	{
		try {
			Elements p= doc.select("html table.infobox + p");
			if(p == null)
			{
				throw new Exception("table Information table not found.");
			}
			
			String[] words = p.get(0).text().split("<{1}[a-zA-Z=\"'-/\\_ ]*>{1}");
			logger.info(words[0] + "are the words split by regex.");
			String desc = String.join(" ", words);
			logger.info("Descroipt ion captured " + desc);
			
			this.setDescription(desc);
			
			// FOR CATEGORIES:
			Elements as = doc.select("div.catlinks ul li a");
//			Elements as = div.select("ul > li > a");
			
			ArrayList<String> cats  = new ArrayList<String>();
			for(Element ele : as)
			{
				String cat = ele.text();
				cats.add(cat);
			}
			this.setCategories(cats);
			
			logger.info(String.join(",",  cats) + "are the categories joined by ,");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("Description and or categs could not be processed.");
			
		}
				
	}
	public String toString() {
		return title + " starring " + actors.toString();
	}

}

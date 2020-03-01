package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class Properties {

   @Value("${props.movies_latest}")
   String[] movies;
   @Value("${props.movies}")
   String[] movies2;
	@Value("${props.wikiUrl}")
	String wikiUrl;
	@Value("${props.descriptionUrl}")
	String descriptionUrl;
	
	
	public void setMovies(String[] movies) {
		this.movies = movies;
	}
	
	public void setWikiUrl(String wikiUrl) {
		this.wikiUrl = wikiUrl;
	}
	
	public String getWikiUrl() {
		return wikiUrl;
	}

	public String[] getMoviesList() {
		return this.movies;
	}
	public String getDescriptionUrl() {
		return descriptionUrl;
	}
	
}

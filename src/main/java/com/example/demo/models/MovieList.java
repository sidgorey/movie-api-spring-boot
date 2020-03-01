package com.example.demo.models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MovieList {

	List<Movie> moviesList;

	public MovieList(){
		
	}
	
	public List<Movie> getMoviesList() {
		return moviesList;
	}
	public void setMoviesList(List<Movie> list){
		this.moviesList = list;
	}
}

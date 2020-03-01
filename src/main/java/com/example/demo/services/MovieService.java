package com.example.demo.services;

import java.util.List;
import com.example.demo.models.Movie;

public interface MovieService {

	public void save(Movie movie) ;
	
	public List<Movie> list();
	public Long count() ;
}

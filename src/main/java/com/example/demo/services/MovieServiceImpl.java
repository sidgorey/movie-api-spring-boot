package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Movie;
import com.example.demo.repositories.MovieRepository;

@Service
public class MovieServiceImpl implements MovieService{

		@Autowired
		private MovieRepository movieRepository;
		
		public Long count() {
			return movieRepository.count();
		}

		@Override
		public void save(Movie movie) {
			movieRepository.save(movie);
		}

		@Override
		public List<Movie> list() {
			return movieRepository.findAll();
		}
	

}

package com.example.demo;


import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import com.example.demo.config.Properties;
import com.example.demo.models.Movie;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.services.MovieService;

@SpringBootApplication
public class MoviesApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MoviesApplication.class, args);
	}

}

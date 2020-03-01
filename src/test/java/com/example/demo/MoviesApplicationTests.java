package com.example.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

import com.example.demo.models.Movie;
import com.example.demo.repositories.MovieRepository;
import com.example.demo.services.MovieClient;
import com.example.demo.services.MovieService;
import com.example.demo.services.MovieServiceImpl;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
class MoviesApplicationTests {
	
	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Mock
	private MovieRepository movieRepository;
	
	@InjectMocks
	private MovieService movieService = new MovieServiceImpl();
	
	@BeforeEach
	void setMockUp()
	{
		Mockito.when(movieRepository.count()).thenReturn((long)10);
		
	}
	
	@DisplayName("Test Mock movieService + movieRepository")
    @Test
    void testGet() {
        assertEquals(Long.valueOf(10), movieService.count());
        
    }
	
	@DisplayName("Testing API - welcome to API.")
	@Test
	void testAPI1() throws RestClientException, MalformedURLException {
		ResponseEntity<String> response = restTemplate.getForEntity(
				new URL("http://localhost:" + port + "/api/").toString(), String.class);
		assertEquals("Welcome to API.", response.getBody());
		
	}
	
}

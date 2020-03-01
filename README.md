# movie-api-spring-boot
A Spring Boot API which consumes the Wikipedia API and allows us to consume a new API to sort the data or in consume it in different formats which are xml or json. 
Features:

Launchung URL -> http://localhost:8080/
1. /api/saveAll -> Download all the movie data from hitting two Wiki APIs. This is recommended before performing any other API hit.
2. /api/movies[.json/.xml]?sortBy=''&reverse='' -> sort by can have values of 'title', 'release' or 'numcategories' where :
                                    + title denotes sort the list by title names or the movie title names.
                                    + release denotes sort the list according to the release date in ascending order.
                                    + numcategories denotes sort the list according to the number of categories of each movie. Category example is '1970s movie'.
                                    + reverse can be either true or false which is not required. It reverses the order of the list.                                    
                                    + Adding .json displays in json format and .xml for xml.
3. /api/movie[.json/.xml]?similarTo=''&matches=''&limit=''   -> Display a list of all movies which match the movie as mentioned in similarTo and matches according to either of 'actor' or 'category' which means that it would either matche according to the actor in the movie or according to the category of the movie.limit indicates how many maximum records are required in one API hit.
4. /api/movie/{title} -> Display a movie details according to the title.

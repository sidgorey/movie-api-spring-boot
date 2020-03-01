

drop table if exists movie_actors;
drop table if exists movie_producers;
drop table if exists movie_directors;
drop table if exists movie_release_dates;
drop table if exists movie_composers;
drop table if exists movie_prod_houses;

drop table if exists movie_producer;
drop table if exists movie_director;
drop table if exists movie_release_date;
drop table if exists movie_composer;
drop table if exists movie_prod_house;

drop table if exists release_date;
drop table if exists prod_house;

drop table if exists release_dates;
drop table if exists prod_houses;

drop table if exists movie_categories;
drop table if exists movie;


create table movie(
  ID INTEGER NOT NULL AUTO_INCREMENT,
  TITLE VARCHAR(500),
  DURATION VARCHAR(500),
  MOVIE_LANGUAGE VARCHAR(500),
  BUDGET VARCHAR(500),
  BOX_COLLECTION VARCHAR(500),
  DESCRIPTION VARCHAR(2000),
  PRIMARY KEY (ID)
);
CREATE TABLE MOVIE_ACTORS(
  MOVIE_ID INTEGER NOT NULL,
  ACTOR VARCHAR(100),
  FOREIGN KEY(MOVIE_ID) REFERENCES MOVIE(ID)
);
CREATE TABLE MOVIE_DIRECTORS(
  MOVIE_ID INTEGER NOT NULL,
  DIRECTOR VARCHAR(100),
  FOREIGN KEY(MOVIE_ID) REFERENCES MOVIE(ID)
);
CREATE TABLE MOVIE_PRODUCERS(
  MOVIE_ID INTEGER NOT NULL,
  PRODUCER VARCHAR(100),
  FOREIGN KEY(MOVIE_ID) REFERENCES MOVIE(ID)
);
CREATE TABLE MOVIE_COMPOSERS(
  MOVIE_ID INTEGER NOT NULL,
  COMPOSER VARCHAR(100),
  FOREIGN KEY(MOVIE_ID) REFERENCES MOVIE(ID)
);
CREATE TABLE MOVIE_PROD_HOUSES(
  MOVIE_ID INTEGER NOT NULL,
  PROD_HOUSE VARCHAR(100),
  FOREIGN KEY(MOVIE_ID) REFERENCES MOVIE(ID)
);
CREATE TABLE MOVIE_RELEASE_DATES(
  MOVIE_ID INTEGER NOT NULL,
  RELEASE_DATE VARCHAR(100),
  FOREIGN KEY(MOVIE_ID) REFERENCES MOVIE(ID)
);
CREATE TABLE MOVIE_CATEGORIES(
  MOVIE_ID INTEGER NOT NULL,
  CATEGORY VARCHAR(100),
  FOREIGN KEY(MOVIE_ID) REFERENCES MOVIE(ID)
)

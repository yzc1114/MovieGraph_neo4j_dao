package com.yzchnb.neo4jdao.NodeEntity;
import org.neo4j.ogm.annotation.*;

import java.util.List;

@NodeEntity(label = "Movie")
public class Movie {
    @Id
    @GeneratedValue
    Long id;

    public Movie(String title, String releaseDate, Short releaseYear, Short releaseMonth, Short releaseDay, Short releaseWeekDay, Float ranking) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.releaseYear = releaseYear;
        this.releaseMonth = releaseMonth;
        this.releaseDay = releaseDay;
        this.releaseWeekDay = releaseWeekDay;
        this.ranking = ranking;
    }

    @Property(name = "title")
    @Index
    private String title;

    @Property(name = "releaseDate")
    private String releaseDate;

    @Property(name = "releaseYear")
    private Short releaseYear;

    @Property(name = "releaseMonth")
    private Short releaseMonth;

    @Property(name = "releaseDay")
    private Short releaseDay;

    @Property(name = "releaseWeekDay")
    private Short releaseWeekDay;

    @Property(name = "ranking")
    private Float ranking;

    @Relationship(type = "StarringInMovie")
    private List<Actor> starrings;

    @Relationship(type = "SupportingInMovie")
    private List<Actor> supportingActors;

    @Relationship(type = "HasDirector")
    private List<Director> directors;

    @Relationship(type = "HasGenre")
    private List<Genre> genres;

    @Relationship(type = "OwnsProduct")
    private List<Product> products;

    public List<Actor> getStarrings() {
        return starrings;
    }

    public void setStarrings(List<Actor> starrings) {
        this.starrings = starrings;
    }

    public List<Actor> getSupportingActors() {
        return supportingActors;
    }

    public void setSupportingActors(List<Actor> supportingActors) {
        this.supportingActors = supportingActors;
    }

    public List<Director> getDirectors() {
        return directors;
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Short releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Short getReleaseMonth() {
        return releaseMonth;
    }

    public void setReleaseMonth(Short releaseMonth) {
        this.releaseMonth = releaseMonth;
    }

    public Short getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(Short releaseDay) {
        this.releaseDay = releaseDay;
    }

    public Short getReleaseWeekDay() {
        return releaseWeekDay;
    }

    public void setReleaseWeekDay(Short releaseWeekDay) {
        this.releaseWeekDay = releaseWeekDay;
    }

    public Float getRanking() {
        return ranking;
    }

    public void setRanking(Float ranking) {
        this.ranking = ranking;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseYear=" + releaseYear +
                ", releaseMonth=" + releaseMonth +
                ", releaseDay=" + releaseDay +
                ", releaseWeekDay=" + releaseWeekDay +
                ", ranking=" + ranking +
                ", starrings=" + starrings +
                ", supportingActors=" + supportingActors +
                ", directors=" + directors +
                ", genres=" + genres +
                ", products=" + products +
                '}';
    }
}

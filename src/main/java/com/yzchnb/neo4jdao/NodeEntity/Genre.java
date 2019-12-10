package com.yzchnb.neo4jdao.NodeEntity;

import org.neo4j.ogm.annotation.*;

@NodeEntity(label = "Genre")
public class Genre {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "type")
    private String type;

//    @RelationshipEntity(type = "MovieGenreRelation")
////    private List<MovieGenreRelation> movieGenreRelations;
//    private List<MovieGenreRelation> movieGenreRelations;
//
//    public List<MovieGenreRelation> getMovieGenreRelations() {
//        return movieGenreRelations;
//    }
//
//    public void setMovieGenreRelations(List<MovieGenreRelation> movieGenreRelations) {
//        this.movieGenreRelations = movieGenreRelations;
//    }

    public Genre(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}

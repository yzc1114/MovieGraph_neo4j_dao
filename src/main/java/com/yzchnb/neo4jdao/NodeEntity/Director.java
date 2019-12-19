package com.yzchnb.neo4jdao.NodeEntity;

import org.neo4j.ogm.annotation.*;

import java.util.Objects;

@NodeEntity(label = "Director")
public class Director {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "name")
    private String name;

    @Override
    public String toString() {
        return "Director{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    //    @RelationshipEntity(type = "DirectorActorRelation", direction = RelationshipEntity.UNDIRECTED)
//    private List<DirectorActorRelation> directorActorRelations = new ArrayList<>();
//
//    @RelationshipEntity(type = "DirectorMovieRelation")
//    //private List<DirectorMovieRelation> directorMovieRelations;
//    private List<DirectorMovieRelation> directorMovieRelations = new ArrayList<>();
//
//    public List<DirectorMovieRelation> getDirectorMovieRelations() {
//        return directorMovieRelations;
//    }
//
//    public void setDirectorMovieRelations(List<DirectorMovieRelation> directorMovieRelations) {
//        this.directorMovieRelations = directorMovieRelations;
//    }
//
//    public List<DirectorActorRelation> getDirectorActorRelations() {
//        return directorActorRelations;
//    }
//
//    public void setDirectorActorRelations(List<DirectorActorRelation> directorActorRelations) {
//        this.directorActorRelations = directorActorRelations;
//    }

    public Director(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Director director = (Director) o;
        return Objects.equals(name, director.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

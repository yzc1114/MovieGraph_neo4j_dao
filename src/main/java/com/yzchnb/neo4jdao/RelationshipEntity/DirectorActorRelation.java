package com.yzchnb.neo4jdao.RelationshipEntity;

import com.yzchnb.neo4jdao.NodeEntity.Actor;
import com.yzchnb.neo4jdao.NodeEntity.Director;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "DirectorActorRelation")
public class DirectorActorRelation {
    @Id
    @GeneratedValue
    Long id;

    @StartNode
    private Actor actor;

    @EndNode
    private Director director;

    @Property
    private Integer count;

    public DirectorActorRelation(Actor actor, Director director, Integer count) {
        this.actor = actor;
        this.director = director;
        this.count = count;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }
}

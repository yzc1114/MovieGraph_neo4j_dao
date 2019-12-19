package com.yzchnb.neo4jdao.RelationshipEntity;

import com.yzchnb.neo4jdao.NodeEntity.Actor;
import org.neo4j.ogm.annotation.*;

import java.util.Objects;

@RelationshipEntity(type = "ActorCollaborationRelation")
public class ActorCollaborationRelation {
    @Id
    @GeneratedValue
    Long id;

    @StartNode
    private Actor actor1;

    @EndNode
    private Actor actor2;

    @Property
    private Integer count;

    public ActorCollaborationRelation(Actor actor1, Actor actor2, Integer count) {
        this.actor1 = actor1;
        this.actor2 = actor2;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Actor getActor1() {
        return actor1;
    }

    public void setActor1(Actor actor1) {
        this.actor1 = actor1;
    }

    public Actor getActor2() {
        return actor2;
    }

    public void setActor2(Actor actor2) {
        this.actor2 = actor2;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActorCollaborationRelation that = (ActorCollaborationRelation) o;
        return (actor1.equals(that.actor1) &&
                actor2.equals(that.actor2)) || (actor1.equals(that.actor2) && actor2.equals(that.actor1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(actor1, actor2);
    }
}

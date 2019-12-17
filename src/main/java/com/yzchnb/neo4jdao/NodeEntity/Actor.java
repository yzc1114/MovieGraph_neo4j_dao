package com.yzchnb.neo4jdao.NodeEntity;

import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import com.yzchnb.neo4jdao.RelationshipEntity.DirectorActorRelation;
import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NodeEntity(label = "Actor")
public class Actor {
    @Id
    @GeneratedValue
    Long id;

    @Property("name")
    private String name;

    @Relationship(type = "ActorCollaborationRelation", direction = Relationship.UNDIRECTED)
    private Set<ActorCollaborationRelation> actorCollaborationRelations = new HashSet<>();

    @Relationship(type = "DirectorActorRelation", direction = Relationship.UNDIRECTED)
    private Set<DirectorActorRelation> directorActorRelations = new HashSet<>();

    public Set<ActorCollaborationRelation> getActorCollaborationRelations() {
        return actorCollaborationRelations;
    }

    public void setActorCollaborationRelations(Set<ActorCollaborationRelation> actorCollaborationRelations) {
        this.actorCollaborationRelations = actorCollaborationRelations;
    }

    public Set<DirectorActorRelation> getDirectorActorRelations() {
        return directorActorRelations;
    }

    public void setDirectorActorRelations(Set<DirectorActorRelation> directorActorRelations) {
        this.directorActorRelations = directorActorRelations;
    }

    public Actor(String name) {
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
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", actorCollaborationRelations=" + actorCollaborationRelations +
                ", directorActorRelations=" + directorActorRelations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return name.equals(actor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

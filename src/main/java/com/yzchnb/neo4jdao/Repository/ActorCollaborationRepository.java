package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ActorCollaborationRepository extends Neo4jRepository<ActorCollaborationRelation, Long> {

    @Query("MATCH (:Actor)-[r:ActorCollaborationRelation]->(:Actor) return r order by r.count desc skip {startFrom} limit {limitation}")
    List<ActorCollaborationRelation> getActorCollaborationRelations(@Param("startFrom")Integer startFrom, @Param("limitation")Integer limitation);

    @Query("MATCH (a1:Actor)-[r:ActorCollaborationRelation]-(a2:Actor) where a1.name = {actorName1} and a2.name = {actorName2} return r.count")
    Integer getActorsCollaborationCount(@Param("actorName1")String actorName1, @Param("actorName2")String actorName2);

    @Query("match (:Actor)-[r:ActorCollaborationRelation]-(:Actor) with r, rand() as rand order by rand return r limit {limit}")
    Set<ActorCollaborationRelation> getRandomCollaboration(@Param("limit")Integer limit);

    @Query("match (:Actor)-[r:ActorCollaborationRelation]-(:Actor) return count(r)")
    Integer getCountOfRelation();

    @Query("MATCH (a1:Actor)-[r:ActorCollaborationRelation]-(a2:Actor) where a1.name = {actorName1} and a2.name = {actorName2} return r limit 1")
    ActorCollaborationRelation getActorCollaborationRelationByActorNames(@Param("actorName1")String actorName1, @Param("actorName2") String actorName2);

    @Query("MATCH (:Actor)-[r:ActorCollaborationRelation]-(:Actor) return r skip {s} limit {l}")
    HashSet<ActorCollaborationRelation> getBatch(@Param("s")Integer s, @Param("l")Integer l);
}

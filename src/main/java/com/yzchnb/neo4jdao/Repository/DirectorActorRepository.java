package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import com.yzchnb.neo4jdao.RelationshipEntity.DirectorActorRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface DirectorActorRepository extends Neo4jRepository<DirectorActorRelation, Long> {
    @Query("MATCH (:Actor)-[r:DirectorActorRelation]-(:Director) return r skip {startFrom} limit {limitation}")
    List<DirectorActorRelation> getDirectorActorRelations(@Param("startFrom")Integer startFrom, @Param("limitation")Integer limitation);

    @Query("MATCH (a:Actor)-[r:DirectorActorRelation]-(d:Director) where a.name = {actorName} and d.name = {directorName} return r.count")
    Integer getDirectorActorCollaborationCount(@Param("actorName")String actorName, @Param("directorName")String directorName);

    @Query("match (:Actor)-[r:DirectorActorRelation]-(:Director) with r, rand() as rand order by rand return r limit {limit}")
    Set<DirectorActorRelation> getRandomCollaboration(@Param("limit")Integer limit);

    @Query("match (:Actor)-[r:DirectorActorRelation]-(:Director) return count(r)")
    Integer getCountOfRelation();

    @Query("match (a:Actor)-[r:DirectorActorRelation]-(d:Director) where a.name = {actorName} and d.name = {directorName} return r limit 1")
    DirectorActorRelation getDirectorActorRelation(@Param("actorName")String actorName, @Param("directorName") String directorName);
}

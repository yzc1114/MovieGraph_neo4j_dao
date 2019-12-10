package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.RelationshipEntity.DirectorActorRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DirectorActorRepository extends Neo4jRepository<DirectorActorRelation, Long> {
    @Query("MATCH (:Actor)-[r:DirectorActorRelation]-(:Director) return r skip {startFrom} limit {limitation}")
    List<DirectorActorRelation> getDirectorActorRelations(@Param("startFrom")Integer startFrom, @Param("limitation")Integer limitation);
}

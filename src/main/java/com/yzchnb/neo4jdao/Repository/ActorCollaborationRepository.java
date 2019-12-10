package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActorCollaborationRepository extends Neo4jRepository<ActorCollaborationRelation, Long> {

    @Query("MATCH (:Actor)-[r:ActorCollaborationRelation]-(:Actor) return r skip {startFrom} limit {limitation}")
    List<ActorCollaborationRelation> getActorCollaborationRelations(@Param("startFrom")Integer startFrom, @Param("limitation")Integer limitation);
}

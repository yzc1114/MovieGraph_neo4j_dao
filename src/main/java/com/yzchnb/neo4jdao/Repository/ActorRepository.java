package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Actor;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ActorRepository extends Neo4jRepository<Actor, Long> {
    Actor findActorByName(String name);
    Boolean existsActorByName(String name);
}

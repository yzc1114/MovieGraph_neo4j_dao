package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Actor;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;

public interface ActorRepository extends Neo4jRepository<Actor, Long> {
    Actor findActorByName(String name);
    Boolean existsActorByName(String name);

    @Query("match (a:Actor) with a, rand() as r order by r return a limit {limit}")
    HashSet<Actor> findRandomActors(@Param("limit")Integer limit);
}

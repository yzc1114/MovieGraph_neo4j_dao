package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Director;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Set;

public interface DirectorRepository extends Neo4jRepository<Director, Long> {
    Director findDirectorByName(String name);

    @Query("match (a:Director) with a, rand() as r order by r return a limit {limit};")
    Set<Director> findRandomDirectors(@Param("limit")Integer limit);
}

package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Director;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface DirectorRepository extends Neo4jRepository<Director, Long> {
    Director findDirectorByName(String name);
}

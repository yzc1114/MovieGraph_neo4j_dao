package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Genre;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface GenreRepository extends Neo4jRepository<Genre, Long> {
    Genre findGenreByType(String type);
}

package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Genre;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface GenreRepository extends Neo4jRepository<Genre, Long> {
    Genre findGenreByType(String type);

    @Query("Match (g:Genre) where g.type = {type} return count(g)")
    Integer getGenreCount(@Param("type")String type);

    @Query("Match (g:Genre) return g")
    Set<Genre> getAllGenres();
}

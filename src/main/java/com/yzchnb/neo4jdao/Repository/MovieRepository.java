package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Movie;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {
    @Query("MATCH (m: Movie) WHERE m.releaseYear = {year} and m.releaseMonth = {month} and m.releaseDay = day return m")
    HashSet<Movie> findMoviesByYearMonthDay(@Param("year")Integer year, @Param("month")Integer month, @Param("day")Integer day);

    @Query("MATCH (m:Movie) WHERE m.releaseYear = {year} and m.releaseMonth = {month} return m")
    HashSet<Movie> findMoviesByYearMonth(@Param("year")Integer year, @Param("month")Integer month);

    @Query("MATCH (m:Movie) WHERE m.releaseYear = {year} return m")
    HashSet<Movie> findMoviesByYear(@Param("year")Integer year);

    @Query("MATCH (m:Movie) WHERE m.releaseWeekDay = {weekDay} return m")
    HashSet<Movie> findMoviesByWeekDay(@Param("weekDay")Integer weekDay);

    HashSet<Movie> findMoviesByTitle(@Param("title") String title);

    @Query("MATCH (m:Movie)-[:HasGenre]-(g:Genre) where g.type = {genre} return m skip {startFrom} limit {limitation}")
    HashSet<Movie> findMoviesByGenre(@Param("genre") String genre, @Param("startFrom")Integer startFrom, @Param("limitation")Integer limitation);

    @Query("MATCH (m:Movie)-[:HasDirector]-(d:Director) where d.name = {director} return m")
    HashSet<Movie> findMoviesByDirector(@Param("director") String director);

    @Query("MATCH (m:Movie)-[:StarringInMovie]-(a:Actor) where a.name = {starring} return m")
    HashSet<Movie> findMoviesByStarring(@Param("starring")String starring);

    @Query("MATCH (m:Movie)-[:SupportingInMovie]-(a:Actor) where a.name = {supportingActor} return m")
    HashSet<Movie> findMoviesBySupportingActor(@Param("supportingActor")String supportingActor);

    @Query("MATCH (m:Movie) where m.ranking > {ranking} return m order by m.ranking desc skip {startFrom} limit {limitation}")
    HashSet<Movie> findMoviesByRankingGreaterThan(@Param("ranking")Float ranking, @Param("startFrom")Integer startFrom, @Param("limitation") Integer limitation);

    @Query("MATCH (m:Movie) where m.ranking >= {ranking} return m order by m.ranking desc skip {startFrom} limit {limitation}")
    HashSet<Movie> findMoviesByRankingGreaterThanEqual(@Param("ranking")Float ranking, @Param("startFrom")Integer startFrom, @Param("limitation") Integer limitation);

    @Query("MATCH (m:Movie) where m.ranking < {ranking} return m order by m.ranking desc skip {startFrom} limit {limitation}")
    HashSet<Movie> findMoviesByRankingLessThan(@Param("ranking")Float ranking, @Param("startFrom")Integer startFrom, @Param("limitation") Integer limitation);

    @Query("MATCH (m:Movie) where m.ranking <= {ranking} return m order by m.ranking desc skip {startFrom} limit {limitation}")
    HashSet<Movie> findMoviesByRankingLessThanEqual(@Param("ranking")Float ranking, @Param("startFrom")Integer startFrom, @Param("limitation") Integer limitation);
}
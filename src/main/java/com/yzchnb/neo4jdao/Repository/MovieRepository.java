package com.yzchnb.neo4jdao.Repository;

import com.alibaba.fastjson.JSONObject;
import com.yzchnb.neo4jdao.NodeEntity.*;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {

    @Query("MATCH (m:Movie) return count(m)")
    Integer getMoviesCount();

    @Query("MATCH (m:Movie) return distinct m.releaseYear")
    List<Integer> getYears();

    @Query("MATCH (m:Movie) where m.releaseWeekDay = {releaseWeekDay} return count(m)")
    Integer getMoviesCountOfWeekDay(@Param("releaseWeekDay")Integer releaseWeekDay);

    @Query("MATCH (m:Movie) with m, rand() as r order by r return m.title limit {limit}")
    List<String> getRandomTitles(@Param("limit")Integer limit);

    @Query("MATCH (m:Movie) with m, rand() as r order by r return m limit {limit}")
    List<Movie> getRandomMovies(@Param("limit")Integer limit);

    @Query("MATCH (m: Movie) WHERE m.releaseYear = {year} and m.releaseMonth = {month} and m.releaseDay = {day} return m")
    HashSet<Movie> findMoviesByYearMonthDay(@Param("year")Integer year, @Param("month")Integer month, @Param("day")Integer day);

    @Query("MATCH (m:Movie) WHERE m.releaseYear = {year} and m.releaseMonth = {month} return m")
    HashSet<Movie> findMoviesByYearMonth(@Param("year")Integer year, @Param("month")Integer month);

    @Query("MATCH (m:Movie) WHERE m.releaseYear = {year} return m")
    HashSet<Movie> findMoviesByYear(@Param("year")Integer year);

    @Query("MATCH (m:Movie) WHERE m.releaseWeekDay = {weekDay} return m skip {startFrom} limit {limitation}")
    HashSet<Movie> findMoviesByWeekDay(@Param("weekDay")Integer weekDay, @Param("startFrom")Integer startFrom, @Param("limitation")Integer limitation);

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

    @Query("MATCH (u:User)-[:UserReviewRelation]-(r:Review)-[:ReviewProductRelation]-(p:Product)-[]-(m:Movie) where u.userId = {userId} and r.mood = {mood} return m as movie, r as review order by review.mood")
    HashSet<JSONObject> findMovieAndReviewsByUserIdReviewMood(@Param("mood")String mood, @Param("userId")String userId);

    @Query("MATCH (u:User)-[:UserReviewRelation]-(r:Review)-[:ReviewProductRelation]-(p:Product)-[]-(m:Movie) where {userName} in u.profileNames and r.mood = {mood} return m as movie, r as review order by review.mood")
    HashSet<JSONObject> findMovieAndReviewsByUserNameReviewMood(@Param("mood")String mood, @Param("userName")String userName);

}
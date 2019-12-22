package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Review;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends Neo4jRepository<Review, Long> {
    @Query("match (u:User)-[:ReviewUserRelation]-(r:Review) where u.userId = {userId} and r.time = {time} return count(r) > 0")
    Boolean existsByUserAndReviewTime(@Param("userId")String userId, @Param("time")Long time);

    @Query("Match (review:Review) with review, rand() as r order by r return review limit {limit}")
    List<Review> getRandomReviews(@Param("limit")Integer limit);

    @Query("MATCH (r:Review) return distinct r.mood")
    List<String> getMoods();
}

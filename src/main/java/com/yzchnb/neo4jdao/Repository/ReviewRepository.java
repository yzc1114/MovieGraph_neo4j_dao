package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Review;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends Neo4jRepository<Review, Long> {
    @Query("match (u:User)->(r:Review) where u.userId = {userId} and r.time = {time} return count(r) > 0")
    Boolean existsByUserAndReviewTime(@Param("userId")String userId, @Param("time")Long time);
}

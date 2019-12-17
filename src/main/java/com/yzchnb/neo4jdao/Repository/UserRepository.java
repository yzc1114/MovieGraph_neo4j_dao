package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, Long> {
    User findByUserId(String userId);

    @Query("MATCH (u:User) with u, rand() as r order by r return u.userId limit {limit}")
    List<String> getRandomUserIds(@Param("limit")Integer limit);

    @Query("MATCH (u:User) with u, rand() as r order by r return u.profileNames[0] limit {limit}")
    List<String> getRandomUserNames(@Param("limit")Integer limit);
}

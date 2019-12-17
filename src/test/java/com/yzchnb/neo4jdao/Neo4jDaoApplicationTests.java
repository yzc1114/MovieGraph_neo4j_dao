package com.yzchnb.neo4jdao;

import com.alibaba.fastjson.JSONObject;
import com.yzchnb.neo4jdao.NodeEntity.Actor;
import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import com.yzchnb.neo4jdao.Repository.ActorCollaborationRepository;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;

@SpringBootTest
class Neo4jDaoApplicationTests {

    @Resource
    private MovieRepository movieRepository;

    @Test
    void contextLoads() {

    }

    @Resource
    private ActorCollaborationRepository actorCollaborationRepository;

    @Test
    void movieRepositoryTest(){
        Actor a = new Actor("aaa");
        Actor b = new Actor("bbb");
        actorCollaborationRepository.save(new ActorCollaborationRelation(a, b, 1), 0);
    }

}

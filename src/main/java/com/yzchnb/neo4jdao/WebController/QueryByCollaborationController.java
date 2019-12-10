package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yzchnb.neo4jdao.NodeEntity.Director;
import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import com.yzchnb.neo4jdao.RelationshipEntity.DirectorActorRelation;
import com.yzchnb.neo4jdao.Repository.ActorCollaborationRepository;
import com.yzchnb.neo4jdao.Repository.DirectorActorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController("/byCollaboration")
public class QueryByCollaborationController {
    @Resource
    private ActorCollaborationRepository actorCollaborationRepository;

    @Resource
    private DirectorActorRepository directorActorRepository;

    @GetMapping("/getActorCollaborations")
    public String getActorsCollaborations(@RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        Set<ActorCollaborationRelation> res = actorCollaborationRepository.getActorCollaborationRelations(startFrom, limitation).stream().peek((r)->{
            r.getActor1().setActorCollaborationRelations(null);
            r.getActor2().setActorCollaborationRelations(null);
            r.getActor1().setDirectorActorRelations(null);
            r.getActor2().setDirectorActorRelations(null);
        }).collect(Collectors.toSet());
        //System.out.println(res);
        return JSON.toJSONString(res, SerializerFeature.DisableCircularReferenceDetect);
    }

    @GetMapping("/getDirectorActorCollaborations")
    public String getDirectorActorCollaborations(@RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){

        Set<DirectorActorRelation> res = directorActorRepository.getDirectorActorRelations(startFrom, limitation).stream().peek((r) -> {
            r.getActor().setDirectorActorRelations(null);
            r.getActor().setActorCollaborationRelations(null);
        }).collect(Collectors.toSet());
        return JSON.toJSONString(res, SerializerFeature.DisableCircularReferenceDetect);
    }

}

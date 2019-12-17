package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.NodeEntity.Actor;
import com.yzchnb.neo4jdao.NodeEntity.Director;
import com.yzchnb.neo4jdao.RelationshipEntity.ActorCollaborationRelation;
import com.yzchnb.neo4jdao.RelationshipEntity.DirectorActorRelation;
import com.yzchnb.neo4jdao.Repository.ActorCollaborationRepository;
import com.yzchnb.neo4jdao.Repository.ActorRepository;
import com.yzchnb.neo4jdao.Repository.DirectorActorRepository;
import com.yzchnb.neo4jdao.Repository.DirectorRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController(value = "/byCollaboration")
@RequestMapping("/byCollaboration")
public class QueryByCollaborationController {
    @Resource
    private ActorCollaborationRepository actorCollaborationRepository;

    @Resource
    private DirectorActorRepository directorActorRepository;

    @Resource
    private ActorRepository actorRepository;

    @Resource
    private DirectorRepository directorRepository;

    @GetMapping("/getActorsCollaborations")
    public String getActorsCollaborations(@RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() ->
            actorCollaborationRepository.getActorCollaborationRelations(startFrom, limitation).stream().peek((r)->{
                r.getActor1().setDirectorActorRelations(null);
                r.getActor2().setDirectorActorRelations(null);
                r.getActor1().setActorCollaborationRelations(null);
                r.getActor2().setActorCollaborationRelations(null);
            }).collect(Collectors.toSet())
        );
    }

    @GetMapping("/getActorsCollaborationCount")
    public String getActorsCollaborationCount(@RequestParam("actorName1")String actorName1, @RequestParam("actorName2")String actorName2){
        return Utils.wrap(() -> actorCollaborationRepository.getActorsCollaborationCount(actorName1, actorName2));
    }

    @GetMapping("/getDirectorActorCollaborationCount")
    public String getDirectorActorCollaborationCount(@RequestParam("actorName")String actorName, @RequestParam("directorName")String directorName){
        return Utils.wrap(() -> directorActorRepository.getDirectorActorCollaborationCount(actorName, directorName));
    }

    @GetMapping("/getDirectorActorCollaborations")
    public String getDirectorActorCollaborations(@RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){

        return Utils.wrap(() -> directorActorRepository.getDirectorActorRelations(startFrom, limitation).stream().peek((r) -> {
                r.getActor().setDirectorActorRelations(null);
                r.getActor().setActorCollaborationRelations(null);
            }).collect(Collectors.toSet())
        );
    }

    @GetMapping(path = "/test")
    public String test(@RequestParam Integer times) throws Exception{
        //首先得到1千个演员合作关系。
        //再得到1千个演员导演合作关系
        Set<ActorCollaborationRelation> randomActorCollaborations = actorCollaborationRepository.getRandomCollaboration(times);
        Set<DirectorActorRelation> randomDirectorActorCollaborations = directorActorRepository.getRandomCollaboration(times);

        HashMap<String, ParamsProvider> params = new HashMap<>();

        ParamsProvider paramsProviderForActorCollaborations = () -> randomActorCollaborations.stream().map((c) -> Arrays.asList(c.getActor1().getName(), c.getActor2().getName())).collect(Collectors.toList());
        ParamsProvider paramsProviderForDirectorActorCollaborations = () -> randomDirectorActorCollaborations.stream().map((c) -> Arrays.asList(c.getActor().getName(), c.getDirector().getName())).collect(Collectors.toList());
        params.put("getActorsCollaborationCount", paramsProviderForActorCollaborations);
        params.put("getDirectorActorCollaborationCount", paramsProviderForDirectorActorCollaborations);
        int actorCollaborationsCount = actorCollaborationRepository.getCountOfRelation();
        Random random = new Random();
        ParamsProvider p1 = () -> {
            List<List> a = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                a.add(new ArrayList<Integer>() { {add(random.nextInt(actorCollaborationsCount)); add(10);}});
            }
            return a;
        };
        params.put("getActorsCollaborations", p1);
        int directorActorCollaborationsCount = directorActorRepository.getCountOfRelation();
        ParamsProvider p2 = () -> {
            List<List> a = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                a.add(new ArrayList<Integer>() { {add(random.nextInt(directorActorCollaborationsCount)); add(10);}});
            }
            return a;
        };
        params.put("getDirectorActorCollaborations", p2);
        return Utils.testQueries(this.getClass(), this, params);
    }

}

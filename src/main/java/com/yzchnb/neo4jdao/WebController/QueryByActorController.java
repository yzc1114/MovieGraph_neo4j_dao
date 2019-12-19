package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.NodeEntity.Actor;
import com.yzchnb.neo4jdao.Repository.ActorRepository;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController("/byActor")
@RequestMapping("/byActor")
public class QueryByActorController {

    @Resource
    private MovieRepository movieRepository;
    @Resource
    private ActorRepository actorRepository;

    @GetMapping("/getMoviesOfStarring")
    public String getMoviesOfStarring(@RequestParam("starring") String starring){
        return Utils.wrap(() -> movieRepository.findMoviesByStarring(starring));
    }

    @GetMapping("/getMoviesOfSupportingActor")
    public String getMoviesOfSupportingActor(@RequestParam("supportingActor")String supportingActor){
        return Utils.wrap(() -> movieRepository.findMoviesBySupportingActor(supportingActor));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times) throws Exception{
        //首先得到1千个演员的名字。
        HashSet<Actor> randomActors = actorRepository.findRandomActors(times);
        HashMap<String, ParamsProvider> params = new HashMap<>();
        List<String> actorNames = randomActors.stream().map(Actor::getName).collect(Collectors.toList());
        //1.
        ParamsProvider paramsProvider = () -> actorNames.stream().map(Arrays::asList).collect(Collectors.toList());
        params.put("getMoviesOfStarring", paramsProvider);
        params.put("getMoviesOfSupportingActor", paramsProvider);
        return Utils.testQueries(this, params);
    }

    @GetMapping("/compareIndex")
    public String compareIndex(@RequestParam Integer times) throws Exception{
        return Utils.compare(this, times);
    }

}

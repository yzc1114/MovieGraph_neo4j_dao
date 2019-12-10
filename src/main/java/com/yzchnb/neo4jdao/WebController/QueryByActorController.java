package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.Repository.ActorRepository;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/byActor")
public class QueryByActorController {

    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/getMoviesOfStarring")
    public String getMoviesOfStarring(@RequestParam("starring") String starring){
        return JSON.toJSONString(movieRepository.findMoviesByStarring(starring));
    }

    @GetMapping("/getMoviesOfSupportingActor")
    public String getMoviesOfSupportingActor(@RequestParam("supportingActor")String supportingActor){
        return JSON.toJSONString(movieRepository.findMoviesBySupportingActor(supportingActor));
    }

}

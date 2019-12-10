package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/byDirector")
public class QueryByDirectorController {
    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/getMoviesByDirector")
    public String getMoviesByDirector(@RequestParam("director")String director){
        return JSON.toJSONString(movieRepository.findMoviesByDirector(director));
    }
}

package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/byTitle")
public class QueryByTitleController {
    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/getMoviesByTitle")
    public String getMoviesByTitle(@RequestParam("title")String title){
        return JSON.toJSONString(movieRepository.findMoviesByTitle(title));
    }
}

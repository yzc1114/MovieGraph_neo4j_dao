package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/byGenre")
public class QueryByGenreController {

    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/getMoviesByGenre")
    public String getMoviesByGenre(@RequestParam("genre")String genre, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return JSON.toJSONString(movieRepository.findMoviesByGenre(genre, startFrom, limitation));
    }
}

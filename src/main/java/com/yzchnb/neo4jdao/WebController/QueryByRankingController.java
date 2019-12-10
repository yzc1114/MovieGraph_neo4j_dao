package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/byRanking")
public class QueryByRankingController {
    @Resource
    private MovieRepository movieRepository;
    
    @GetMapping("/getMoviesRankingGreaterThan")
    public String getMoviesRankingGreaterThan(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return JSON.toJSONString(movieRepository.findMoviesByRankingGreaterThan(ranking, startFrom, limitation));
    }

    @GetMapping("/getMoviesByRankingGreaterThanEqual")
    public String getMoviesByRankingGreaterThanEqual(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return JSON.toJSONString(movieRepository.findMoviesByRankingGreaterThanEqual(ranking, startFrom, limitation));
    }

    @GetMapping("/getMoviesByRankingLessThan")
    public String getMoviesByRankingLessThan(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return JSON.toJSONString(movieRepository.findMoviesByRankingLessThan(ranking, startFrom, limitation));
    }

    @GetMapping("/getMoviesByRankingLessThanEqual")
    public String getMoviesByRankingLessThanEqual(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return JSON.toJSONString(movieRepository.findMoviesByRankingLessThanEqual(ranking, startFrom, limitation));
    }
}

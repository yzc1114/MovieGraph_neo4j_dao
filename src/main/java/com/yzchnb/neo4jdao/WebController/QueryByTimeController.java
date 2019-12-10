package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.NodeEntity.Movie;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;

@RestController("/byTime")
public class QueryByTimeController {

    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/")
    public String test(){
        return "t";
    }

    @GetMapping("/getMoviesByYear")
    public String getMoviesByYear(@RequestParam("year")Integer year){
        return JSON.toJSONString(movieRepository.findMoviesByYear(year));
    }

    @GetMapping("/getMoviesByYearMonth")
    public String getMoviesByYearMonth(@RequestParam("year")Integer year, @RequestParam("month")Integer month){
        return JSON.toJSONString(movieRepository.findMoviesByYearMonth(year, month));
    }

    @GetMapping("/getMoviesByYearMonthDay")
    public String getMoviesByYearMonthDay(@RequestParam("year")Integer year, @RequestParam("month")Integer month, @RequestParam("day")Integer day){
        return JSON.toJSONString(movieRepository.findMoviesByYearMonthDay(year, month, day));
    }

    @GetMapping("/getMoviesByYearSeason")
    public String getMoviesByYearSeason(@RequestParam("year")Integer year, @RequestParam("season")Integer season){
        HashSet<Movie> res = new HashSet<>();
        for(int m = (season - 1) * 3 + 1; m <= season * 3; m++){
            res.addAll(movieRepository.findMoviesByYearMonth(year, m));
        }
        return JSON.toJSONString(res);
    }

    @GetMapping("/getMoviesByWeekDay")
    public String getMoviesByWeekDay(@RequestParam("weekDay")Integer weekDay){
        return JSON.toJSONString(movieRepository.findMoviesByWeekDay(weekDay));
    }
}

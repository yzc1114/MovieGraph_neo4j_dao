package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController("/byTitle")
@RequestMapping("/byTitle")
public class QueryByTitleController {
    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/getMoviesByTitle")
    public String getMoviesByTitle(@RequestParam("title")String title){
        return Utils.wrap(() -> movieRepository.findMoviesByTitle(title));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times) throws Exception{
        List<String> randomTitles = movieRepository.getRandomTitles(times);
        ParamsProvider p = () -> randomTitles.stream().map(Arrays::asList).collect(Collectors.toList());
        HashMap<String, ParamsProvider> m = new HashMap<>();
        m.put("getMoviesByTitle", p);
        return Utils.testQueries(this.getClass(), this, m);
    }
}

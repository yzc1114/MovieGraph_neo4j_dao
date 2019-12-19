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
import java.util.*;

@RestController("/byRanking")
@RequestMapping("/byRanking")
public class QueryByRankingController {
    @Resource
    private MovieRepository movieRepository;
    
    @GetMapping("/getMoviesRankingGreaterThan")
    public String getMoviesRankingGreaterThan(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() -> movieRepository.findMoviesByRankingGreaterThan(ranking, startFrom, limitation));
    }

    @GetMapping("/getMoviesByRankingGreaterThanEqual")
    public String getMoviesByRankingGreaterThanEqual(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() -> movieRepository.findMoviesByRankingGreaterThanEqual(ranking, startFrom, limitation));
    }

    @GetMapping("/getMoviesByRankingLessThan")
    public String getMoviesByRankingLessThan(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() -> movieRepository.findMoviesByRankingLessThan(ranking, startFrom, limitation));
    }

    @GetMapping("/getMoviesByRankingLessThanEqual")
    public String getMoviesByRankingLessThanEqual(@RequestParam("ranking")Float ranking, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() -> movieRepository.findMoviesByRankingLessThanEqual(ranking, startFrom, limitation));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times) throws Exception{
        int moviesCount = movieRepository.getMoviesCount();
        Random random = new Random();
        ParamsProvider p = () -> {
            List<List> params = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                Float ranking = random.nextFloat() * 5;
                Integer startFrom = random.nextInt(moviesCount);
                params.add((Arrays.asList(ranking, startFrom, 10)));
            }
            return params;
        };
        HashMap<String, ParamsProvider> m = new HashMap<>();
        m.put("getMoviesRankingGreaterThan", p);
        m.put("getMoviesByRankingGreaterThanEqual", p);
        m.put("getMoviesByRankingLessThan", p);
        m.put("getMoviesByRankingLessThanEqual", p);
        return Utils.testQueries(this, m);
    }

    @GetMapping("/compareIndex")
    public String compareIndex(@RequestParam Integer times) throws Exception{
        return Utils.compare(this, times);
    }
}

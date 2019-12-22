package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.NodeEntity.Movie;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController("/byTime")
@RequestMapping("/byTime")
public class QueryByTimeController {

    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/getMoviesByYear")
    public String getMoviesByYear(@RequestParam("year")Integer year){
        return Utils.wrap(() ->
                movieRepository.findMoviesByReleaseYear(year)
        );
    }

    @GetMapping("/getMovieTitlesByYear")
    public String getMovieTitlesByYear(@RequestParam("year")Integer year){
        return Utils.wrap(() ->
                movieRepository.findMovieTitlesByReleaseYear(year)
        );
    }

    @GetMapping("/getMoviesByYearMonth")
    public String getMoviesByYearMonth(@RequestParam("year")Integer year, @RequestParam("month")Integer month){
        return Utils.wrap(() -> movieRepository.findMoviesByYearMonth(year, month));
    }

    @GetMapping("/getMoviesByYearMonthDay")
    public String getMoviesByYearMonthDay(@RequestParam("year")Integer year, @RequestParam("month")Integer month, @RequestParam("day")Integer day){
        return Utils.wrap(() -> movieRepository.findMoviesByYearMonthDay(year, month, day));
    }

    @GetMapping("/getMoviesByYearSeason")
    public String getMoviesByYearSeason(@RequestParam("year")Integer year, @RequestParam("season")Integer season){
        return Utils.wrap(() -> {
            HashSet<Movie> res = new HashSet<>();
            for(int m = (season - 1) * 3 + 1; m <= season * 3; m++){
                res.addAll(movieRepository.findMoviesByYearMonth(year, m));
            }
            return res;
        });
    }

    @GetMapping("/getMovieTitlesByYearSeason")
    public String getMovieTitlesByYearSeason(@RequestParam("year")Integer year, @RequestParam("season")Integer season){
        return Utils.wrap(() -> {
            HashSet<String> res = new HashSet<>();
            for(int m = (season - 1) * 3 + 1; m <= season * 3; m++){
                res.addAll(movieRepository.findMovieTitlesByYearMonth(year, m));
            }
            return res;
        });
    }

    @GetMapping("/getMoviesByWeekDay")
    public String getMoviesByWeekDay(@RequestParam("weekDay")Integer weekDay, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() -> movieRepository.findMoviesByWeekDay(weekDay, startFrom, limitation));
    }

    @GetMapping("/getMovieTitlesByWeekDay")
    public String getMovieTitlesByWeekDay(@RequestParam("weekDay")Integer weekDay, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() -> movieRepository.findMovieTitlesByWeekDay(weekDay, startFrom, limitation));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times) throws Exception{
        List<Integer> years = movieRepository.getYears();
        HashMap<Integer, Integer> weekDay2MoviesCount = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            weekDay2MoviesCount.put(i + 1, movieRepository.getMoviesCountOfWeekDay(i + 1));
        }
        Random random = new Random();
        ParamsProvider getMoviesByYear = () -> {
            List<List> params = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                params.add(Collections.singletonList(years.get(random.nextInt(years.size() - 1))));
            }
            return params;
        };
        ParamsProvider getMoviesByYearMonth = () -> {
            List<List> params = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                params.add(Arrays.asList(years.get(random.nextInt(years.size())), random.nextInt(12) + 1));
            }
            return params;
        };
        ParamsProvider getMoviesByYearMonthDay = () -> {
            List<List> params = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                params.add(Arrays.asList(years.get(random.nextInt(years.size())), random.nextInt(12) + 1, random.nextInt(28) + 1));
            }
            return params;
        };
        ParamsProvider getMoviesByYearSeason = () -> {
            List<List> params = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                params.add(Arrays.asList(years.get(random.nextInt(years.size())), random.nextInt(4) + 1));
            }
            return params;
        };
        ParamsProvider getMoviesByWeekDay = () -> {
            List<List> params = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                int weekDay = random.nextInt(7) + 1;
                params.add(Arrays.asList(weekDay, random.nextInt(weekDay2MoviesCount.get(weekDay)), 10));
            }
            return params;
        };
        HashMap<String, ParamsProvider> m = new HashMap<>();
        m.put("getMoviesByYear", getMoviesByYear);
        m.put("getMoviesByYearMonth", getMoviesByYearMonth);
        m.put("getMoviesByYearMonthDay", getMoviesByYearMonthDay);
        m.put("getMoviesByYearSeason", getMoviesByYearSeason);
        m.put("getMoviesByWeekDay", getMoviesByWeekDay);
        return Utils.testQueries(this, m);
    }

    @GetMapping("/compareIndex")
    public String compareIndex(@RequestParam Integer times) throws Exception{
        return Utils.compare(this, times);
    }
}

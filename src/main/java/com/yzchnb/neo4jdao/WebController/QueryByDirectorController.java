package com.yzchnb.neo4jdao.WebController;

import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.NodeEntity.Director;
import com.yzchnb.neo4jdao.Repository.DirectorRepository;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController("/byDirector")
@RequestMapping("/byDirector")
public class QueryByDirectorController {
    @Resource
    private MovieRepository movieRepository;

    @Resource
    private DirectorRepository directorRepository;

    @GetMapping("/getMoviesByDirector")
    public String getMoviesByDirector(@RequestParam("director")String director){
        return Utils.wrap(() -> movieRepository.findMoviesByDirector(director));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times) throws Exception{
        Set<Director> randomDirectors = directorRepository.findRandomDirectors(times);
        ParamsProvider p = () -> randomDirectors.stream().map(Director::getName).map(Arrays::asList).collect(Collectors.toList());
        HashMap<String, ParamsProvider> m = new HashMap<>();
        m.put("getMoviesByDirector", p);
        return Utils.testQueries(this, m);
    }

    @GetMapping("/compareIndex")
    public String compareIndex(@RequestParam Integer times) throws Exception{
        return Utils.compare(this, times);
    }
}

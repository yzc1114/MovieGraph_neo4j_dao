package com.yzchnb.neo4jdao.WebController;

import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("/byGenreActor")
@RequestMapping("/byGenreActor")
public class QueryByGenreActorController {

    @Resource
    private MovieRepository movieRepository;

    @GetMapping("/getMovieTitlesByGenreActor")
    public String getMovieTitlesByGenreActor(@RequestParam String genre, @RequestParam String actor){
        return Utils.wrap(() -> movieRepository.findMovieTitlesByGenreActor(actor, genre));
    }

}

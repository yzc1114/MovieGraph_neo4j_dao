package com.yzchnb.neo4jdao.WebController;

import com.alibaba.fastjson.JSON;
import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.NodeEntity.Director;
import com.yzchnb.neo4jdao.NodeEntity.Genre;
import com.yzchnb.neo4jdao.Repository.GenreRepository;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController("/byGenre")
@RequestMapping("/byGenre")
public class QueryByGenreController {

    @Resource
    private MovieRepository movieRepository;

    @Resource
    private GenreRepository genreRepository;

    @GetMapping("/getMoviesByGenre")
    public String getMoviesByGenre(@RequestParam("genre")String genre, @RequestParam("startFrom")Integer startFrom, @RequestParam("limitation")Integer limitation){
        return Utils.wrap(() -> movieRepository.findMoviesByGenre(genre, startFrom, limitation));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times) throws Exception{
        List<Genre> genres = new ArrayList<>(genreRepository.getAllGenres());
        HashMap<Genre, Integer> genre2MoviesCount = new HashMap<>();
        for (Genre genre : genres) {
            genre2MoviesCount.put(genre, genreRepository.getGenreCount(genre.getType()));
        }
        Random random = new Random();

        ParamsProvider p = () -> {
            List<List> res = new ArrayList<>(times);
            for (int i = 0; i < times; i++) {
                List ps = new ArrayList();
                Genre g = genres.get(i % genres.size());
                ps.add(g.getType());
                ps.add(random.nextInt(genre2MoviesCount.get(g)));
                ps.add(10);
                res.add(ps);
            }
            return res;
        };
        HashMap<String, ParamsProvider> m = new HashMap<>();
        m.put("getMoviesByGenre", p);
        return Utils.testQueries(this, m);
    }

    @GetMapping("/compareIndex")
    public String compareIndex(@RequestParam Integer times) throws Exception{
        return Utils.compare(this, times);
    }
}

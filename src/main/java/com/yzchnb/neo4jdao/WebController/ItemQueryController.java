package com.yzchnb.neo4jdao.WebController;

import com.yzchnb.neo4jdao.Repository.*;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/item")
public class ItemQueryController {

    @Resource
    private ActorRepository actorRepository;

    @Resource
    private DirectorRepository directorRepository;

    @Resource
    private GenreRepository genreRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private ReviewRepository reviewRepository;

    @Resource
    private MovieRepository movieRepository;

    @Resource
    private ProductRepository productRepository;

    @GetMapping("/getRandomActors")
    public String getRandomActors(@RequestParam Integer count){
        return Utils.wrap(() -> actorRepository.findRandomActors(count));
    }

    @GetMapping("/getRandomDirectors")
    public String getRandomDirectors(@RequestParam Integer count){
        return Utils.wrap(() -> directorRepository.findRandomDirectors(count));
    }

    @GetMapping("/getGenres")
    public String getGenres(){
        return Utils.wrap(() -> genreRepository.getAllGenres());
    }

    @GetMapping("/getRandomUsers")
    public String getRandomUsers(@RequestParam Integer count) {
        return Utils.wrap(() -> userRepository.getRandomUsers(count));
    }

    @GetMapping("/getRandomReviews")
    public String getRandomReviews(@RequestParam Integer count){
        return Utils.wrap(() -> reviewRepository.getRandomReviews(count));
    }

    @GetMapping("/getRandomMovies")
    public String getRandomMovies(@RequestParam Integer count){
        return Utils.wrap(() -> movieRepository.getRandomMovies(count));
    }

    @GetMapping("/getRandomProducts")
    public String getRandomProducts(@RequestParam Integer count){
        return Utils.wrap(() -> productRepository.getRandomProducts(count));
    }
}

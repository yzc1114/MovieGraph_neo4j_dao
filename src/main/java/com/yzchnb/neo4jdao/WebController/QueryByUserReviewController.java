package com.yzchnb.neo4jdao.WebController;

import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Repository.UserRepository;
import com.yzchnb.neo4jdao.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@RestController("/byUserReview")
@RequestMapping("/byUserReview")
public class QueryByUserReviewController {
    @Resource
    private MovieRepository movieRepository;
    @Resource
    private UserRepository userRepository;

    @GetMapping("/getMovieAndReviewsByUserIdScoreGreaterThan")
    public String getMovieAndReviewsByUserIdScoreGreaterThan(@RequestParam("userId")String userId, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserIdReviewScoreGreaterThan(score, userId));
    }

    @GetMapping("/getMovieAndReviewsByUserIdReviewScoreGreaterThanEqual")
    public String getMovieAndReviewsByUserIdReviewScoreGreaterThanEqual(@RequestParam("userId")String userId, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserIdReviewScoreGreaterThanEqual(score, userId));
    }

    @GetMapping("/getMovieAndReviewsByUserIdReviewScoreLessThan")
    public String getMovieAndReviewsByUserIdReviewScoreLessThan(@RequestParam("userId")String userId, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserIdReviewScoreLessThan(score, userId));
    }

    @GetMapping("/getMovieAndReviewsByUserIdReviewScoreLessThanEqual")
    public String getMovieAndReviewsByUserIdReviewScoreLessThanEqual(@RequestParam("userId")String userId, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserIdReviewScoreLessThanEqual(score, userId));
    }

    @GetMapping("/getMovieAndReviewsByUserNameReviewScoreGreaterThan")
    public String getMovieAndReviewsByUserNameReviewScoreGreaterThan(@RequestParam("userName")String userName, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserNameReviewScoreGreaterThan(score, userName));
    }

    @GetMapping("/getMovieAndReviewsByUserNameReviewScoreGreaterThanEqual")
    public String getMovieAndReviewsByUserNameReviewScoreGreaterThanEqual(@RequestParam("userName")String userName, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserNameReviewScoreGreaterThanEqual(score, userName));
    }

    @GetMapping("/getMovieAndReviewsByUserNameReviewScoreLessThan")
    public String getMovieAndReviewsByUserNameReviewScoreLessThan(@RequestParam("userName")String userName, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserNameReviewScoreLessThan(score, userName));
    }

    @GetMapping("/getMovieAndReviewsByUserNameReviewScoreLessThanEqual")
    public String getMovieAndReviewsByUserNameReviewScoreLessThanEqual(@RequestParam("userName")String userName, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserNameReviewScoreLessThanEqual(score, userName));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times) throws Exception{
        List<Method> methods = new ArrayList<>(Arrays.asList(this.getClass().getDeclaredMethods()));
        methods.removeIf((m) -> !m.getName().startsWith("get"));

        Random random = new Random();

        List<String> randomUserIds = userRepository.getRandomUserIds(times);
        List<String> randomUserNames = userRepository.getRandomUserNames(times);

        ParamsProvider pForUserId = () -> randomUserIds.stream().map((userId) -> Arrays.asList(userId, random.nextFloat() * 5)).map(Arrays::asList).collect(Collectors.toList());
        ParamsProvider pForUserNames = () -> randomUserNames.stream().map((userName) -> Arrays.asList(userName, random.nextFloat() * 5)).map(Arrays::asList).collect(Collectors.toList());

        HashMap<String, ParamsProvider> m = new HashMap<>();

        for (Method method : methods) {
            if(method.getName().startsWith("getMovieAndReviewsByUserId")){
                m.put(method.getName(), pForUserId);
            }else if(method.getName().startsWith("getMovieAndReviewsByUserName")){
                m.put(method.getName(), pForUserNames);
            }
        }
        return Utils.testQueries(this.getClass(), this, m);
    }

}

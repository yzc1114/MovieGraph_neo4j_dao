package com.yzchnb.neo4jdao.WebController;

import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
import com.yzchnb.neo4jdao.NodeEntity.User;
import com.yzchnb.neo4jdao.Repository.MovieRepository;
import com.yzchnb.neo4jdao.Repository.ReviewRepository;
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
    @Resource
    private ReviewRepository reviewRepository;

    //////////TODO 接口变了
    @GetMapping("/getMovieAndReviewsByUserNameReviewMood")
    public String getMovieAndReviewsByUserNameReviewMood(@RequestParam("userName")String userName, @RequestParam("mood")String mood){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserNameReviewMood(mood, userName));
    }

    @GetMapping("/getMovieAndReviewsByUserNameReviewScore")
    public String getMovieAndReviewsByUserNameReviewScore(@RequestParam("userName")String userName, @RequestParam("score")Float score){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserNameReviewScoreGreaterThan(score, userName));
    }

    @GetMapping("/getMovieAndReviewsByUserIdReviewMood")
    public String getMovieAndReviewsByUserIdReviewMood(@RequestParam("userId")String userId, @RequestParam("mood")String mood){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserIdReviewMood(mood, userId));
    }

    @GetMapping("/test")
    public String test(@RequestParam Integer times){
        List<Method> methods = new ArrayList<>(Arrays.asList(this.getClass().getDeclaredMethods()));
        methods.removeIf((m) -> !m.getName().startsWith("get"));

        Random random = new Random();

        List<User> randomUsers = userRepository.getRandomUsers(times);
        List<String> moods = reviewRepository.getMoods();

        ParamsProvider pForUserId = () -> randomUsers.stream().map((user) -> Arrays.asList(user.getUserId(), moods.get(random.nextInt(moods.size())))).collect(Collectors.toList());
        ParamsProvider pForUserNames = () -> randomUsers.stream().map((user) -> Arrays.asList(user.getProfileNames().iterator().next(),  moods.get(random.nextInt(moods.size())))).collect(Collectors.toList());

        HashMap<String, ParamsProvider> m = new HashMap<>();

        for (Method method : methods) {
            if(method.getName().startsWith("getMovieAndReviewsByUserId")){
                m.put(method.getName(), pForUserId);
            }else if(method.getName().startsWith("getMovieAndReviewsByUserName")){
                m.put(method.getName(), pForUserNames);
            }
        }
        return Utils.testQueries(this, m);
    }

    @GetMapping("/compareIndex")
    public String compareIndex(@RequestParam Integer times) throws Exception{
        return Utils.compare(this, times);
    }
}

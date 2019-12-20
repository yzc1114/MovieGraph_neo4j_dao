package com.yzchnb.neo4jdao.WebController;

import com.yzchnb.neo4jdao.LamdbaDefination.ParamsProvider;
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

    @GetMapping("/getMovieAndReviewsByUserNameReviewMood")
    public String getMovieAndReviewsByUserNameReviewMood(@RequestParam("userId")String userId, @RequestParam("mood")String mood){
        return Utils.wrap(() -> movieRepository.findMovieAndReviewsByUserNameReviewMood(mood, userId));
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

        List<String> randomUserIds = userRepository.getRandomUserIds(times);
        List<String> randomUserNames = userRepository.getRandomUserNames(times);

        List<String> moods = reviewRepository.getMoods();

        ParamsProvider pForUserId = () -> randomUserIds.stream().map((userId) -> Arrays.asList(userId, moods.get(random.nextInt(moods.size())))).map(Arrays::asList).collect(Collectors.toList());
        ParamsProvider pForUserNames = () -> randomUserNames.stream().map((userName) -> Arrays.asList(userName,  moods.get(random.nextInt(moods.size())))).map(Arrays::asList).collect(Collectors.toList());

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

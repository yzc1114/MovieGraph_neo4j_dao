package com.yzchnb.neo4jdao;

import com.yzchnb.neo4jdao.NodeEntity.Product;
import com.yzchnb.neo4jdao.NodeEntity.Review;
import com.yzchnb.neo4jdao.NodeEntity.User;
import com.yzchnb.neo4jdao.Repository.ProductRepository;
import com.yzchnb.neo4jdao.Repository.ReviewRepository;
import com.yzchnb.neo4jdao.Repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

@Component
public class ReviewLoader {


    private static ReviewRepository reviewRepository;
    private static UserRepository userRepository;
    private static ProductRepository productRepository;

    @Resource
    private ReviewRepository r;
    @Resource
    private UserRepository u;
    @Resource
    private ProductRepository p;
    @PostConstruct
    public void init(){
        reviewRepository = r;
        userRepository = u;
        productRepository = p;
    }

    private static BufferedReader br;

    public static void main(String[] args) throws Exception {
        //ReviewLoader.loadData();
        br = new BufferedReader(new FileReader("/Users/purchaser/Desktop/review.csv"));
        br.readLine();
        HashSet<String> keys = new HashSet<>();
        while(true){
            String l = br.readLine();
            if(l == null)
                break;
            String s = l.split("\\t")[6];
            String userId = l.split("\\t")[1];
            String time = l.split("\\t")[5];
            keys.add(s + userId + time);
        }
        System.out.println(keys.size());
    }

    public static void loadData() throws Exception{
        br = new BufferedReader(new FileReader("/Users/purchaser/Desktop/review.csv"));
        br.readLine();
        while(true){
            String l = br.readLine();
            if(l == null){
                break;
            }
            //System.out.println(l);
            try{
                String[] ss = l.split("\\t");
                String userId = ss[1];
                String profileName = ss[2];
                String helpfulness = ss[3];
                String score = ss[4];
                String time = ss[5];
                String summary = ss[6];
                String text = ss[7];
                String productId = ss[8];
                String mood = ss[9];
                User user = new User(userId, new HashSet<String>(){{add(profileName);}});
                Review r = new Review(helpfulness, Float.parseFloat(score), Long.parseLong(time), summary, text, mood);
                loadOneReview(r, user, productId);
            }catch (Exception e){
                //
                e.printStackTrace();
            }

        }
        System.out.println("finished");
    }

    private static void loadOneReview(Review review, User user, String productId){
        if(reviewRepository.existsByUserAndReviewTime(user.getUserId(), review.getTime())){
            //重复评论
            //System.out.println("重复评论：" + review.getSummary());
            return;
        }
        Product searchedProduct = productRepository.getByProductId(productId);
        if(searchedProduct != null){
            review.setProduct(searchedProduct);
        }else{
            review.setProduct(new Product(productId, null, null));
        }
        User searchedUser = userRepository.findByUserId(user.getUserId());
        if(searchedUser != null){
            searchedUser.getProfileNames().addAll(user.getProfileNames());
            review.setUser(searchedUser);
        }else{
            review.setUser(user);
        }
        reviewRepository.save(review, 1);
        System.out.println("saved review, id = " + review.getId());
    }
}

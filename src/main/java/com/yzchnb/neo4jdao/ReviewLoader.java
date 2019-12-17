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

@Component
public class ReviewLoader {


    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

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

    public static void main(String[] args) {

    }

    private void loadOneReview(Review review){
        User user = review.getUser();
        Product product = review.getProduct();
        if(reviewRepository.existsByUserAndReviewTime(user.getUserId(), review.getTime())){
            //重复评论
            return;
        }
        Product searchedProduct = productRepository.findByProductIdAndFormatAndPrice(product.getProductId(), product.getFormat(), product.getPrice());
        if(searchedProduct != null){
            review.setProduct(searchedProduct);
        }
        User searchedUser = userRepository.findByUserId(user.getUserId());
        if(searchedUser != null){
            searchedUser.getProfileNames().addAll(user.getProfileNames());
            review.setUser(searchedUser);
        }
        reviewRepository.save(review, 1);
    }
}

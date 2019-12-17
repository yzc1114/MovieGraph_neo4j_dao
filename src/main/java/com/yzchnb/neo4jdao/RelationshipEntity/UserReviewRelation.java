package com.yzchnb.neo4jdao.RelationshipEntity;

import com.yzchnb.neo4jdao.NodeEntity.Review;
import com.yzchnb.neo4jdao.NodeEntity.User;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "UserReviewRelation")
public class UserReviewRelation {
    @Id
    @GeneratedValue
    Long id;

    @StartNode
    private Review review;

    @EndNode
    private User user;

    public UserReviewRelation(Review review, User user) {
        this.review = review;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

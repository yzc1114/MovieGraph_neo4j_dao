package com.yzchnb.neo4jdao.RelationshipEntity;

import com.yzchnb.neo4jdao.NodeEntity.Product;
import com.yzchnb.neo4jdao.NodeEntity.Review;
import com.yzchnb.neo4jdao.NodeEntity.User;
import org.neo4j.ogm.annotation.*;

@RelationshipEntity(type = "ReviewProductRelation")
public class ReviewProductRelation {
    @Id
    @GeneratedValue
    Long id;

    @StartNode
    private Review review;

    @EndNode
    private Product product;

    public ReviewProductRelation(Review review, Product product) {
        this.review = review;
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

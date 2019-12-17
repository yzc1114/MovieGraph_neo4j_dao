package com.yzchnb.neo4jdao.NodeEntity;

import org.neo4j.ogm.annotation.*;

@NodeEntity
public class Review {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "helpfulness")
    private String helpfulness;

    @Property(name = "score")
    private Float score;

    @Property(name = "time")
    private Long time;

    @Property(name = "summary")
    private String summary;

    @Property(name = "text")
    private String text;

    @Relationship(type = "ReviewProductRelation")
    private Product product;

    @Relationship(type = "ReviewUserRelation")
    private User user;

    public Review(String helpfulness, Float score, Long time, String summary, String text, Product product, User user) {
        this.helpfulness = helpfulness;
        this.score = score;
        this.time = time;
        this.summary = summary;
        this.text = text;
        this.product = product;
        this.user = user;
    }

    public String getHelpfulness() {
        return helpfulness;
    }

    public void setHelpfulness(String helpfulness) {
        this.helpfulness = helpfulness;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

package com.yzchnb.neo4jdao.NodeEntity;

import org.neo4j.ogm.annotation.*;

@NodeEntity(label = "Product")
public class Product {
    @Id
    @GeneratedValue
    Long id;

    @Property(name = "productId")
    private String productId;

    @Property(name = "format")
    private String format;

    @Property(name = "price")
    private String price;

    public Product(String productId, String format, String price) {
        this.productId = productId;
        this.format = format;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productId='" + productId + '\'' +
                ", format='" + format + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

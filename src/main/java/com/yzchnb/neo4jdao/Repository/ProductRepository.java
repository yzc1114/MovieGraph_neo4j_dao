package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Product;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface ProductRepository extends Neo4jRepository<Product, Long> {
    Boolean existsByProductId(String productId);

    @Query("match (p:Product) where p.productId = {productId} and p.format = {format} and p.price = {price} return p limit 1")
    Product findByProductIdAndFormatAndPrice(@Param("productId") String productId,
                                             @Param("format") String format,
                                             @Param("price") String price);


    @Query("MATCH (p:Product) with rand() as r, p order by r return p limit {limit}")
    Set<Product> getRandomProducts(@Param("limit")Integer limit);
}

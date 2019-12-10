package com.yzchnb.neo4jdao.Repository;

import com.yzchnb.neo4jdao.NodeEntity.Product;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ProductRepository extends Neo4jRepository<Product, Long> {
    Boolean existsByProductId(String productId);
}

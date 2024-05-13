package com.teamproject.garngalore.Repositories;

import com.teamproject.garngalore.Models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {


    List<Product> findByNameContainingIgnoreCase(String keyword);

    Collection<? extends Product> findByCategoryContainingIgnoreCase(String keyword);

    Collection<? extends Product> findByColorContainingIgnoreCase(String keyword);

    Collection<? extends Product> findByDescriptionContainingIgnoreCase(String keyword);
}


package com.teamproject.garngalore.config;


import com.teamproject.garngalore.Models.Product;
import com.teamproject.garngalore.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Autowired
    public ProductSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        // Check if products already exist
        List<Product> existingProducts = productRepository.findAll();

        // If no products exist, seed product data
        if (existingProducts.isEmpty()) {
            seedProducts();
        }
    }

    private void seedProducts() {
        // Seed product data
        Product product1 = new Product();
        product1.setName("Lila ekologiskt garn");
        product1.setPrice(100);
        product1.setDescription("Ekologiskt garn i lila färg.");
        product1.setCategory("Ekologiskt garn");
        product1.setQuantity(10);
        product1.setBild("/images/lila garn.png");
        product1.setColor("Lila");

        Product product2 = new Product();
        product2.setName("Grön ekologiskt garn");
        product2.setPrice(100);
        product2.setDescription("Ekologiskt garn i grön färg.");
        product2.setCategory("Ekologiskt garn");
        product2.setQuantity(10);
        product2.setBild("/images/grönt ullgarn.png");
        product2.setColor("Grön");

        Product product3 = new Product();
        product3.setName("Gult självlysande garn");
        product3.setPrice(150);
        product3.setDescription("Självlysande garn i gul färg.");
        product3.setCategory("Självlysande garn");
        product3.setQuantity(10);
        product3.setBild("/images/Gult självlysande garn.png");
        product3.setColor("Gul");

        Product product4 = new Product();
        product4.setName("Rosa självlysande garn");
        product4.setPrice(150);
        product4.setDescription("Självlysande garn i rosa färg.");
        product4.setCategory("Självlysande garn");
        product4.setQuantity(10);
        product4.setBild("/images/rosa självlysande garn.png");
        product4.setColor("Rosa");

        Product product5 = new Product();
        product5.setName("Blått handgjort ullgarn");
        product5.setPrice(200);
        product5.setDescription("Handgjort ullgarn i blå färg.");
        product5.setCategory("Handgjort ullgarn");
        product5.setQuantity(10);
        product5.setBild("/images/Blått ullgarn.jpg");
        product5.setColor("Blå");

        Product product6 = new Product();
        product6.setName("Gult handgjort ullgarn");
        product6.setPrice(200);
        product6.setDescription("Handgjort ullgarn i gul färg.");
        product6.setCategory("Handgjort ullgarn");
        product6.setQuantity(10);
        product6.setBild("/images/Gul ullgarn.jpg");
        product6.setColor("Gul");

        Product product7 = new Product();
        product7.setName("Grått Alpackagarn");
        product7.setPrice(250);
        product7.setDescription("Alpackagarn i grå färg.");
        product7.setCategory("Alpackagarn");
        product7.setQuantity(10);
        product7.setBild("/images/grå alpackagarn.jpg");
        product7.setColor("Grått");

        Product product8 = new Product();
        product8.setName("Vitt Alpackagarn");
        product8.setPrice(250);
        product8.setDescription("Alpackagarn i vit färg.");
        product8.setCategory("Alpackagarn");
        product8.setQuantity(10);
        product8.setBild("/images/Vitt Alpacka.jpg");
        product8.setColor("Vit");

        // Save products to the database
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        productRepository.save(product7);
        productRepository.save(product8);
    }
}

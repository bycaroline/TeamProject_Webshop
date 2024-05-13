package com.teamproject.garngalore.Services;

import com.teamproject.garngalore.Models.Product;
import com.teamproject.garngalore.Repositories.ProductRepository;
import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public void createUser(Product product) {
        productRepository.save(product);
    }

    public Product getUserById(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new MongoException("Produkt med ID " + id + " hittades inte.");
        }
    }

    public void deleteProduct(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new MongoException("Produkt med ID " + id + " finns inte i databasen.");
        }
    }

    public void updateProduct(String id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            productRepository.save(product);
        } else {
            throw new MongoException("Produkt med ID " + id + " finns inte i databasen.");
        }
    }

    public Iterable<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(String productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        } else {
            throw new MongoException("Produkt med ID " + productId + " hittades inte.");
        }
    }

    //Reduces the quantity of a product by the given amount, when a product is bought (when cart is paid for)
    public void updateProductQuantity(String id, int quantity) {
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setQuantity(quantity);
            productRepository.save(product);
        } else {
            throw new MongoException("Produkt med ID " + id + " hittades inte.");
        }
    }

    public void decreaseProductStock(String productId, int quantitySold) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            int currentStock = product.getQuantity();
            if (currentStock >= quantitySold) {
                product.setQuantity(currentStock - quantitySold);
                productRepository.save(product);
            } else {
                // Hantera fallet där det inte finns tillräckligt med lager
                // Till exempel kasta ett undantag eller logga ett meddelande
            }
        }
    }


}

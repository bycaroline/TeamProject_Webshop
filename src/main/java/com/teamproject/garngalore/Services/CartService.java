package com.teamproject.garngalore.Services;

import com.teamproject.garngalore.Models.Cart;
import com.teamproject.garngalore.Models.Product;
import com.teamproject.garngalore.Repositories.CartRepository;
import com.mongodb.MongoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {


    private final CartRepository cartRepository;

    // Konstruktör för att injicera CartRepository
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }


    public void save(Cart cart) {
        cartRepository.save(cart);
    }

    public void saveOrUpdateCart(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart getCartFromSession() {
        Optional<Cart> optionalCart = cartRepository.findById("1");

        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            Cart cart = new Cart();
            cart.setId("1");
            cartRepository.save(cart);
            return cart;
        }
    }
}
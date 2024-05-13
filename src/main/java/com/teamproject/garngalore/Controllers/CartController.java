package com.teamproject.garngalore.Controllers;

import com.teamproject.garngalore.Models.Cart;
import com.teamproject.garngalore.Models.Order;
import com.teamproject.garngalore.Models.Product;
import com.teamproject.garngalore.Models.User;
import com.teamproject.garngalore.Services.CartService;
import com.teamproject.garngalore.Services.OrderService;
import com.teamproject.garngalore.Services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
//@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    private final ProductService productService;


    @Autowired
    public CartController(CartService cartService, OrderService orderService, ProductService productService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String showCartPage(Model model, HttpSession session) {
        // Retrieve the cart associated with the user's session
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            // If there is no cart associated with the session, you can handle it here
            model.addAttribute("message", "No cart found for the current session");
        } else {
            // If the cart exists, send it to the view to display the information
            model.addAttribute("cart", cart);
        }
        return "Cart";
    }

    @PostMapping("/startCartSession")
    public ResponseEntity<Map<String, Object>> startCartSession(HttpSession session) {
        // Initialize the cart session if not already initialized
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        // Send a response to the client
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "Cart session started successfully");

        return ResponseEntity.ok(jsonResponse);
    }

    //för att visa antalet produkter i kundvagnen i navbaren
    @GetMapping("/getCartItemCount")
    public ResponseEntity<Map<String, Integer>> getCartItemCount(HttpSession session) {
        // Retrieve the cart from session
        Cart cart = (Cart) session.getAttribute("cart");

        // Calculate the number of products in the cart
        int itemCount = 0;
        if (cart != null) {
            itemCount = cart.getNumberOfProducts();
        }

        // Return the number of products in the cart
        Map<String, Integer> response = new HashMap<>();
        response.put("cartItemCount", itemCount);

        return ResponseEntity.ok(response);
    }

    //För att tömma kundvagnen
    @PostMapping ("/cart/clearCart")
    public String clearCart(HttpSession session) {
        //Hämta kundvagnen som är kopplad till användarens session
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            //Om det inte finns någon kundvagn kopplad till sessionen, hantera det här
            return "redirect:/cart";
        }

        // Clear kundvagnen
        cart.getProductList().clear();
        cart.setTotalPrice(0);
        cart.setNumberOfProducts(0);

        // Spara den uppdaterade kundvagnen till sessionen
        session.setAttribute("cart", cart);

        return "redirect:/cart";
    }

    //betalsidan
    @GetMapping("/paymentPage")
    public String showPaymentPage(Model model, HttpSession session) {
        // Hämta kundvagnen från sessionen
        Cart cart = (Cart) session.getAttribute("cart");
        // Om kundvagnen är null, skapa en ny
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        // Lägg till kundvagnen i modellen
        model.addAttribute("cart", cart);
        return "PaymentPage";
    }

    //placera order
    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam("paymentMethod") String paymentMethod, HttpSession session, Model model) {
        //Hämta kundvagnen som är kopplad till användarens session
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            //Om det inte finns någon kundvagn kopplad till sessionen, hantera det här
            return "redirect:/cart";
        }

        //skapa en ny order, baserat på kundvagnen
        Order order = new Order();
        order.setProducts(cart.getProductList());
        order.setTotalPrice(cart.getTotalPrice());
        orderService.saveOrder(order);

        // Uppdatera lagersaldot för varje produkt i kundvagnen
        for (Product productInCart : cart.getProductList()) {
            int quantityOrdered = 1; // Set quantity ordered to 1 for each product
            productService.decreaseProductStock(productInCart.getId(), quantityOrdered); // Update stock count
        }


        // Töm kundvagnen
        cart.getProductList().clear();
        cart.setTotalPrice(0);
        cart.setNumberOfProducts(0);

        // Spara den uppdaterade kundvagnen till sessionen
        session.setAttribute("cart", cart);

        // Skicka orderobjektet till confirmationPage
        model.addAttribute("order", order);

        // Visa bekräftelsesidan
        return "ConfirmationPage";
    }
}


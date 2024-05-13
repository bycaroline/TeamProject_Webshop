package com.teamproject.garngalore.Controllers;

import com.teamproject.garngalore.Models.Cart;
import com.teamproject.garngalore.Models.Product;
import com.teamproject.garngalore.Repositories.ProductRepository;
import com.teamproject.garngalore.Services.CartService;
import com.teamproject.garngalore.Services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Uppdaterat import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService; // Använd beroendeinjektion för att injicera CartService
    }



    @GetMapping("/search")
    public String listProducts(Model model, @RequestParam(required = false) String keyword) {
        List<Product> products;

        if (keyword == null || keyword.isEmpty()) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByNameContainingIgnoreCase(keyword);
            products.addAll(productRepository.findByCategoryContainingIgnoreCase(keyword));
            products.addAll(productRepository.findByColorContainingIgnoreCase(keyword));
            products.addAll(productRepository.findByDescriptionContainingIgnoreCase(keyword));
        }

        model.addAttribute("products", products);
        //Visar resultatsidan.
        return "ShowSearchPage";
    }
    @GetMapping("/products")
    public String listAllProducts(Model model) {
        List<Product> products = productRepository.findAll();

        model.addAttribute("products", products);
        //Visar resultatsidan.
        return "AllProductsPage";
    }
    @GetMapping("/product/{id}")
    public String showProduct(@PathVariable String id, Model model) {
        Product product = productRepository.findById(id).orElse(null);
        model.addAttribute("product", product);
        return "productInfo";
    }
    @GetMapping("/")
    public String showIndex(Model model) {
        // Hämta alla produkter från databasen
        List<Product> allProducts = productRepository.findAll();

        // Skapa en lista för att lagra slumpmässiga produkter
        List<Product> randomProducts = getRandomProducts(allProducts, 4); // Hämta 4 slumpmässiga produkter

        // Skicka de slumpmässiga produkterna till vyn
        model.addAttribute("products", randomProducts);

        return "index"; // Namnet på din startsida-HTML-templaten
    }

    // Metod för att hämta ett slumpmässigt antal produkter från en lista
    private List<Product> getRandomProducts(List<Product> productList, int count) {
        List<Product> randomProducts = new ArrayList<>();

        // Skapa en kopia av produktlistan för att undvika att ändra original listan
        List<Product> copyOfProductList = new ArrayList<>(productList);

        // Skapa en instans av Random
        Random random = new Random();

        // Begränsa antalet produkter att välja från om listan är mindre än count
        int maxIndex = Math.min(count, copyOfProductList.size());

        // Hämta slumpmässiga och unika produkter från listan
        for (int i = 0; i < maxIndex; i++) {
            // Generera ett slumpmässigt index inom det återstående intervallet av produkter
            int randomIndex = random.nextInt(copyOfProductList.size());

            // Hämta produkten vid det slumpmässiga indexet och lägg till den i randomProducts
            randomProducts.add(copyOfProductList.remove(randomIndex));
        }

        return randomProducts;
    }

    @PostMapping("/addToCart/{productId}")
    public ResponseEntity<Map<String, Object>> addToCart(@PathVariable("productId") String productId, HttpSession session) {
        //Hämta en kundvagn associerad på användarens session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        //Hämta produkten baserat på det angivna produkt-ID:t
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Product not found"));
        }

        //Lägg till produkten i kundvagnen
        cart.getProductList().add(product);
        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());
        cart.setNumberOfProducts(cart.getNumberOfProducts() + 1);

        //Att spara eller uppdatera kundvagnen brukar inte behövas för sessionbaserade kundvagnar

        //Skicka ett svar till klienten
        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("message", "Product added to cart successfully");

        return ResponseEntity.ok(jsonResponse);
    }


}

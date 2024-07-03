package com.example.cartapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.cartapi.models.CartItem;

@RestController
@RequestMapping("api/cart")
@CrossOrigin(origins = "${frontend.origin}")
public class CartController {
    private final List<CartItem> cartItems = new ArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    //get all cart items
    @GetMapping
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    //get cart item by id
    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getCartItemById(@PathVariable int id) {
        Optional<CartItem> cartItem = cartItems.stream().filter(c -> c.getId() == id).findFirst();
        return cartItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //add new cart item
    @PostMapping
    public ResponseEntity<CartItem> addCartItem(@RequestBody CartItem cartItem) {
        cartItem.setId(counter.incrementAndGet());
        cartItems.add(cartItem);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    //add new cart item through headers
    @PostMapping("/add")
    public ResponseEntity<CartItem> addCartItemWithHeader(@RequestHeader String name, @RequestHeader int quantity, @RequestHeader BigDecimal price) {
        CartItem cartItem = new CartItem();

        cartItem.setId(counter.incrementAndGet());
        cartItem.setName(name);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(price);
        cartItems.add(cartItem);

        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    //update existing cart item
    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable int id, @RequestBody CartItem cartItemDetails) {
        Optional<CartItem> cartItemOptional = cartItems.stream().filter(c -> c.getId() == id).findFirst();
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setName(cartItemDetails.getName());
            cartItem.setQuantity(cartItemDetails.getQuantity());
            cartItem.setPrice(cartItemDetails.getPrice());
            return ResponseEntity.ok(cartItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //delete cart item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        Optional<CartItem> cartItemOptional = cartItems.stream().filter(c -> c.getId() == id).findFirst();
        if (cartItemOptional.isPresent()) {
            cartItems.remove(cartItemOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

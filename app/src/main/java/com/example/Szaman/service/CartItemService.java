package com.example.Szaman.service;

import com.example.Szaman.model.*;

import java.util.List;

public class CartItemService {

    public static void connectCartItemsWithDishesAndUsers(List<CartItem> cartItems, List<Dish> dishes, List<User> users){
        connectCartItemsWithDishes(cartItems,dishes );
        connectCartItemsWithUsers(cartItems, users);
    }

    private static void connectCartItemWithDish(CartItem cartItem, List<Dish> dishes) {
        for (Dish dish : dishes) {
            if (dish.getDishId() == cartItem.getDishId()) {
                cartItem.setDish(dish);
            }
        }
    }

    public static void connectCartItemsWithDishes(List<CartItem> cartItems, List<Dish> dishes) {
        for (CartItem item : cartItems) {
            connectCartItemWithDish(item, dishes);
        }
    }

    private static void connectCartItemWithUser(CartItem cartItem, List<User> users) {
        for (User user : users) {
            if (user.getUserId() == cartItem.getUserId()) {
                cartItem.setCartOwner(user);
            }
        }
    }

    public static void connectCartItemsWithUsers(List<CartItem> cartItems, List<User> users) {
        for (CartItem item : cartItems) {
            connectCartItemWithUser(item, users);
        }
    }
}

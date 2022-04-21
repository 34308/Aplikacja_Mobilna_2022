package com.example.Szaman.model;

import androidx.annotation.Nullable;

public class CartItem {
    private int cartItemId;
    private Integer userId;
    private Integer dishId;

    public CartItem(int cartItemId, Integer userId, Integer dishId) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.dishId = dishId;
    }

    public CartItem(Integer userId, Integer dishId) {
        this.userId = userId;
        this.dishId = dishId;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

}

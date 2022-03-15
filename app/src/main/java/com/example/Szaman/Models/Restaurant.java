package com.example.Szaman.Models;

import java.util.List;

public class Restaurant {
    private int restaurantId;
    private String name;
    private List<Dish> dishes;

    public Restaurant(int restaurantId, String name) {
        this.restaurantId = restaurantId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "RestaurantId=" + restaurantId +
                ", name='" + name + '\'' +
                ", dishes=" + dishes +
                '}';
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}

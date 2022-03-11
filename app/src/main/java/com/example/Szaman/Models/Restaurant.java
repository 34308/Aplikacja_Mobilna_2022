package com.example.Szaman.Models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Restaurant {
    private int RestaurantId;
    private String name;
    private List<Dish> dishes;

    @Override
    public String toString() {
        return "Restaurant{" +
                "RestaurantId=" + RestaurantId +
                ", name='" + name + '\'' +
                ", dishes=" + dishes +
                '}';
    }

    public int getRestaurantId() {
        return RestaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        RestaurantId = restaurantId;
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

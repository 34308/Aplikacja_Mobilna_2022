package com.example.Szaman.model;

import java.util.ArrayList;
import java.util.List;

public class RestaurantUserConnector {

    public static void fillRestaurantWithDishes(Restaurant restaurant, List<Dish> dishes) {
        List<Dish> restaurantDishes = new ArrayList<>();
        for (Dish dish : dishes) {
            if (dish.getRestaurantId() == restaurant.getRestaurantId()) {
                restaurantDishes.add(dish);
            }
        }
        restaurant.setDishes(restaurantDishes);
    }

    public static void fillRestaurantsWithDishes(List<Restaurant> restaurants, List<Dish> dishes) {
        for ( Restaurant restaurant : restaurants) {
            fillRestaurantWithDishes(restaurant, dishes);
        }
    }
}
package com.example.Szaman.Models;

public class Dish {
    private int DishId;
    private String name;
    private String description;
    private Double price;
    private int restaurantId;
    private Restaurant restaurant;
    private String imageUrl;

    @Override
    public String toString() {
        return "Dish{" +
                "DishId=" + DishId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", restaurantId=" + restaurantId +
                ", restaurant=" + restaurant +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getDishId() {
        return DishId;
    }

    public void setDishId(int dishId) {
        DishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

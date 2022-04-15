package com.example.Szaman.model;

import androidx.annotation.Nullable;

public class ShoppingCardDish {
    private Integer dishId;
    private Integer count;

    public ShoppingCardDish(Integer dishId, Integer count) {
        this.dishId = dishId;
        this.count = count;
    }

    public Integer getDishId() {
        return dishId;
    }

    public void setDishId(Integer dishId) {
        this.dishId = dishId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

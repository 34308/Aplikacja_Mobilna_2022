package com.example.Szaman.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Szaman.Models.Dish;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;

import java.util.ArrayList;
import java.util.List;

//Adapter klasy uzywany do recycler view umozliwia wlasciwe zaprezentowanie klasy.
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    //lista przechowujaca zawartosc pobrana ze strony
    public ArrayList<Dish> mdish;
    //interfejs uzywany do nasluchiwania klikniecia na recycler view
    public OnClickInterface onClickInterface;

    //konstruktor
    public RestaurantAdapter(List<Dish> dish, OnClickInterface onClickInterface) {
        mdish = (ArrayList<Dish>) dish;
        this.onClickInterface = onClickInterface;
    }
    ///konstruktor 2
    public void DataAdapter(ArrayList<Dish> data){
        mdish =data;
    }


    @NonNull
    @Override

    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //stworzenie view zawierajacego layout pojedynczego itemu w liscie
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rycecle_item_restaurant_layout,parent,false);
        RestaurantViewHolder dataViewHolder=new RestaurantViewHolder(view);
        return dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        //wyswitlenie tekstu zapisanego w klasie
        Dish currentData= mdish.get(position);

    }
    //funkcja podajaca konkretny item z listy recycler view
    public Dish getSpecificData(int position){
     return mdish.get(position);
    }
    //funkcja podajaca cala zawartosc recycle viewer
    public ArrayList<Dish> getData(){
        return mdish;
    }
    @Override
    //uzyskuje wielkosc listy
    public int getItemCount() {
        return mdish.size();
    }
    //klasa zawierajaca poszczegolne elementy interfejsu
    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{
        public TextView restaurantName;
        public TextView restaurantRating;
        public ImageView RestaurantImage;
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName =itemView.findViewById(R.id.RestaurantNameLabel);
            restaurantRating =itemView.findViewById(R.id.restaurantRating);
            RestaurantImage =itemView.findViewById(R.id.restaurantItemImage);
        }

    }
}

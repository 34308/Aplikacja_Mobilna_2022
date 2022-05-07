package com.example.Szaman.adapters;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//Adapter klasy uzywany do recycler view umozliwia wlasciwe zaprezentowanie klasy.
public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    //lista przechowujaca zawartosc pobrana ze strony
    public List<Restaurant> mRestaurant;
    //interfejs uzywany do nasluchiwania klikniecia na recycler view
    public OnClickInterface onClickInterface;

    //konstruktor
    public RestaurantAdapter(List<Restaurant> restaurants, OnClickInterface onClickInterface) {
        mRestaurant = (List<Restaurant>) restaurants;
        this.onClickInterface = onClickInterface;
    }
    ///konstruktor 2
    public void DataAdapter(ArrayList<Restaurant> data){
        mRestaurant =data;
    }
    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //stworzenie view zawierajacego layout pojedynczego itemu w liscie
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rycecle_item_restaurant_layout,parent,false);
        RestaurantViewHolder dataViewHolder=new RestaurantViewHolder(view);
        return dataViewHolder;
    }
    public void filterList(List<Restaurant> filterllist) {
        // below line is to add our filtered
        // list in our course array list.
        mRestaurant= filterllist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        //wyswitlenie tekstu zapisanego w klasie
        Restaurant currentData= mRestaurant.get(position);
        holder.restaurantName.setText(currentData.getName());

        Picasso.get().load(currentData.getImageUrl()).resize(350,100).centerCrop().into(holder.restaurantImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.setClick(position);
            }
        });

    }
    //funkcja podajaca konkretny item z listy recycler view
    public Restaurant getSpecificData(int position){
     return mRestaurant.get(position);
    }
    //funkcja podajaca cala zawartosc recycle viewer
    public List<Restaurant> getData(){
        return mRestaurant;
    }
    @Override
    //uzyskuje wielkosc listy
    public int getItemCount() {
        return mRestaurant.size();
    }
    //klasa zawierajaca poszczegolne elementy interfejsu
    public static class RestaurantViewHolder extends RecyclerView.ViewHolder{
        public TextView restaurantName;

        public ImageView restaurantImage;
        public RestaurantViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName =itemView.findViewById(R.id.rItemRestaurantNameLabel);

            restaurantImage =itemView.findViewById(R.id.restaurantItemImage);
        }

    }
}

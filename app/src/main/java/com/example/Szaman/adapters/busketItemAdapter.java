package com.example.Szaman.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.model.Dish;

import java.util.ArrayList;
import java.util.List;

//Adapter klasy uzywany do recycler view umozliwia wlasciwe zaprezentowanie klasy.
public class busketItemAdapter extends RecyclerView.Adapter<busketItemAdapter.DishViewHolder> {
    //lista przechowujaca zawartosc pobrana ze strony
    public ArrayList<Dish> mdish;
    //interfejs uzywany do nasluchiwania klikniecia na recycler view
    public OnClickInterface onClickInterface;

    //konstruktor
    public busketItemAdapter(List<Dish> dish, OnClickInterface onClickInterface) {
        mdish = (ArrayList<Dish>) dish;
        this.onClickInterface = onClickInterface;
    }
    ///konstruktor 2
    public void DataAdapter(ArrayList<Dish> data){
        mdish =data;
    }


    @NonNull
    @Override

    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //stworzenie view zawierajacego layout pojedynczego itemu w liscie
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rycecle_item_dish_layout,parent,false);
        DishViewHolder dataViewHolder=new DishViewHolder(view);
        return dataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        //wyswitlenie tekstu zapisanego w klasie
        Dish currentData= mdish.get(position);
        holder.dishName.setText(currentData.getName());
        holder.dishPriceLabel.setText(currentData.getPrice().toString());

        holder.quantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.setClick(position);
            }
        });
        //ustawienie listenera dla przycisku
        holder.addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInterface.setClick(position);
            }
        });

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
    public static class DishViewHolder extends RecyclerView.ViewHolder{
        public TextView dishName;
        public TextView dishPriceLabel;
        public Button quantityButton;
        public Button addbutton;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName =itemView.findViewById(R.id.ritemDishNameLabel);
            dishPriceLabel =itemView.findViewById(R.id.ritemPrice);
            quantityButton=itemView.findViewById(R.id.rItemDishQuantityButton);

        }

    }
}

package com.example.Szaman.Adapters;

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
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {
    //lista przechowujaca zawartosc pobrana ze strony
    public List<Dish> mdish;
    //interfejs uzywany do nasluchiwania klikniecia na recycler view
    public OnClickInterface onClickInterface;

    //konstruktor
    public DishAdapter(List<Dish> dish, OnClickInterface onClickInterface) {
        mdish = (List<Dish>) dish;
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
    public void filterList(List<Dish> filterList) {
        // below line is to add our filtered
        // list in our course array list.
        mdish= filterList;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    //funkcja podajaca konkretny item z listy recycler view
    public Dish getSpecificData(int position){
     return mdish.get(position);
    }
    //funkcja podajaca cala zawartosc recycle viewer
    public List<Dish> getData(){
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
            dishName =itemView.findViewById(R.id.busketDishNameLabel);
            dishPriceLabel =itemView.findViewById(R.id.restaurantRating);
            quantityButton=itemView.findViewById(R.id.dishQuantityButton);
            addbutton=itemView.findViewById(R.id.dishItemAddButton);
        }

    }
}

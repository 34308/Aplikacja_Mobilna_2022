package com.example.Szaman.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.model.CartItem;

import java.util.ArrayList;
import java.util.List;

//Adapter klasy uzywany do recycler view umozliwia wlasciwe zaprezentowanie klasy.
public class CasketItemAdapter extends RecyclerView.Adapter<CasketItemAdapter.CartViewHolder> {
    //lista przechowujaca zawartosc pobrana ze strony
    public ArrayList<CartItem> mitems;
    //interfejs uzywany do nasluchiwania klikniecia na recycler view
    public OnClickInterface onClickInterface;

    //konstruktor
    public CasketItemAdapter(List<CartItem> items, OnClickInterface onClickInterface) {
        mitems = (ArrayList<CartItem>) items;
        this.onClickInterface = onClickInterface;
    }
    ///konstruktor 2
    public void DataAdapter(ArrayList<CartItem> data){
        mitems =data;
    }

    @NonNull
    @Override

    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //stworzenie view zawierajacego layout pojedynczego itemu w liscie
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rycecle_item_dish_layout,parent,false);
        CartViewHolder dataViewHolder=new CartViewHolder(view);
        return dataViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        //wyswitlenie tekstu zapisanego w klasie
        CartItem currentData= mitems.get(position);
        holder.dishName.setText(currentData.getDish().getName());
        holder.dishPriceLabel.setText(currentData.getDish().getPrice().toString());
        Log.w("B",""+currentData.getCountOfDish());
        holder.quantityButton.setNumber(String.valueOf(currentData.getCountOfDish()));

        holder.quantityButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                DatabaseConnector databaseConnector=new DatabaseConnector(view.getContext());
                currentData.setCountOfDish(newValue);
                databaseConnector.updateCartItem(currentData);
                onClickInterface.setClick(position);
            }
        });

        //ustawienie listenera dla przycisku



    }
    //funkcja podajaca konkretny item z listy recycler view
    public CartItem getSpecificData(int position){
     return mitems.get(position);
    }
    //funkcja podajaca cala zawartosc recycle viewer
    public ArrayList<CartItem> getData(){
        return mitems;
    }
    @Override
    //uzyskuje wielkosc listy
    public int getItemCount() {
        return mitems.size();
    }
    //klasa zawierajaca poszczegolne elementy interfejsu
    public static class CartViewHolder extends RecyclerView.ViewHolder{
        public TextView dishName;
        public TextView dishPriceLabel;

        public ElegantNumberButton quantityButton;
        public ImageButton rBuyButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            rBuyButton=itemView.findViewById(R.id.rBuyButton);
            dishName =itemView.findViewById(R.id.ritemDishNameLabel);
            dishPriceLabel =itemView.findViewById(R.id.ritemPrice);
            quantityButton=itemView.findViewById(R.id.rItemDishQuantityButton);

        }

    }
}

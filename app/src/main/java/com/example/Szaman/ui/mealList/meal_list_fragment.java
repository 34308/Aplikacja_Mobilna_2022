package com.example.Szaman.ui.mealList;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Szaman.adapters.DishAdapter;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.MealListFragmentBinding;
import com.example.Szaman.model.CartItem;
import com.example.Szaman.model.Dish;

import java.util.ArrayList;
import java.util.List;

public class meal_list_fragment extends Fragment {

    private DishAdapter adapter;
    private MealListFragmentBinding binding;
    private OnClickInterface onClickInterface;
    private List<Dish> dataBank;

    private int ID;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ID = bundle.getInt("ID");
        }
        binding = MealListFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton searchButton =root.getRootView().findViewById(R.id.mealsSearchButton);
        EditText searchBar =root.getRootView().findViewById(R.id.mealsSearchBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(searchBar.getText().toString());
            }
        });
        onClickInterface = new OnClickInterface() {
            @Override
            public void setClick(int pos) {
                //przycisk znajdz przycisk
                //pobierz info z databank majac pozycje kliknieteho elementu
                Dish dish = dataBank.get(pos);
                //stworz bundle dzieki ktoremu bedziesz mogl wyslasc informacje z databank do drugiego fragmentu
                Bundle bundle = new Bundle();
                ArrayList<String> str=new ArrayList<>();
                str.add(dish.getName());
                str.add(String.valueOf(dish.getPrice()));
                str.add(String.valueOf(dish.getRestaurantId()));
                //dodanie listy do bundla z id key
                bundle.putStringArrayList("key", str);
                //przejdz do innego fragmentu uzywajac navcontroller

            }
        };
        setMeals(root);
        return root;
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Dish> filteredList = new ArrayList<>();

        // running a for loop to compare elements.
        for (Dish item : dataBank) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredList);
        }
    }
    public String loadPasses(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared_preferences",getContext().MODE_PRIVATE);
        String login=sharedPreferences.getString("CurrentUser", String.valueOf(R.string.default_value));
        return login;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setMeals(View root){
        DatabaseConnector databaseConnector =new DatabaseConnector(getContext());
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Dish dish= dataBank.get(viewHolder.getAdapterPosition());
                String[] passes=loadPasses().split("-");
                ElegantNumberButton e=viewHolder.itemView.findViewById(R.id.rItemDishQuantityButton);

                CartItem cartItem=new CartItem(databaseConnector.getUser(passes[0],passes[1]).getUserId(),dish.getDishId(),Integer.parseInt(e.getNumber()));
                databaseConnector.upsertCartItem(cartItem);

            }
        };
        List<Dish> d2= databaseConnector.getDishes();
        List<Dish> dishes= new ArrayList<Dish>();
        for (Dish dish:d2) {
           if(dish.getRestaurantId()==ID){
               dishes.add(dish);
               Log.w("dishes",""+ ID);
           }
        }
        RecyclerView recyclerView = root.getRootView().findViewById(R.id.mealListRecycleViewer);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        //uzycie itemtouchhleper do wykrywania
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter =  new DishAdapter(dishes,onClickInterface);
        recyclerView.setAdapter((DishAdapter) adapter);
        adapter =(DishAdapter) recyclerView.getAdapter();
        dataBank= ((DishAdapter) adapter).getData();


    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MealListFragmentViewModel mViewModel = new ViewModelProvider(this).get(MealListFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}
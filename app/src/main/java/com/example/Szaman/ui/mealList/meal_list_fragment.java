package com.example.Szaman.ui.mealList;

import androidx.lifecycle.ViewModelProvider;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Szaman.Adapters.DishAdapter;
import com.example.Szaman.Models.Dish;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;

import java.util.ArrayList;

public class meal_list_fragment extends Fragment {

    private MealListFragmentViewModel mViewModel;
    private RecyclerView.Adapter adapter;
    private OnClickInterface onClickInterface;
    private ArrayList<Dish> dataBank;
    public static meal_list_fragment newInstance() {
        return new meal_list_fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        onClickInterface = new OnClickInterface() {
            @Override
            public void setClick(int pos) {
                //przycisk znajdz przycisk
                Button button= getActivity().findViewById(R.id.dishItemAddButton);
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
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.dish,bundle);
            }


        };
        return inflater.inflate(R.layout.meal_list_fragment, container, false);
    }
    public void setMeals(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            }
        };

        //ArrayList<Dish> data=////tu wstawic pobranie informacji z listy dań
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        //uzycie itemtouchhleper do wykrywania
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //adapter = (RecyclerView.Adapter) new DishAdapter(dish,onClickInterface);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        adapter =recyclerView.getAdapter();
        dataBank= ((DishAdapter) adapter).getData();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MealListFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

}
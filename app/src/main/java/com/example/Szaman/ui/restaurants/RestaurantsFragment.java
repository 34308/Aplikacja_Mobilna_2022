package com.example.Szaman.ui.restaurants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Szaman.Adapters.DishAdapter;
import com.example.Szaman.Models.Dish;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.databinding.FragmentRestaurantsBinding;

import java.util.ArrayList;

public class RestaurantsFragment extends Fragment {

    private RestaurantsViewModel homeViewModel;
    private FragmentRestaurantsBinding binding;
    private RecyclerView.Adapter adapter;
    private OnClickInterface onClickInterface;
    private ArrayList<Dish> dataBank;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(RestaurantsViewModel.class);

        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void showRestaurants(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            }
        };

        //ArrayList<Restaurant> restaurant=////tu wstawic pobranie informacji z listy dań
        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        //uzycie itemtouchhleper do wykrywania
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //adapter = (RecyclerView.Adapter) new DishAdapter(restaurant,onClickInterface);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        adapter =recyclerView.getAdapter();
        dataBank= ((DishAdapter) adapter).getData();
    }
}
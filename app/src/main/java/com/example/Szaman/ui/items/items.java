package com.example.Szaman.ui.items;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Szaman.adapters.DishAdapter;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.model.Dish;

import java.util.List;

public class items extends Fragment {
    private RecyclerView.Adapter adapter;
    private OnClickInterface onClickInterface;
    private List<Dish> dataBank;
    private ItemsViewModel mViewModel;

    public static items newInstance() {
        return new items();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);
        // TODO: Use the ViewModel
    }

    public void showItems(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            }
        };

        //ArrayList<Item> item=////tu wstawic pobranie informacji z listy dań
        RecyclerView recyclerView = getActivity().findViewById(R.id.ItemRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        //uzycie itemtouchhleper do wykrywania
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //adapter = (RecyclerView.Adapter) new busketItemAdapter(item,onClickInterface);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        adapter =recyclerView.getAdapter();
        dataBank= ((DishAdapter) adapter).getData();
    }
}
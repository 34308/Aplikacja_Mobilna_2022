package com.example.Szaman.ui.restaurants;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Szaman.adapters.RestaurantAdapter;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.FragmentRestaurantsBinding;
import com.example.Szaman.model.Dish;
import com.example.Szaman.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsFragment extends Fragment {
    private DatabaseConnector databaseConnector;
    private RestaurantsViewModel homeViewModel;
    private FragmentRestaurantsBinding binding;
    private RestaurantAdapter adapter;
    private OnClickInterface onClickInterface;
    private List<Restaurant> dataBank;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(RestaurantsViewModel.class);
        binding = FragmentRestaurantsBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        ImageButton searchButton =root.getRootView().findViewById(R.id.restaurantSearchButton);
        EditText searchBar =root.getRootView().findViewById(R.id.restaurantsSearchBar);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter(searchBar.getText().toString());
            }
        });
        onClickInterface = new OnClickInterface() {
            @Override
            public void setClick(int pos) {

                //pobierz info z databank majac pozycje kliknieteho elementu

                Dish dish=new Dish(1,"ryba","ryba panierowana kotem",22.00,1,"potrawka22");
                Dish dish1=new Dish(1,"ryba2","ryba panierowana kotem",22.00,1,"potrawka22");
                ArrayList<Dish> data =new ArrayList<Dish>();
                data.add(dish);
                data.add(dish1);

                //stworz bundle dzieki ktoremu bedziesz mogl wyslasc informacje z databank do drugiego fragmentu
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dishes", data);

                //przejdz do innego fragmentu uzywajac navcontroller
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_restaurants_to_meal_list,bundle);
            }
        };

        showRestaurants(root);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Restaurant> filteredList = new ArrayList<>();

        // running a for loop to compare elements.
        for (Restaurant item : dataBank) {
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showRestaurants(View root){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            }
        };
        databaseConnector =new DatabaseConnector(getContext());
        List<Restaurant> restaurant= databaseConnector.getRestaurants();
        RecyclerView recyclerView = root.getRootView().findViewById(R.id.restaurantsRecycleViewer);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        //uzycie itemtouchhleper do wykrywania
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter =  new RestaurantAdapter(restaurant,onClickInterface);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        adapter =(RestaurantAdapter) recyclerView.getAdapter();
        dataBank= ((RestaurantAdapter) adapter).getData();
    }
}
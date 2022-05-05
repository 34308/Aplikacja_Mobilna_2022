package com.example.Szaman.ui.restaurants;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Szaman.MainActivity;
import com.example.Szaman.adapters.RestaurantAdapter;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.FragmentRestaurantsBinding;
import com.example.Szaman.model.Dish;
import com.example.Szaman.model.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

                Restaurant r=dataBank.get(pos);
                Integer s=r.getRestaurantId();

                //stworz bundle dzieki ktoremu bedziesz mogl wyslasc informacje z databank do drugiego fragmentu
                Fragment fragment = new Fragment();
                Bundle bundle = new Bundle();
                bundle.putInt("ID", s);
                fragment.setArguments(bundle);
                //przejdz do innego fragmentu uzywajac navcontroller
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.meal_list,bundle);
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
        databaseConnector =new DatabaseConnector(getContext());
        List<Restaurant> restaurants = databaseConnector.getRestaurants();

        RecyclerView recyclerView = root.getRootView().findViewById(R.id.restaurantsRecycleViewer);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        //uzycie itemtouchhleper do wykrywania

        adapter =  new RestaurantAdapter(restaurants,onClickInterface);
        recyclerView.setAdapter((RecyclerView.Adapter) adapter);
        adapter =(RestaurantAdapter) recyclerView.getAdapter();
        dataBank= ((RestaurantAdapter) adapter).getData();
    }

}
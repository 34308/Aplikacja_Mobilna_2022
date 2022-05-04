package com.example.Szaman.ui.dish;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.DishFragmentBinding;
import com.example.Szaman.model.CartItem;
import com.example.Szaman.model.Dish;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class dish extends Fragment {
    private DishFragmentBinding binding;
    private DishViewModel mViewModel;
    DatabaseConnector databaseConnector;
    Dish dish;
    public static dish newInstance() {
        return new dish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DishFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        databaseConnector=new DatabaseConnector(getContext());
        Button gotobasket=root.findViewById(R.id.dishGoToBasket);
        gotobasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController= Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_dish_to_nav_summary);
            }
        });

        ArrayList<String> dishB;
        dishB = getArguments().getStringArrayList("Dish");
        List<Dish> d=databaseConnector.getDishes();
        for (Dish di:d) {
            if(di.getDishId()==Integer.parseInt(dishB.get(1))){
                dish=di;
                break;
            }
        }
        System.out.println("filename: "+ dishB);

        ImageView dishImage= root.findViewById(R.id.dishImageView);
        Picasso.get().load(dishB.get(4)).into(dishImage);
        TextView name= root.findViewById(R.id.dishNameLabel);
        TextView desc= root.findViewById(R.id.dishDiscriptionLabel);
        desc.setText(dishB.get(5));
        name.setText(dishB.get(0));
        Button addBasket =root.findViewById(R.id.dishAddButton);
        addBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBusket(root);
            }
        });
        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addToBusket(View root){
        EditText editText= root.findViewById(R.id.dishQuantityET);
        int q=Integer.parseInt(String.valueOf(editText.getText()));
        if(q!=0){
            String[] pases= loadPasses().split("-");
            CartItem cartItem=new CartItem(databaseConnector.getUser(pases[0],pases[1]).getUserId(),dish.getDishId(),q);
            databaseConnector.upsertCartItem(cartItem);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DishViewModel.class);
        // TODO: Use the ViewModel
    }
    public String loadPasses(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared_preferences",getContext().MODE_PRIVATE);
        String login=sharedPreferences.getString("CurrentUser", String.valueOf(R.string.default_value));
        return login;
    }
}
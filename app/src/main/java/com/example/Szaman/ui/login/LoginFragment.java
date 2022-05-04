package com.example.Szaman.ui.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.Szaman.MainActivity;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.FragmentLoginBinding;
import com.example.Szaman.model.User;
import com.google.android.material.navigation.NavigationView;

import java.util.List;


public class LoginFragment extends Fragment {
    private DatabaseConnector databaseConnector;
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        ((MainActivity) getActivity()).lockMneu();
        ((MainActivity) getActivity()).hideMneu();
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        Button refreshButton=root.getRootView().findViewById(R.id.refresherButton);
        Button loginButton= root.getRootView().findViewById(R.id.loginButton);
        Button registerButton=root.getRootView().findViewById(R.id.loginRegisterButton);
        TextView forgotten=root.getRootView().findViewById(R.id.loginForgottenPassword);
        forgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restorePassword(root);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Login(root);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(root);
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View restoreWindow=root.getRootView().findViewById(R.id.loginRefresherWindow);
                restoreWindow.setVisibility(View.GONE);
                //sendEmail();
            }
        });
        return root;
    }

    private void restorePassword(View root) {
        View restoreWindow=root.getRootView().findViewById(R.id.loginRefresherWindow);
        restoreWindow.setVisibility(View.VISIBLE);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Login(View root){
        TextView login=root.getRootView().findViewById(R.id.loginWindow);
        TextView password=root.getRootView().findViewById(R.id.loginPasswordWindow);
        String loginText=  login.getText().toString();
        String passwordText= password.getText().toString();

        if(checkLogin(loginText,passwordText)) {
            savePasses(loginText+"-"+passwordText);
            goToRestaurants();
        }
    }
    private void goToRestaurants() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_login_to_nav_restaurants);
    }
    public void register(View root){
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_login_to_nav_register);
    }
    @Override
    public void onDestroyView() {
        ((MainActivity) getActivity()).unlockMneu();
        ((MainActivity) getActivity()).showMneu();
        super.onDestroyView();
        binding = null;
    }

    public void savePasses(String data){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("CurrentUser",data);
        editor.apply();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkLogin(String login,String password) {
        databaseConnector=new DatabaseConnector(getContext());
        List<User> users=databaseConnector.getUsers();
        for (User u:users) {
            if(u.getLogin().equals(login) && u.getPassword().equals(password)) {
                return true;
            }
        }

        return false;
    }
}
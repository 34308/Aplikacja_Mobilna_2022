package com.example.Szaman.ui.login;

import static com.example.Szaman.CurrentUserService.savePasses;
import static com.example.Szaman.validators.Validators.postCodeValidator;
import static com.example.Szaman.service.MailService.sendEmail;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.Szaman.MainActivity;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.FragmentLoginBinding;
import com.example.Szaman.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;


public class LoginFragment extends Fragment {
    private DatabaseConnector databaseConnector;
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button refreshButton=root.getRootView().findViewById(R.id.refresherButton);
        Button loginButton= root.getRootView().findViewById(R.id.loginButton);
        Button registerButton=root.getRootView().findViewById(R.id.loginRegisterButton);
        TextView forgotten=root.getRootView().findViewById(R.id.loginForgottenPassword);
        EditText refresherLogin=root.getRootView().findViewById(R.id.refresherLogin);
        EditText refresherEmail =root.getRootView().findViewById(R.id.refresherEmail);
        forgotten.setOnClickListener(v -> restorePassword(root));
        loginButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Login(root);
            }
        });
        registerButton.setOnClickListener(v -> register(root));
        Button closeButton=root.getRootView().findViewById(R.id.loginCloseButton);
        closeButton.setOnClickListener(v -> {
            View restoreWindow=root.getRootView().findViewById(R.id.loginRefresherWindow);
            restoreWindow.setVisibility(View.GONE);
        });
        final boolean[] sendMailSuccess = {false};
        refreshButton.setOnClickListener(v -> {
            if (databaseConnector == null) databaseConnector=new DatabaseConnector(getContext());
            View restoreWindow=root.getRootView().findViewById(R.id.loginRefresherWindow);
            for (User user: databaseConnector.getUsers()) {
                if(user.getLogin().equals(refresherLogin.getText().toString()) && user.getEmail().equals(refresherEmail.getText().toString())) {
                    try {
                        sendEmail(refresherEmail.getText().toString(),user.getLogin()+" password Recovery","Your password is :"+user.getPassword());
                         sendMailSuccess[0] =true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(!sendMailSuccess[0]) Snackbar.make(root,R.string.WrongEmail, BaseTransientBottomBar.LENGTH_SHORT).show() ;
            restoreWindow.setVisibility(View.GONE);
            //sendEmail();
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).lockMneu();
        ((MainActivity) requireActivity()).hideMneu();
    }

    private void restorePassword(View root) {
        View restoreWindow=root.getRootView().findViewById(R.id.loginRefresherWindow);
        restoreWindow.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Login(View root){
       Log.w("poprawny czy nie",""+ postCodeValidator("33-150"));
        TextView login=root.getRootView().findViewById(R.id.loginWindow);
        TextView password=root.getRootView().findViewById(R.id.loginPasswordWindow);
        String loginText=  login.getText().toString();
        String passwordText= password.getText().toString();

        if(checkLogin(loginText,passwordText)) {
            savePasses(loginText+"-"+passwordText,getActivity());
            login.setText("");
            password.setText("");
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

        super.onDestroyView();
        binding = null;
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
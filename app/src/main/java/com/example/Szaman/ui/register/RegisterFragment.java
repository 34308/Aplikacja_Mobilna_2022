package com.example.Szaman.ui.register;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.FragmentRegisterBinding;
import com.example.Szaman.model.User;

import java.util.List;

public class RegisterFragment extends Fragment {
    private DatabaseConnector databaseConnector;
    private RegisterViewModel registerViewModel;
    private FragmentRegisterBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button registerButton= root.findViewById(R.id.loginRegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                addNewUser(root);
            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNewUser(View root) {
        databaseConnector= new DatabaseConnector(getContext());
        EditText email= root.getRootView().findViewById(R.id.emailWindow);
        EditText login= root.getRootView().findViewById(R.id.personalLoginWindow);
        EditText name= root.getRootView().findViewById(R.id.personalNameWindow);
        EditText surname= root.getRootView().findViewById(R.id.personalSurnameWindow);
        EditText password= root.getRootView().findViewById(R.id.personalPasswordWindow);
        EditText passwordAgain= root.getRootView().findViewById(R.id.registerPasswordAgainWindow);
        EditText DCNumber= root.getRootView().findViewById(R.id.personalDebitCardNumberWindow);
        EditText DCCVV= root.getRootView().findViewById(R.id.personalDebitCardCVVWindow);
        EditText DCExpires= root.getRootView().findViewById(R.id.personalDebitCardExpiresWindow);
        EditText city= root.getRootView().findViewById(R.id.personalCityWindow);
        EditText houseNumber= root.getRootView().findViewById(R.id.personalHauseNumberWindow);
        EditText postalCode= root.getRootView().findViewById(R.id.personalPostCodeWindow);

        String adress=city.getText().toString()+","+houseNumber.getText().toString()+","+postalCode.getText().toString();

        if(!login.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !name.getText().toString().isEmpty() &&
                !surname.getText().toString().isEmpty() && !passwordAgain.getText().toString().isEmpty() && !DCCVV.getText().toString().isEmpty()
                && !DCNumber.getText().toString().isEmpty() && !DCExpires.getText().toString().isEmpty()&&
        !city.getText().toString().isEmpty() && !houseNumber.getText().toString().isEmpty() && !postalCode.getText().toString().isEmpty() ){
            if(password.getText().toString().equals(passwordAgain.getText().toString())){
                User user=new User(login.getText().toString(),password.getText().toString(),name.getText().toString(),surname.getText().toString(),adress,DCNumber.getText().toString(),DCExpires.getText().toString(),DCCVV.getText().toString(),email.getText().toString());
                if(checkLogin(user)){
                    databaseConnector.addUser(user);
                }
            }
        }
        gotologin();
    }

    private void gotologin() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_register_to_nav_login);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkLogin(User user) {
        String login= user.getLogin();
        databaseConnector=new DatabaseConnector(getContext());
        List<User> users=databaseConnector.getUsers();
        for (User u:users) {
            if(u.getLogin()==login){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
package com.example.Szaman.ui.register;

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
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.FragmentRegisterBinding;
import com.example.Szaman.model.User;
import com.example.Szaman.model.UserComparator;

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
        Button registerButton= root.findViewById(R.id.registerButton);
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
        EditText login= root.getRootView().findViewById(R.id.registerLoginWindow);
        EditText name= root.getRootView().findViewById(R.id.registerNameWindow);
        EditText surname= root.getRootView().findViewById(R.id.registerSurnameWindow);
        EditText password= root.getRootView().findViewById(R.id.registerPasswordWindow);
        EditText passwordAgain= root.getRootView().findViewById(R.id.registerPasswordAgainWindow);
        EditText DCNumber= root.getRootView().findViewById(R.id.registerDebitCardNumberWindow);
        EditText DCCVV= root.getRootView().findViewById(R.id.registerDebitCardCVVWindow);
        EditText DCExpires= root.getRootView().findViewById(R.id.registerDebitCardExpiresWindow);
        EditText adress= root.getRootView().findViewById(R.id.registerAdressWindow);
        EditText city= root.getRootView().findViewById(R.id.registerCityWindow);
        EditText houseNumber= root.getRootView().findViewById(R.id.registerHauseNumberWindow);
        EditText postalCode= root.getRootView().findViewById(R.id.registerPostCodeWindow);
        if(!login.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !name.getText().toString().isEmpty() &&
                !surname.getText().toString().isEmpty() && !passwordAgain.getText().toString().isEmpty() && !DCCVV.getText().toString().isEmpty()
                && !DCNumber.getText().toString().isEmpty() && !DCExpires.getText().toString().isEmpty()&& !adress.getText().toString().isEmpty() &&
        !city.getText().toString().isEmpty() && !houseNumber.getText().toString().isEmpty() && !postalCode.getText().toString().isEmpty() ){
            if(password.getText().toString().equals(passwordAgain.getText().toString())){
                User user=new User(login.getText().toString(),password.getText().toString(),name.getText().toString(),surname.getText().toString(),adress.getText().toString(),DCNumber.getText().toString(),DCExpires.getText().toString(),DCCVV.getText().toString());
                if(checkLogin(user)){
                    databaseConnector.addUser(user);
                }
            }
        }
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
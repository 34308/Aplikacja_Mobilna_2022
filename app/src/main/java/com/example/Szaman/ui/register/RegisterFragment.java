package com.example.Szaman.ui.register;

import static com.example.Szaman.Validators.Validators.cvvValidator;
import static com.example.Szaman.Validators.Validators.debitCardValidator;
import static com.example.Szaman.Validators.Validators.emailValidator;
import static com.example.Szaman.Validators.Validators.expireDateValidator;
import static com.example.Szaman.Validators.Validators.passwordValidator;
import static com.example.Szaman.Validators.Validators.postCodeValidator;

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

import com.example.Szaman.MainActivity;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.FragmentRegisterBinding;
import com.example.Szaman.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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
        ((MainActivity) getActivity()).lockMneu();
        ((MainActivity) getActivity()).hideMneu();
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
        EditText street= root.getRootView().findViewById(R.id.personalStreetWindow);
        String adress=city.getText().toString()+","+street.getText().toString()+","+houseNumber.getText().toString()+","+postalCode.getText().toString();

        if (!emailValidator(email.getText().toString())) {Snackbar.make(root,R.string.WrongEmail, BaseTransientBottomBar.LENGTH_SHORT).show() ;return;}
        if (!passwordValidator(password.getText().toString())){ Snackbar.make(root,R.string.WrongPassword, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
        if (!postCodeValidator(postalCode.getText().toString())){ Snackbar.make(root,R.string.WrongPostalCode, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
        if (!debitCardValidator(DCNumber.getText().toString())){ Snackbar.make(root,R.string.WrongDebitCard, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
        if(! expireDateValidator(DCExpires.getText().toString())){ Snackbar.make(root,R.string.WrongExpireCard, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
        if(! cvvValidator(DCCVV.getText().toString())){ Snackbar.make(root,R.string.WrongCVV, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
        if(!password.getText().toString().equals(passwordAgain.getText().toString())){  Snackbar.make(root,R.string.PasswordDontMatch, BaseTransientBottomBar.LENGTH_SHORT).show();return;}

        if(!login.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !name.getText().toString().isEmpty() &&
                !surname.getText().toString().isEmpty() && !passwordAgain.getText().toString().isEmpty() && !DCCVV.getText().toString().isEmpty()
                && !DCNumber.getText().toString().isEmpty() && !DCExpires.getText().toString().isEmpty()&&
        !city.getText().toString().isEmpty() && !houseNumber.getText().toString().isEmpty() && !postalCode.getText().toString().isEmpty() ){
            if(password.getText().toString().equals(passwordAgain.getText().toString())){
                User user=new User(login.getText().toString(),password.getText().toString(),name.getText().toString(),surname.getText().toString(),adress,DCNumber.getText().toString(),DCExpires.getText().toString(),DCCVV.getText().toString(),email.getText().toString());
                if(checkLogin(user)){
                    databaseConnector= new DatabaseConnector(getContext());
                    databaseConnector.addUser(user);
                    goTologin();
                }
                else{
                    Snackbar.make(root,R.string.WrongLogin, BaseTransientBottomBar.LENGTH_SHORT).show() ;

                }
            }
        }else{
            Snackbar.make(root,R.string.EmptyWindow, BaseTransientBottomBar.LENGTH_SHORT).show() ;
        }

    }

    private void goTologin() {
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
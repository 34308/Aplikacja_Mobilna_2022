package com.example.Szaman.ui.personalDataSettings;

import static com.example.Szaman.CurrentUserService.loadPasses;
import static com.example.Szaman.CurrentUserService.savePasses;
import static com.example.Szaman.validators.Validators.addressValidator;
import static com.example.Szaman.validators.Validators.cvvValidator;
import static com.example.Szaman.validators.Validators.debitCardValidator;
import static com.example.Szaman.validators.Validators.emailValidator;
import static com.example.Szaman.validators.Validators.expireDateValidator;
import static com.example.Szaman.validators.Validators.passwordValidator;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.PersonalDataSettingFragmentBinding;
import com.example.Szaman.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class PersonalDataSetting extends Fragment {
    DatabaseConnector databaseConnector;
    private PersonalDataSettingViewModel mViewModel;
    private PersonalDataSettingFragmentBinding binding;
    User currentUser;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = PersonalDataSettingFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        EditText name= root.findViewById(R.id.personalNameWindow);
        EditText surname= root.findViewById(R.id.personalSurnameWindow);
        EditText login= root.findViewById(R.id.personalLoginWindow);
        EditText email= root.findViewById(R.id. personalEmailWindow);
        EditText password= root.findViewById(R.id.personalPasswordWindow);
        EditText debitCard= root.findViewById(R.id.personalDebitCardNumberWindow);
        EditText cvv= root.findViewById(R.id.personalDebitCardCVVWindow);
        EditText exDebitCard= root.findViewById(R.id.personalDebitCardExpiresWindow);
        EditText address= root.findViewById(R.id.personalAdressWindow);
        Button save= root.findViewById(R.id.personalDataSavingbutton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailValidator(email.getText().toString())) {
                    Snackbar.make(root,R.string.WrongEmail, BaseTransientBottomBar.LENGTH_SHORT).show() ;return;}
                if (!passwordValidator(password.getText().toString())){ Snackbar.make(root,R.string.WrongPassword, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
                if (!debitCardValidator(debitCard.getText().toString())){ Snackbar.make(root,R.string.WrongDebitCard, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
                if(! expireDateValidator(exDebitCard.getText().toString())){ Snackbar.make(root,R.string.WrongExpireCard, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
                if(! cvvValidator(cvv.getText().toString())){ Snackbar.make(root,R.string.WrongCVV, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
                if(!addressValidator(address.getText().toString())){Snackbar.make(root,R.string.WrongAdress, BaseTransientBottomBar.LENGTH_SHORT).show();return;}
                databaseConnector=new DatabaseConnector(getContext());
                User upUser=new User(currentUser.getUserId(),login.getText().toString(),password.getText().toString(),name.getText().toString(),surname.getText().toString(),address.getText().toString(),debitCard.getText().toString(),exDebitCard.getText().toString(),cvv.getText().toString(),email.getText().toString());
                Log.w("updateUser", ""+databaseConnector.updateUser(upUser));
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_personalData_to_nav_settings);
                savePasses(upUser.getLogin()+"-"+upUser.getPassword(),getActivity());
            }
        });
        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        setUser();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUser() {
        databaseConnector=new DatabaseConnector(getContext());
        String[] passes= loadPasses(getActivity(),getContext()).split("-");
        currentUser=databaseConnector.getUser(passes[0]);

        EditText name= getView().findViewById(R.id.personalNameWindow);
        EditText surname= getView().findViewById(R.id.personalSurnameWindow);
        EditText login= getView().findViewById(R.id.personalLoginWindow);
        EditText email= getView().findViewById(R.id. personalEmailWindow);
        EditText password= getView().findViewById(R.id.personalPasswordWindow);
        EditText debitCard= getView().findViewById(R.id.personalDebitCardNumberWindow);
        EditText cvv= getView().findViewById(R.id.personalDebitCardCVVWindow);
        EditText exDebitCard= getView().findViewById(R.id.personalDebitCardExpiresWindow);
        EditText address= getView().findViewById(R.id.personalAdressWindow);


        name.setText(currentUser.getName());
        surname.setText(currentUser.getSurname());
        login.setText(currentUser.getLogin());
        email.setText(currentUser.getEmail());
        password.setText(currentUser.getPassword());
        debitCard.setText(currentUser.getDebitCardNumber());
        cvv.setText(currentUser.getCvv());
        exDebitCard.setText(currentUser.getExpireDate());
        address.setText(currentUser.getAddress());

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonalDataSettingViewModel.class);
        // TODO: Use the ViewModel
    }

}
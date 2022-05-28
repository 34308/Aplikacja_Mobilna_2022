package com.example.Szaman.ui.personalDataSettings;

import static com.example.Szaman.CurrentUserService.loadPasses;
import static com.example.Szaman.CurrentUserService.savePasses;

import androidx.annotation.RequiresApi;


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

import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.PersonalDataSettingFragmentBinding;
import com.example.Szaman.model.User;

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

        save.setOnClickListener(v -> {
            databaseConnector=new DatabaseConnector(getContext());
            User upUser=new User(currentUser.getUserId(),login.getText().toString(),password.getText().toString(),name.getText().toString(),surname.getText().toString(),address.getText().toString(),debitCard.getText().toString(),exDebitCard.getText().toString(),cvv.getText().toString(),email.getText().toString());
            databaseConnector.updateUser(upUser);
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_personalData_to_nav_settings);
            savePasses(upUser.getLogin()+"-"+upUser.getPassword(),getActivity());
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

}
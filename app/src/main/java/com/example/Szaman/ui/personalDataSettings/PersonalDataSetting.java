package com.example.Szaman.ui.personalDataSettings;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.PersonalDataSettingFragmentBinding;
import com.example.Szaman.databinding.SettingsFragmentBinding;
import com.example.Szaman.model.User;

public class PersonalDataSetting extends Fragment {

    private PersonalDataSettingViewModel mViewModel;
    private PersonalDataSettingFragmentBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = PersonalDataSettingFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button save= root.findViewById(R.id.personalDataSavingbutton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_personalData_to_nav_settings);
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
        DatabaseConnector databaseConnector=new DatabaseConnector(getContext());
        String[] passes= loadPasses().split("-");
        User user=databaseConnector.getUser(passes[0],passes[1]);
        Log.w("USER",user.getEmail());
        EditText name= getView().findViewById(R.id.personalNameWindow);
        EditText surname= getView().findViewById(R.id.personalSurnameWindow);
        EditText login= getView().findViewById(R.id.personalLoginWindow);
        EditText email= getView().findViewById(R.id. personalEmailWindow);
        EditText password= getView().findViewById(R.id.personalPasswordWindow);
        EditText debitCard= getView().findViewById(R.id.personalDebitCardNumberWindow);
        EditText cvv= getView().findViewById(R.id.personalDebitCardCVVWindow);
        EditText exDebitCard= getView().findViewById(R.id.personalDebitCardExpiresWindow);
        EditText address= getView().findViewById(R.id.personalAdressWindow);

        name.setText(user.getName());
        surname.setText(user.getSurname());
        login.setText(user.getLogin());
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        debitCard.setText(user.getDebitCardNumber());
        cvv.setText(user.getCvv());
        exDebitCard.setText(user.getExpireDate());
        address.setText(user.getAddress());

    }
    public String loadPasses(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared_preferences",getContext().MODE_PRIVATE);
        String login=sharedPreferences.getString("CurrentUser", String.valueOf(R.string.default_value));
        return login;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PersonalDataSettingViewModel.class);
        // TODO: Use the ViewModel
    }

}
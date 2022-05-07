package com.example.Szaman.ui.settings;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.SettingsFragmentBinding;
import com.example.Szaman.databinding.SummaryFragmentBinding;
import com.example.Szaman.model.User;

public class settings extends Fragment {

    private SettingsViewModel mViewModel;
    private SettingsFragmentBinding binding;
    public static settings newInstance() {
        return new settings();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SettingsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button delete=root.findViewById(R.id.settingsDeleteAcount);
        Button logOut=root.findViewById(R.id.settingsLogOutButton);

        View popupView = inflater.inflate(R.layout.popup_delete_layout, null);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areYouSure( getView(),popupView);

            }
        });
        return root;
    }


    public String loadPasses(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared_preferences",getContext().MODE_PRIVATE);
        String login=sharedPreferences.getString("CurrentUser", String.valueOf(R.string.default_value));
        return login;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteAccount(){
        logOut();
        DatabaseConnector databaseConnector=new DatabaseConnector(getContext());
        String[] passes=loadPasses().split("-");
        User currentUser= databaseConnector.getUser(passes[0],passes[1]);
        databaseConnector.deleteUser(currentUser);

    }

    private void logOut() {
        NavController navController= Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_settings_to_nav_login);
    }

    public void areYouSure(View view, View popupView){
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int  height= LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        Button delete= popupView.findViewById(R.id.popupDeleteAccount);
        EditText password= popupView.findViewById(R.id.popupPassword);
        Button cancel= popupView.findViewById(R.id.popupCancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String passes=loadPasses().split("-")[1];
                Log.w("USER",passes);
                if(password.getText().toString().equals(passes)){
                    popupWindow.dismiss();
                    deleteAccount();
                }
            }
        });

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        Button button= (Button) getView().findViewById(R.id.settingsDataEditing);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController= Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.action_nav_settings_to_personalData);
            }
        });

    }

}
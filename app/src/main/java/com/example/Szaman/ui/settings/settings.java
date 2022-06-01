package com.example.Szaman.ui.settings;

import static com.example.Szaman.CurrentUserService.loadPasses;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.widget.ToggleButton;

import com.example.Szaman.MainActivity;
import com.example.Szaman.R;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.SettingsFragmentBinding;
import com.example.Szaman.databinding.SummaryFragmentBinding;
import com.example.Szaman.model.User;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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
        ToggleButton changeTheme=root.findViewById(R.id.settingsDayNightButton);

        changeTheme.setChecked(!loadStyle().equals("1") && !loadStyle().equals(String.valueOf(R.string.style_value)));

        Button delete=root.findViewById(R.id.settingsDeleteAcount);
        Button logOut=root.findViewById(R.id.settingsLogOutButton);

        changeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeTheme.isChecked()){
                    saveStyle("0",getActivity());
                }
                else{
                    saveStyle("1",getActivity());
                }
                Snackbar.make(root,R.string.RestartStyle, BaseTransientBottomBar.LENGTH_SHORT).show() ;
            }
        });
        View popupView = inflater.inflate(R.layout.popup_delete_layout, null);
        logOut.setOnClickListener(v -> logOut());
        delete.setOnClickListener(v -> areYouSure( getView(),popupView));
        return root;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void deleteAccount(){
        logOut();
        DatabaseConnector databaseConnector=new DatabaseConnector(getContext());
        String[] passes=loadPasses(getActivity(),getContext()).split("-");
        User currentUser= databaseConnector.getUser(passes[0]);
        databaseConnector.deleteUser(currentUser);

    }

    private void logOut() {
        NavController navController= Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_nav_settings_to_nav_login);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void areYouSure(View view, View popupView){
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int  height= LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        Button delete= popupView.findViewById(R.id.popupDeleteAccount);
        EditText password= popupView.findViewById(R.id.popupPassword);
        Button cancel= popupView.findViewById(R.id.popupCancelButton);
        cancel.setOnClickListener(v -> popupWindow.dismiss());
        delete.setOnClickListener(v -> {
            String passes=loadPasses(getActivity(),getContext()).split("-")[1];
            Log.w("USER",passes);
            if(password.getText().toString().equals(passes)){
                popupWindow.dismiss();
                deleteAccount();
            }
        });

    }
    public static void saveStyle(String data, Activity activity){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putString("CurrentStyle",data);
        editor.apply();
    }
    public String loadStyle(){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String style=sharedPreferences.getString("CurrentStyle", String.valueOf(R.string.style_value));
        return style;
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
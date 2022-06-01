package com.example.Szaman.validators;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Validators {
    public static Boolean addressValidator(String adress){
        Pattern adressregex = Pattern.compile("^[^\\[\\]]+,[^\\[\\]]+,[^\\[\\]]+,[^\\[\\]]+$");
        String[] s=adress.split(",");
        return (!TextUtils.isEmpty(adress) && adressregex.matcher(adress).matches() && postCodeValidator( s[3]));
}
    public static Boolean emailValidator(String email){
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    public static Boolean postCodeValidator(String postcode){
       Pattern postalCoderegex= Pattern.compile("^(?=.*[0-9])(?=.*[-]).{6}$");
        return (!TextUtils.isEmpty(postcode) && postalCoderegex.matcher(postcode).matches());
    }
    public static Boolean expireDateValidator(String exDate){
        Pattern expireDateregex = Pattern.compile("^(?=.*[0-9])(?=.*[/]).{5}$");
        return (!TextUtils.isEmpty(exDate) && expireDateregex.matcher(exDate).matches());
    }
    public static Boolean debitCardValidator(String debitCardNumber){
        if(debitCardNumber.isEmpty())return false;
        List<String> listOfPattern=new ArrayList<>();
        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);

        for(String p:listOfPattern){
            if(debitCardNumber.matches(p)){
                Log.d("DEBUG", "afterTextChanged : discover");
                return true;
            }
        }
        return false;
    }
    @NonNull
    public static Boolean cvvValidator(String cvv){
        return cvv.length() == 3;
    }
    public static Boolean passwordValidator(String password){
        Pattern PASSWORD_PATTERN=Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@#$%^&*()_+~`=]).{8,}$");
        return  PASSWORD_PATTERN.matcher(password).matches();
    }


}

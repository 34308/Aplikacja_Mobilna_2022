package com.example.Szaman.ui.summary;

import static com.example.Szaman.CurrentUserService.loadPasses;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.Szaman.OnClickInterface;
import com.example.Szaman.R;
import com.example.Szaman.receiptWriter.PDFWriter;
import com.example.Szaman.adapters.CasketItemAdapter;
import com.example.Szaman.dataBaseConnection.DatabaseConnector;
import com.example.Szaman.databinding.SummaryFragmentBinding;
import com.example.Szaman.model.CartItem;
import com.example.Szaman.model.User;
import com.example.Szaman.service.CartItemService;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Summary extends Fragment {
    private CasketItemAdapter adapter;
    private SummaryFragmentBinding binding;
    private SummaryViewModel mViewModel;
    private int ID;
    private DatabaseConnector databaseConnector ;
    User curremtUser;
    List<CartItem> cartItems;
    private OnClickInterface onClickInterface;
    private ArrayList<CartItem> dataBank;
    public boolean delivery=true;
    public double deliveryFee=9.99;
    public static Summary newInstance() {
        return new Summary();
    }
    public Double sum=0.0;
    private String notes="";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        databaseConnector=new DatabaseConnector(getContext());
        binding = SummaryFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View popupView = inflater.inflate(R.layout.summary_note_layout, null);
        ImageButton rBuyButton= root.findViewById(R.id.rBuyButton);
        rBuyButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                Snackbar.make(root,"Thank You for Purchase\nYour receipt is located in your download's folder", BaseTransientBottomBar.LENGTH_LONG).show() ;
                PDFWriter pdf=new PDFWriter(getContext());
                try {
                    pdf.generatePDF(curremtUser,dataBank,notes,delivery);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //czysczenie koszka
                for (CartItem item:cartItems) {
                    databaseConnector.deleteCartItem(item);
                }
                cartItems.clear();
                setItems(root);
            }
        });
        CheckBox checkBox= root.findViewById(R.id.rItemcheckBox);
        ElegantNumberButton elegantNumberButton= root.findViewById(R.id.rItemDishQuantityButton);
        onClickInterface = pos -> priceRecount(root);
        checkBox.setOnClickListener(v -> {
            if(checkBox.isChecked()){
                delivery=true;
                priceRecount(root);
            }
            else{
                delivery=false;
                priceRecount(root);
            }
        });
        cartItems=getItems();
        if(cartItems.isEmpty()) rBuyButton.setEnabled(false);
        ImageButton noteButton= root.findViewById(R.id.noteButton);
        noteButton.setOnClickListener(v -> {
            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int  height= LinearLayout.LayoutParams.WRAP_CONTENT;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);

            Button add= popupView.findViewById(R.id.summaryPopUpAddNote);
            EditText note=popupView.findViewById(R.id.summaryNoteWindow);

            add.setOnClickListener(v1->{
                notes=note.getText().toString();
                popupWindow.dismiss();
            });

        });

        setItems(root);
        priceRecount(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SummaryViewModel.class);

        Button button= (Button) getView().findViewById(R.id.summaryGoBackButton);
        button.setOnClickListener(v -> {
            NavController navController= Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.action_nav_summary_to_nav_restaurants);
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)

    public List<CartItem> getItems(){
        String[] passes= loadPasses(getActivity(),getContext()).split("-");
        curremtUser=databaseConnector.getUser(passes[0]);
        List<CartItem> cartItems= databaseConnector.getCartItems(curremtUser.getUserId());
        CartItemService.connectCartItemsWithDishes(cartItems,databaseConnector.getDishes());

        return cartItems;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)

    public void setItems(View root){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                DatabaseConnector databaseConnector=new DatabaseConnector(getContext());
                CartItem cartItem =dataBank.get(viewHolder.getAdapterPosition());
                databaseConnector.deleteCartItem(cartItem);
                cartItems.remove(cartItem);
                setItems(root);
            }

        };
        DatabaseConnector databaseConnector =new DatabaseConnector(getContext());

        RecyclerView recyclerView = root.getRootView().findViewById(R.id.summaryRecyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        //uzycie itemtouchhleper do wykrywania
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        adapter =  new CasketItemAdapter(cartItems,onClickInterface);
        recyclerView.setAdapter((CasketItemAdapter) adapter);
        adapter =(CasketItemAdapter) recyclerView.getAdapter();
        assert adapter != null;
        dataBank= ((CasketItemAdapter) adapter).getData();
        priceRecount(root);
    }
    @SuppressLint("SetTextI18n")
    public void priceRecount(View root){
        sum=0.0;
        TextView price = root.findViewById(R.id.rItemPrice);
        for (CartItem item:cartItems) {
            sum+=item.getDish().getPrice() * item.getCountOfDish();
        }
        if(delivery){
            sum+=deliveryFee;
        }
        price.setText(sum.toString()+" zl");
    }
}
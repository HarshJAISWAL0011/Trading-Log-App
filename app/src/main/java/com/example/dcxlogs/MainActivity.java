package com.example.dcxlogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dcxlogs.Fragments.DateFragment;
import com.example.dcxlogs.Fragments.LogsFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class MainActivity extends AppCompatActivity  {
    
    TextView dateView;
    EditText searchView;
    public static final String CLICK_TYPE ="click_type";
    public static final String DELETE_TYPE ="delete_type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateView=findViewById ( R.id.logs );
        searchView=findViewById ( R.id.search );

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DateFragment ())
                .commit();


        SharedViewModel sharedViewModel = new ViewModelProvider (this).get(SharedViewModel.class);

        // Observe changes in the ViewModel's LiveData
        sharedViewModel.getTextViewText().observe(this, newText -> {
            dateView.setText(newText);
        });
        searchView.addTextChangedListener ( new TextWatcher ( ) {
            @Override
            public void beforeTextChanged (CharSequence charSequence , int i , int i1 , int i2) {

            }

            @Override
            public void onTextChanged (CharSequence charSequence , int i , int i1 , int i2) {
                new ViewModelProvider(MainActivity.this).get( SharedViewModel.class).setSearchViewText (   charSequence.toString () );
                Log.d ( "check","  "+charSequence.toString () );
            }

            @Override
            public void afterTextChanged (Editable editable) {

            }
        } );




    }

    public static String getMonth(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
//        return "02";
        return monthFormat.format(calendar.getTime());

    }



    public static class Item {
        private String text;

        public Item(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

}


package com.example.dcxlogs.Fragments;

import static com.example.dcxlogs.MainActivity.getMonth;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dcxlogs.Interface.OnItemClickListener;
import com.example.dcxlogs.MainActivity;
import com.example.dcxlogs.MyAdapter;
import com.example.dcxlogs.R;
import com.example.dcxlogs.SharedViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class TimeFragment extends Fragment  {

    private RecyclerView recyclerView;
    private DateTimeAdapter adapter;
    private List<MainActivity.Item> itemList;
    String date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        FirebaseApp.initializeApp( getContext ( ) );
        if (getArguments() != null) {
            date = getArguments().getString("date", "01");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager (getContext (),2));

        itemList = new ArrayList<> ();
        adapter = new DateTimeAdapter ( itemList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DateTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String data, String type) {
                LogsFragment fragment = new LogsFragment ();
                Bundle args = new Bundle();
                args.putString("time", data);
                args.putString("date", date);
                fragment.setArguments(args);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Start a new transaction
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Replace the current fragment with Fragment2
                transaction.replace(R.id.fragment_container, fragment);

                // Add the transaction to the back stack, allowing the user to navigate back
                transaction.addToBackStack("TimeFragment");
                new ViewModelProvider(requireActivity()).get( SharedViewModel.class).setTextViewText ( "Logs" );

                // Commit the transaction
                transaction.commit();
            }
        });




        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("logs").child ( getMonth()).child ( date );
        databaseReference.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String key = snapshot.getKey();
                    // Use the 'key' as needed
                    Log.d("Key", key);
                    itemList.add ( new MainActivity.Item ( key ) );
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseExample", "Failed to read value.", databaseError.toException());
                Toast.makeText ( getContext ( ) , "Error "+databaseError.getMessage () , Toast.LENGTH_SHORT ).show ( );
            }
        });


        return view;
    }
}
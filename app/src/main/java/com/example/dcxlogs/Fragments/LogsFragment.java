package com.example.dcxlogs.Fragments;

import static com.example.dcxlogs.MainActivity.getMonth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import java.util.Objects;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;


public class LogsFragment extends Fragment {
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<MainActivity.Item> itemList;
    private List<MainActivity.Item> backupList;
    String time, date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);
        backupList = new ArrayList<> ();

        new ViewModelProvider(getActivity ()).get(SharedViewModel.class).getSearchViewText().observe(getActivity(), new Observer<String> () {
                    @Override
                    public void onChanged(@Nullable String newText) {
                        List<MainActivity.Item> filteredList = new ArrayList<>();
                        for ( MainActivity.Item item : backupList) {
                            if (item.getText().toLowerCase (  ).contains(newText.toLowerCase (  ))) {
                                filteredList.add(item);
                            }
                        }
                        Log.d ( "check ---" , " " + newText );
                        if(itemList != null) {
                            itemList.clear ( );
                            itemList.addAll ( filteredList );
                            adapter.notifyDataSetChanged ( );
//                        adapter.updateData(filteredList);
                        }
                    }
                });

        FirebaseApp.initializeApp( getContext ( ) );

        if (getArguments() != null) {
            time = getArguments().getString("time", "01");
            date = getArguments().getString("date", "01");
        }

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager (getContext ()));

        itemList = new ArrayList<> ();
        adapter = new MyAdapter ( itemList);
        recyclerView.setAdapter(adapter);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("logs").child(getMonth()).child(date).child(time);
        databaseReference.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String item = snapshot.getValue(String.class);
                    if (item != null) {
                        itemList.add(new MainActivity.Item ( item ));
                        backupList.add(new MainActivity.Item ( item ));
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseExample", "Failed to read value.", databaseError.toException());
            }
        });

        new ViewModelProvider(getActivity ()).get(SharedViewModel.class).getSearchViewText().observe(getViewLifecycleOwner(), newText -> {

        });

        return view;
    }




}
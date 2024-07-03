package com.example.dcxlogs.Fragments;

import static com.example.dcxlogs.MainActivity.DELETE_TYPE;
import static com.example.dcxlogs.MainActivity.getMonth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A fragment representing a list of Items.
 */
public class DateFragment extends Fragment implements OnItemClickListener {

    private RecyclerView recyclerView;
    private DateTimeAdapter adapter;
    private List<MainActivity.Item> itemList;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        FirebaseApp.initializeApp( getContext ( ) );
        context =getContext ();


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager (getContext (),2));

        itemList = new ArrayList<> ();
        adapter = new DateTimeAdapter ( itemList);
        adapter.setOnItemClickListener(new DateTimeAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String data , String type) {
                if ( type.equals ( DELETE_TYPE ) ) {

                    AlertDialog.Builder builder = new AlertDialog.Builder( context);
                    builder.setTitle("Delete ?")
                            .setMessage("Do you want to delete "+ data)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    FirebaseDatabase.getInstance ().getReference ().child ( "logs" )
                                            .child ( getMonth () ).child ( data ).removeValue ();
                                    for(int i=0; i<itemList.size (); i++){
                                        MainActivity.Item item = itemList.get ( i );
                                        if(item.getText ().equals ( data )){
                                            itemList.remove ( i );
                                            adapter.notifyDataSetChanged ();
                                            break;
                                        }
                                    }
                                    dialog.dismiss ();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss ();
                                }
                            })
                            .show();


                } else {
                    new ViewModelProvider ( requireActivity ( ) ).get ( SharedViewModel.class ).setTextViewText ( "Time" );

                    TimeFragment fragment = new TimeFragment ( );
                    Bundle args = new Bundle ( );
                    args.putString ( "date" , data );
                    fragment.setArguments ( args );
                    FragmentManager fragmentManager = requireActivity ( ).getSupportFragmentManager ( );

                    // Start a new transaction
                    FragmentTransaction transaction = fragmentManager.beginTransaction ( );

                    // Replace the current fragment with Fragment2
                    transaction.replace ( R.id.fragment_container , fragment );

                    // Add the transaction to the back stack, allowing the user to navigate back
                    transaction.addToBackStack ( "DateFragment" );


                    // Commit the transaction
                    transaction.commit ( );
                }
            }
        });
        recyclerView.setAdapter(adapter);




        FirebaseDatabase databaseReference = FirebaseDatabase.getInstance();

        DatabaseReference ref = databaseReference.getReference().child("logs").child(getMonth ());
        ref.keepSynced ( true );
        ref.addValueEventListener(new ValueEventListener () {
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
            }
        });


        return view;
    }

    @Override
    public void onItemClick (String data) {
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putString("date", data);
        fragment.setArguments(args);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Start a new transaction
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace the current fragment with Fragment2
        transaction.replace(R.id.fragment_container, fragment);

        // Add the transaction to the back stack, allowing the user to navigate back
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
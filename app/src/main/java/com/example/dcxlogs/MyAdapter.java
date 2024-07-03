package com.example.dcxlogs;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;


import com.example.dcxlogs.MainActivity;
import com.example.dcxlogs.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MainActivity.Item> itemList;

    public MyAdapter(List<MainActivity.Item> itemList) {
        this.itemList = itemList;
    }

    public void updateData(List<MainActivity.Item> newData) {
        itemList.clear(); // Clear existing items
        itemList.addAll(newData); // Add filtered items
        notifyDataSetChanged(); // Notify adapter about the change
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.normal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity.Item item = itemList.get(position);
        String text = item.getText();

        int colonCount = 0;
        int idx = 0;

        // Iterate through the characters in the text
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ':') {
                colonCount++;
                if (colonCount == 4) {
                    idx = i;
                    break;
                }
            }
        }
        if(text.contains ( "***" ) || text.contains ( "+++") || text.contains ( "---" )){
            holder.textView.setBackgroundResource ( R.color.suspense );
        }else{
            holder.textView.setBackgroundResource ( R.color.def );
        }
        Log.d ( TAG, "Data from firebase "+ text);
        if (colonCount >= 4) {
            // If at least 4 colons are found, split the text
            String beforeFourthColon = text.substring(0, idx+1);
            String afterFourthColon = text.substring(idx + 1);

            holder.details.setText(beforeFourthColon);
            holder.textView.setText(afterFourthColon);
        } else {
            // Handle the case where there are less than 4 colons
            holder.details.setText("Invalid Text");
            holder.textView.setText(text);
        }

    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,details;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            details = view.findViewById(R.id.details);
        }
    }
}

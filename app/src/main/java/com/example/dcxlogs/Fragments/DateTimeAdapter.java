package com.example.dcxlogs.Fragments;

import static com.example.dcxlogs.MainActivity.CLICK_TYPE;
import static com.example.dcxlogs.MainActivity.DELETE_TYPE;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dcxlogs.Interface.OnItemClickListener;
import com.example.dcxlogs.MainActivity;

import com.example.dcxlogs.MyAdapter;
import com.example.dcxlogs.R;

import java.util.List;

public class DateTimeAdapter extends RecyclerView.Adapter<DateTimeAdapter.ViewHolder> {

    private List<MainActivity.Item> itemList;
    private OnItemClickListener onItemClickListener;

    public DateTimeAdapter(List<MainActivity.Item> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_view, parent, false);
        return new DateTimeAdapter.ViewHolder (view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateTimeAdapter.ViewHolder holder, int position) {
            holder.textView.setText(itemList.get ( position ).getText ());
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(itemList.get ( position ).getText (),CLICK_TYPE);
                    }
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(itemList.get ( position ).getText (),DELETE_TYPE);
                    }
                }
            }
        });
        }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String data, String type);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imgView;
        public ImageView delete;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            imgView = view.findViewById(R.id.imageView);
            delete = view.findViewById(R.id.delete);

        }
    }
}

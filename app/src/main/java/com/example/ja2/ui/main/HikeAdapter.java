package com.example.ja2.ui.main;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.entity.Hike;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.MyViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.###");
    // 1- Variable
    private final ArrayList<Hike> data;
    private final OnItemClickListener callBack;
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    //bo sung
    public HikeAdapter(ArrayList<Hike> data, OnItemClickListener callBack) {
        this.data = data;
        this.callBack = callBack;
    }

    // tạo ra đối tượng ViewHolder, trong nó chứa View hiện thị dữ liệu
    @NonNull
    @Override
    public HikeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout cho View biểu diễn phần tử cu the
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hike, parent, false);
        return new MyViewHolder(itemView);
    }

    //chuyển dữ liệu phần tử vào ViewHolder
    @Override
    @SuppressLint({"RecyclerView", "StringFormatInvalid"})
    public void onBindViewHolder(@NonNull HikeAdapter.MyViewHolder holder, int positions) {
        final Hike hike = data.get(holder.getAdapterPosition());
        Date date = new Date(hike.getDate());
        holder.textViewDate.setText(dateFormat.format(date));
        holder.textViewDistance.setText(holder.itemView.getContext().getString(R.string.general_distance, decimalFormat.format(hike.getLength())));
        holder.textViewName.setText(hike.getName());
        holder.textViewNote.setText(hike.getDescription());
        // Xử lý khi nút Add được bấm
        holder.itemView.setOnClickListener(view -> callBack.onItemClickListener(holder.getAdapterPosition(), hike));
    }

    // cho biết số phần tử của dữ liệu
    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount()) {
            data.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updatePosition(int position, Hike hike) {
        if (position >= 0 && position <= getItemCount()) {
            data.set(position, hike);
            notifyItemChanged(position);
        } else {
            Log.e("Tag", "--- Update position fail: " + position);
        }
    }

    public void addHike(int position, Hike hike) {
        data.add(position, hike);
        notifyItemInserted(position);
    }

    public void submitData(ArrayList mData) {
        this.data.clear();
        data.addAll(mData);
        notifyDataSetChanged(); //khi dữ liệu thay đổi, update toàn bộ list
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, Hike hike);
    }

    // 2- ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDate;
        public TextView textViewDistance;
        public TextView textViewName;
        public TextView textViewNote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewDate = itemView.findViewById(R.id.text_view_date);
            this.textViewDistance = itemView.findViewById(R.id.text_view_distance);
            this.textViewName = itemView.findViewById(R.id.text_view_name);
            this.textViewNote = itemView.findViewById(R.id.text_view_note);
        }
    }
}

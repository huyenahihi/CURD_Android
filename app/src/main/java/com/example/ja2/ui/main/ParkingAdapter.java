package com.example.ja2.ui.main;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.entity.Parking;

import java.util.ArrayList;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.MyViewHolder> {

    // 1- Variable
    private final ArrayList<Parking> data;
    private final OnItemClickListener callBack;

    //bo sung
    public ParkingAdapter(ArrayList<Parking> data, OnItemClickListener callBack) {
        this.data = data;
        this.callBack = callBack;
    }

    // tạo ra đối tượng ViewHolder, trong nó chứa View hiện thị dữ liệu
    @NonNull
    @Override
    public ParkingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp layout cho View biểu diễn phần tử cu the
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parking, parent, false);
        return new MyViewHolder(itemView);
    }

    //chuyển dữ liệu phần tử vào ViewHolder
    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull ParkingAdapter.MyViewHolder holder, int positions) {
        final Parking parking = data.get(holder.getAdapterPosition());
        holder.name.setText(parking.getName());
        holder.email.setText(parking.getEmail());
        // Xử lý khi nút Chi tiết được bấm
        holder.itemView.setOnClickListener(view -> callBack.onItemClickListener(holder.getAdapterPosition(), parking));
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

    public void updatePosition(int position, Parking parking) {
        if (position >= 0 && position <= getItemCount()) {
            data.set(position, parking);
            notifyItemChanged(position);
        }
    }

    public void addTheFirsItem(Parking parking) {
        data.add(0, parking);
        notifyItemInserted(0);
    }

    public void submitData(ArrayList contact) {
        data.clear();
        data.addAll(contact);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, Parking parking);
    }

    // 2- ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.email = itemView.findViewById(R.id.email);
        }
    }
}

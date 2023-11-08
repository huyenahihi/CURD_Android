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
import com.example.ja2.db.entity.Parking;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.MyViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DecimalFormat decimalFormat = new DecimalFormat("###.###");
    // 1- Variable
    private final ArrayList<Parking> data;
    private final OnItemClickListener callBack;
    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

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
    @SuppressLint({"RecyclerView", "StringFormatInvalid"})
    public void onBindViewHolder(@NonNull ParkingAdapter.MyViewHolder holder, int positions) {
        final Parking parking = data.get(holder.getAdapterPosition());
        Date date = new Date(parking.getDate());
        holder.textViewDate.setText(dateFormat.format(date));
        holder.textViewDistance.setText(holder.itemView.getContext().getString(R.string.general_distance, decimalFormat.format(parking.getLength())));
        holder.textViewName.setText(parking.getName());
        holder.textViewEmail.setText(parking.getEmail());
        holder.textViewNote.setText(parking.getDescription());
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
        } else {
            Log.e("Tag", "--- Update position fail: " + position);
        }
    }

    public void addParking(int position, Parking parking) {
        data.add(position, parking);
        notifyItemInserted(position);
    }

    public void submitData(ArrayList mData) {
        this.data.clear();
        data.addAll(mData);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, Parking parking);
    }

    // 2- ViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewDate;
        public TextView textViewDistance;
        public TextView textViewName;
        public TextView textViewEmail;
        public TextView textViewNote;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewDate = itemView.findViewById(R.id.text_view_date);
            this.textViewDistance = itemView.findViewById(R.id.text_view_distance);
            this.textViewName = itemView.findViewById(R.id.text_view_name);
            this.textViewEmail = itemView.findViewById(R.id.text_view_email);
            this.textViewNote = itemView.findViewById(R.id.text_view_note);
        }
    }
}

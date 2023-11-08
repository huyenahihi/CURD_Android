package com.example.ja2.ui.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.entity.Observation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ViewHolder> {

    private final ArrayList<Observation> data;
    private final ObservationAdapter.OnItemClickListener callBack;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");

    public ObservationAdapter(ArrayList<Observation> data, OnItemClickListener callBack) {
        this.data = data;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_observation, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int mPosition = holder.getAdapterPosition();
        Observation observation = data.get(mPosition);
        holder.textViewTime.setText(dateFormat.format(observation.getDateTime()));
        holder.textViewNote.setText(observation.getNote());
        holder.itemView.setOnClickListener(v -> callBack.onItemClickListener(mPosition, observation));
    }

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

    public void updatePosition(int position, Observation observation) {
        if (position >= 0 && position <= getItemCount()) {
            data.set(position, observation);
            notifyItemChanged(position);
        }
    }

    public void addTheFirsItem(Observation observation) {
        data.add(0, observation);
        notifyItemInserted(0);
    }

    public void submitData(ArrayList<Observation> observation) {
        data.clear();
        data.addAll(observation);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, Observation observation);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTime;
        public TextView textViewNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewTime = itemView.findViewById(R.id.text_view_time);
            this.textViewNote = itemView.findViewById(R.id.text_view_note);
        }
    }
}

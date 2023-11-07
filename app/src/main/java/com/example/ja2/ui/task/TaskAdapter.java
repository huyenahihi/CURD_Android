package com.example.ja2.ui.task;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ja2.R;
import com.example.ja2.db.entity.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final ArrayList<Task> data;
    private final TaskAdapter.OnItemClickListener callBack;

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm");

    public TaskAdapter(ArrayList<Task> data, OnItemClickListener callBack) {
        this.data = data;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int mPosition = holder.getAdapterPosition();
        Task task = data.get(mPosition);
        holder.textViewTime.setText(dateFormat.format(task.getDateTime()));
        holder.textViewNote.setText(task.getNote());
        holder.itemView.setOnClickListener(v -> callBack.onItemClickListener(mPosition, task));
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

    public void updatePosition(int position, Task task) {
        if (position >= 0 && position <= getItemCount()) {
            data.set(position, task);
            notifyItemChanged(position);
        }
    }

    public void addTheFirsItem(Task task) {
        data.add(0, task);
        notifyItemInserted(0);
    }

    public void submitData(ArrayList<Task> task) {
        data.clear();
        data.addAll(task);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position, Task task);
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

package com.example.ja2.ui.detail;

import static android.view.View.inflate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ja2.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class LevelAdapter extends BaseAdapter {

    private final ArrayList<Integer> data;

    public LevelAdapter(ArrayList mData) {
        this.data = mData;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Integer getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textViewTitle.setText(parent.getContext().getString(R.string.general_level, data.get(position)));
        return view;
    }

    class ViewHolder {

        TextView textViewTitle = null;

        public ViewHolder(View viewItem) {
            textViewTitle = viewItem.findViewById(R.id.text_view_title);
        }
    }
}

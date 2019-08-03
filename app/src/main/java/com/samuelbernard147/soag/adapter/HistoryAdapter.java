package com.samuelbernard147.soag.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.samuelbernard147.soag.R;
import com.samuelbernard147.soag.model.Humidity;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.RiwayatViewHolder> {
    private ArrayList<Humidity> mData = new ArrayList<>();

    public HistoryAdapter() {
    }

    public void setData(ArrayList<Humidity> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public void addItem(final Humidity item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
    }

    @NonNull
    @Override
    public RiwayatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_history, viewGroup, false);
        return new RiwayatViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatViewHolder riwayatViewHolder, int position) {
        riwayatViewHolder.tvTime.setText(mData.get(position).getTime().substring(10));
        riwayatViewHolder.tvHumidity.setText(String.valueOf(mData.get(position).getHumidity()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class RiwayatViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvHumidity;

        RiwayatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time_history);
            tvHumidity = itemView.findViewById(R.id.tv_humidity_history);
        }
    }
}
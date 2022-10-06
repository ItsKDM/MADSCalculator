package com.example.kdm.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<History> historyData;
    private Context context;

    public RecyclerViewAdapter(ArrayList<History> historyData, ViewHistory viewHistory) {
        this.historyData = historyData;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.tvCount.setText(String.valueOf(historyData.get(position).getCount()));
        holder.tvExpression.setText(historyData.get(position).getExpression()+"=");
        holder.tvResult.setText(String.valueOf(historyData.get(position).getResult()));
    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCount,tvExpression,tvResult;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvCount=(TextView)itemView.findViewById(R.id.textviewCount);
            tvExpression=(TextView)itemView.findViewById(R.id.textviewExp);
            tvResult=(TextView)itemView.findViewById(R.id.textviewRes);
        }
    }
}

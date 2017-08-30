package com.digitalcurrencyexperts.rocketwallet.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.digitalcurrencyexperts.rocketwallet.R;
import com.digitalcurrencyexperts.rocketwallet.common.Interfaces;

import java.util.ArrayList;

/**
 * Created by santiagp on 23/08/2017.
 */

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ViewHolder> {
    private ArrayList<String> tittles;
    private ArrayList<Integer> images;
    private Interfaces.OptionsListCallback optionsListCallback;

    public ToolsAdapter(ArrayList<String> tittles, ArrayList<Integer> images) {
        this.tittles = tittles;
        this.images = images;
    }

    @Override
    public ToolsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_recycler_view, parent, false);
        return new ToolsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tittle.setText(tittles.get(position));
        holder.image.setImageResource(images.get(position));
        holder.mainView.setOnClickListener(v -> {
            if (optionsListCallback!=null) optionsListCallback.onOptionClick(position);
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tittles.size();
    }

    public void setOptionsListCallback(Interfaces.OptionsListCallback optionsListCallback) {
        this.optionsListCallback = optionsListCallback;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tittle;
        ImageView image;

        View mainView;

        public ViewHolder(View itemView) {
            super(itemView);
            mainView = itemView;
            tittle = (TextView) itemView.findViewById(R.id.tittle);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

}
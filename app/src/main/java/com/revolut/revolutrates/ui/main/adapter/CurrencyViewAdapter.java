package com.revolut.revolutrates.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.revolut.revolutrates.R;
import com.revolut.revolutrates.model.RevolutCurrencyItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CurrencyViewAdapter extends RecyclerView.Adapter<CurrencyViewAdapter.ViewHolder> {

    private List<RevolutCurrencyItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private float baseRate = 1;

    // data is passed into the constructor
    public CurrencyViewAdapter(Context context, List<RevolutCurrencyItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    public void updateData(List<RevolutCurrencyItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void updateBaseRate(float baseRate) {

        if (this.baseRate == baseRate) {
            return;
        }

        if (baseRate == 0) {
            baseRate = 1;
        }

        this.baseRate = baseRate;
        notifyDataSetChanged();
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_currency_rate, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RevolutCurrencyItem revolutCurrencyItem = mData.get(position);
        holder.currencyInitials.setText(revolutCurrencyItem.getCurrency());
        holder.currencyRate.setText(String.valueOf(revolutCurrencyItem.getRate() * baseRate));
        holder.currencyName.setText(revolutCurrencyItem.getCurrencyCompleteName());

        final int flag = World.getFlagOf(revolutCurrencyItem.getCountryCode());

        Picasso.get().load(flag).placeholder(flag).into(holder.currencyImage);

        if(position == 0) {
            holder.infoContainer.setVisibility(View.INVISIBLE);
        } else {
            holder.infoContainer.setVisibility(View.VISIBLE);
        }

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View infoContainer;

        ImageView currencyImage;

        TextView currencyInitials;
        TextView currencyName;
        EditText currencyRate;

        ViewHolder(View itemView) {

            super(itemView);

            infoContainer = itemView.findViewById(R.id.infoContainer);

            currencyImage = itemView.findViewById(R.id.currencyImage);
            currencyInitials = itemView.findViewById(R.id.currencyInitials);
            currencyName = itemView.findViewById(R.id.currencyName);
            currencyRate = itemView.findViewById(R.id.currencyRate);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, mData.get(getAdapterPosition()).getCurrency());
                notifyItemMoved(getAdapterPosition(), 0);
            }
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, String currency);
    }
}
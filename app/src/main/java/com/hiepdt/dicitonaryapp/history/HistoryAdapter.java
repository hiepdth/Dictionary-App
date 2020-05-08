package com.hiepdt.dicitonaryapp.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiepdt.dicitonaryapp.R;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> mListWord;

    public HistoryAdapter(Context mContext, ArrayList<String> mListWord) {
        this.mContext = mContext;
        this.mListWord = mListWord;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String text = mListWord.get(position);
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return mListWord.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView text;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);

        }
    }
}

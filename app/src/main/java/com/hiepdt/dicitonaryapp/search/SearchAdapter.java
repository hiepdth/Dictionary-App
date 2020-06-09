package com.hiepdt.dicitonaryapp.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiepdt.dicitonaryapp.R;
import com.hiepdt.dicitonaryapp.hepler.DBHelper;
import com.hiepdt.dicitonaryapp.models.Word;
import com.hiepdt.dicitonaryapp.search.result.ResultActivity;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context mContext;
    ArrayList<Word> mListHistory;
    DBHelper helper;

    public SearchAdapter(Context mContext, ArrayList<Word> mListHis) {
        this.mContext = mContext;
        this.mListHistory = mListHis;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search, parent, false);
        helper = new DBHelper(mContext);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Word word = mListHistory.get(position);
        holder.text.setText(word.getKey());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResultActivity.class);
                intent.putExtra("word", word.getKey());
                mContext.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog alertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                alertDialog
                        .setContentText("Are you sure to delete?")
                        .setConfirmText("Yes")
                        .setCancelText("Cancel")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                helper.deleteWord(word.getKey(), "history");
                                notifyItemRemoved(position);
                                mListHistory.remove(position);
                                alertDialog.cancel();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                alertDialog.cancel();
                            }
                        })
                        .showCancelButton(true)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}

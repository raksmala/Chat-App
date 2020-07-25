package com.example.api;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Akun> item;
    private Context context;
    private String senderEmail;

    public RecyclerViewAdapter(Context context, List<Akun> item, String senderEmail) {
        Log.i("autolog", "RecyclerViewAdapter");
        this.item = item;
        this.context = context;
        this.senderEmail = senderEmail;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("autolog", "onCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_row, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("autolog", "onBindViewHolder");
        holder.name.setText(item.get(position).getName());
        holder.email = item.get(position).getEmail();
        holder.receiverName = item.get(position).getName();

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Ini adalah " + holder.email, Toast.LENGTH_LONG).show();

                Bundle myBundle5 = new Bundle();
                Intent intentChat = new Intent(context, ChatActivity.class);
                myBundle5.putString("receiverName", holder.receiverName);
                myBundle5.putString("receiverEmail", holder.email);
                myBundle5.putString("senderEmail", senderEmail);
                intentChat.putExtras(myBundle5);
                context.startActivity(intentChat);
            }
        });
    }


    @Override
    public int getItemCount() {
        Log.i("autolog", "getItemCount");
        return item.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public String email, receiverName;
        public LinearLayout linearLayout;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            Log.i("autolog", "ViewHolder");

            name = itemView.findViewById(R.id.name);
            relativeLayout = itemView.findViewById(R.id.rl);
        }
    }
}

package com.example.api;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MessageResults> item;
    private Context context;
    private String sender, receiver;
    private static final int TYPE_SENDER = 1;
    private static final int TYPE_RECEIVER = 2;

    public RecyclerViewAdapter2(Context context, List<MessageResults> item, String sender, String receiver) {
        Log.i("autolog", "RecyclerViewAdapter2");
        this.item = item;
        this.context = context;
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("autolog", item.get(position).getSender());
        Log.i("autolog", sender);
        if(item.get(position).getSender().equals(sender)) {
            Log.i("autolog", "TYPE_SENDER");
            return TYPE_SENDER;
        } else {
            Log.i("autolog", "TYPE_RECEIVER");
            return TYPE_RECEIVER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Log.i("autolog", "onCreateViewHolder");
        View view;
        if (viewType == TYPE_SENDER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_sent_message, parent, false);
            return new Sender(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_received_message, parent, false);
            return new Receiver(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("autolog", "onBindViewHolder");
        if (getItemViewType(position) == TYPE_SENDER) {
            ((Sender) holder).setSenderDetails(position);
        } else {
            ((Receiver) holder).setReceiverDetails(position);
        }
    }

    @Override
    public int getItemCount() {
        Log.i("autolog", "getItemCount");
        return item.size();
    }

    class Sender extends RecyclerView.ViewHolder{
        private TextView sentTxt;

        public Sender(View itemView) {
            super(itemView);
            Log.i("autolog", "Sender");

            sentTxt = itemView.findViewById(R.id.sentTxt);
        }

        void setSenderDetails(int position) {
            sentTxt.setText(item.get(position).getMessage());
        }
    }

    class Receiver extends RecyclerView.ViewHolder{
        private TextView receivedTxt;

        public Receiver(View itemView) {
            super(itemView);
            Log.i("autolog", "Receiver");

            receivedTxt = itemView.findViewById(R.id.receivedTxt);
        }

        void setReceiverDetails(int position) {
            receivedTxt.setText(item.get(position).getMessage());
        }
    }

    public void addItem(MessageResults messageResults) {
        item.add(messageResults);
        notifyDataSetChanged();
    }
}

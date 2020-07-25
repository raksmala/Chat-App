package com.example.api;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private LinearLayoutManager layoutManager;
    private MessageResults messageResults;
    private List<MessageResults> chatList = null;
    private String name, receiver, sender, message, senderSrv, receiverSrv, messageSrv;
    private ImageView btnSend;
    private EditText msgEdit;
    private Socket socket;
    RecyclerViewAdapter2 recyclerViewAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle myBundle6 = getIntent().getExtras();
        name = myBundle6.getString("receiverName");
        receiver = myBundle6.getString("receiverEmail");
        sender = myBundle6.getString("senderEmail");
        btnSend = findViewById(R.id.btnSend);
        msgEdit = findViewById(R.id.messageEdit);

        Log.i("autolog", "getChatList");
        try {
            String url = "http://192.168.1.9:3000";
            Log.i("autolog", "http://192.168.1.9:3000");

            Retrofit retrofit = null;
            Log.i("autolog", "retrofit");

            if(retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                Log.i("autolog", "build();");
            }

            RetrofitInterface service = retrofit.create(RetrofitInterface.class);
            Log.i("autolog", " RetrofitInterface service = retrofit.create(RetrofitInterface.class);");

            HashMap<String, String> map = new HashMap<>();
            map.put("receiver", receiver);
            map.put("sender", sender);
            Log.i("autolog", sender);
            Log.i("autolog", receiver);
            Call<List<MessageResults>> call = service.getMessage(map);

            call.enqueue(new Callback<List<MessageResults>>() {
                @Override
                public void onResponse(Call<List<MessageResults>> call, Response<List<MessageResults>> response) {
                    Log.i("autolog", "onResponse");

                    chatList = response.body();
                    Log.i("autolog", "List<MessageResults> chatList = response.body();");

                    RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);
                    Log.i("autolog", "RecyclerView recyclerView2 = findViewById(R.id.recyclerView2);");

                    layoutManager = new LinearLayoutManager(ChatActivity.this);
                    Log.i("autolog", "layoutManager = new LinearLayoutManager(ChatActivity.this);");
                    recyclerView2.setLayoutManager(layoutManager);
                    Log.i("autolog", "recyclerView2.setLayoutManager(layoutManager);");

                    SpacingItemDecorator itemDecorator = new SpacingItemDecorator(10);
                    recyclerView2.addItemDecoration(itemDecorator);

                    RecyclerViewAdapter2 recyclerViewAdapter3 = new RecyclerViewAdapter2(getApplicationContext(), chatList, sender, receiver);
                    Log.i("autolog", "RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), chatList, sender, receiver);");
                    recyclerView2.setAdapter(recyclerViewAdapter3);
                    Log.i("autolog", "recyclerView2.setAdapter(recyclerViewAdapter2);");
                }

                @Override
                public void onFailure(Call<List<MessageResults>> call, Throwable t) {

                }
            });
        }catch (Exception e) {
            Log.i("autolog", "Exception");
        }

        try {
            socket = IO.socket("http://192.168.1.9:3000");
            socket.connect();
            socket.emit("user_connected", sender);

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.equals(btnSend)) {
                        message = msgEdit.getText().toString();
                        socket.emit("send_message", sender, receiver, message);
                        msgEdit.setText("");

                        socket.on("new_message", new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MessageResults messageResults = new MessageResults(sender, receiver, message);

                                        messageResults.setSender(sender);
                                        messageResults.setReceiver(receiver);
                                        messageResults.setMessage(message);

                                        recyclerViewAdapter2.addItem(messageResults);
                                    }
                                });
                            }
                        });
                    }
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
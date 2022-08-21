package com.raagav.chatbot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRv;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFAB;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatsModal> ChatsModalArrayList;
    private ChatRVAdapter ChatRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing all our views.
        chatsRv    = findViewById(R.id.idRVChats);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        sendMsgFAB = findViewById(R.id.idFABSend);


        ChatsModalArrayList = new ArrayList<>();
        ChatRVAdapter = new ChatRVAdapter(ChatsModalArrayList, this);

        // LinearLayoutManager -> Provides similar function to listView.
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        chatsRv.setLayoutManager(manager);
        chatsRv.setItemAnimator(new DefaultItemAnimator());
        chatsRv.setAdapter(ChatRVAdapter);

        sendMsgFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMsgEdt.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your message", Toast.LENGTH_SHORT).show();
                    return;
                }
                getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear:  {
                ProgressDialog progress = new ProgressDialog(this);
                progress.setMessage("Please wait a moment ");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setCancelable(false);
                ChatsModalArrayList.clear();
                ChatRVAdapter.notifyDataSetChanged();
                progress.dismiss();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getResponse(String message) {
        ChatsModalArrayList.add(new ChatsModal(message, USER_KEY)); // User message;
        ChatRVAdapter.notifyDataSetChanged(); // To Notify that the data is changed.

        String url = "http://api.brainshop.ai/get?bid=168601&key=ZkdPkydGwFZauozq&uid=[uid]&msg="+message;
        String BASE_URL = "http://api.brainshop.ai/";

        // Retrofit is a kind of REST API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<MsgModal> call = retrofitAPI.getMessage(url); // request have been made.
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if (response.isSuccessful()) {
                    MsgModal modal = response.body();
                    ChatsModalArrayList.add(new ChatsModal(modal.getCnt(), BOT_KEY));
                    ChatRVAdapter.notifyDataSetChanged();
                    chatsRv.getLayoutManager().smoothScrollToPosition(chatsRv, null, ChatRVAdapter.getItemCount() - 1);
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            ChatRVAdapter.notifyDataSetChanged();
//                            if (ChatRVAdapter.getItemCount() > 1) {
//
//
//                            }
//
//                        }
//                    });
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                ChatsModalArrayList.add(new ChatsModal("Please revert your question", BOT_KEY));
                ChatRVAdapter.notifyDataSetChanged();
            }
        });
    }
}


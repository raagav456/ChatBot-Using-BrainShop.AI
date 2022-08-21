package com.raagav.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatRVAdapter extends RecyclerView.Adapter {

    private ArrayList<ChatsModal> chatsModalArrayList;
    private Context context;

    public ChatRVAdapter(ArrayList<ChatsModal> chatsModalArrayList, Context context) {
        this.chatsModalArrayList = chatsModalArrayList;
        this.context = context;
    }

    // ViewHolder -> Describes an item view and metadata about its place within the RecyclerView.
    // To Create ViewHolder and initialize private fields.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0 :
                // LayoutInflater -> Instantiates a layout XML file into its corresponding View objects.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg_rv_item, parent, false);
                return new UserViewHolder(view);

            case 1 :
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg_rv_item, parent, false);
                return new BotViewHolder(view);

        }
        return null;
    }

    // To Update ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatsModal chatsModal = chatsModalArrayList.get(position);
        switch (chatsModal.getSender()) {
            case "user" :
                ((UserViewHolder)holder).userTv.setText(chatsModal.getMessage());
                ((UserViewHolder)holder).userTv.setVisibility(View.VISIBLE);
                break;
            case "bot" :
                ((BotViewHolder)holder).botMsgTv.setText(chatsModal.getMessage());
                ((BotViewHolder)holder).botMsgTv.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatsModalArrayList.get(position).getSender()) {
            case "user" :
                return 0;
            case "bot" :
                return 1;
            default :
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return chatsModalArrayList.size();
    }

    // ViewHolder -> Describes an item view and metadata about its place within the RecyclerView.
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userTv;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userTv = itemView.findViewById(R.id.idTVUser);
        }
    }

    // ViewHolder -> Describes an item view and metadata about its place within the RecyclerView.
    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botMsgTv;
        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMsgTv = itemView.findViewById(R.id.idTVBot);
        }
    }
}

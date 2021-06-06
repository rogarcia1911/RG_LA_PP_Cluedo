package com.example.rg_la_pp_cluedo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<HolderMessage> {

    private List<RecieveMessage> messageList = new ArrayList<>();
    private Context context = null;


    public MessageAdapter(Context context){
        this.context = context;
    }

    public void addMessage(RecieveMessage msg){
        messageList.add(msg);
        notifyItemInserted(messageList.size());
    }

    @NonNull
    @Override
    public HolderMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_view, parent, false);
        return new HolderMessage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMessage holder, int position) {
        holder.getTvPlayerName().setText(messageList.get(position).getMessagePlayer());
        holder.getTvMessageView().setText(messageList.get(position).getMessageText());

        Long messageCode = messageList.get(position).getHour();

        Date date = new Date(messageCode);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");

        holder.getTvMessageDate().setText(dateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

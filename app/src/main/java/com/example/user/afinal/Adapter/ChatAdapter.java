package com.example.user.afinal.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.afinal.Model.BaseMessage;
import com.example.user.afinal.Model.ChatMessage;
import com.example.user.afinal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 1/11/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{


    private String userId;
    private Context context;

    //Tag for tracking self message
    private int SELF = 786;

    //ArrayList of messages object containing all the messages in the thread
    private ArrayList<ChatMessage> messages;

    //private int selectedPos = RecyclerView.NO_POSITION;
    int selected_position = 0;
    private List<ChatMessage> selectedmsg = new ArrayList<>();
    Boolean isMultiSelect = false;
    android.os.Handler handler = new android.os.Handler();


    public ChatAdapter(Context context, ArrayList<ChatMessage> messages, String userId) {

        this.userId = userId;
        this.messages = messages;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }


      @Override
  public int getItemViewType(int position) {

    ChatMessage message = messages.get(position);
        if (message.getUsersId().equalsIgnoreCase(userId)) {
            // If the current user is the sender of the message
            return SELF;
        } else {
            // If some other user sent the message
            return position;
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Creating view
        View itemView;
        //if view type is self
        if (viewType == SELF) {
            //Inflating the layout self
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_thread, parent, false);
        } else {
            //else inflating the layout others
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_thread_other, parent, false);
        }

        //returing the view
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ChatMessage message = messages.get(position);
        holder.textViewMessage.setText(message.getMessage());
        holder.textViewTime.setText(message.getSentAt());

        message.setView(holder);

    }

    public ChatMessage getItem(int position) {
        return messages.get(position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewMessage;
        public TextView textViewTime;
        public ImageView imgstatus;
        public View views;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            textViewTime = (TextView) itemView.findViewById(R.id.textViewTime);

        }

        @Override
        public void onClick(View v) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;


        }
    }

}

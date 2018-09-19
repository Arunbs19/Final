package com.example.user.afinal.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.afinal.Adapter.ChatAdapter;
import com.example.user.afinal.Model.ChatMessage;
import com.example.user.afinal.R;
import com.example.user.afinal.Utility.MyApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatroomActivity extends AppCompatActivity {
    TextView txtname;
    TextView txtid;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter adapter;
    private ArrayList<ChatMessage> messages;
    private Button buttonSend;
    private EditText editTextMessage;
    private  Button btndelete;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        init();
        initcallback();

    }

    private void init() {
        try {
            txtname =(TextView)findViewById(R.id.txtname);
            txtid =(TextView)findViewById(R.id.txtid);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            MyApplication.getInstance().setRecycleview(recyclerView);
           // btndelete = (Button)findViewById(R.id.btndelete);
            //btndelete.setVisibility(View.INVISIBLE);
            //MyApplication.getInstance().setDeleteicon("NO");
            buttonSend = (Button) findViewById(R.id.buttonSend);
            editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        }catch (Exception ex){
            Log.e("chatroom init",ex.getMessage().toString());
        }
    }

    private  void initcallback(){
        try{

            txtname.setText(MyApplication.getInstance().getMsgname()+ MyApplication.getInstance().getMsgid());
            txtid.setText(MyApplication.getInstance().getMsgid());
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            messages = new ArrayList<ChatMessage>();



            buttonSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    message = editTextMessage.getText().toString().trim();
                    if (message.equalsIgnoreCase(""))
                        return;
                    String userId = MyApplication.getInstance().getMsgid();
                    String sentAt = getTimeStamp();
                    String name = MyApplication.getInstance().getMsgname();

                    ChatMessage m = new ChatMessage(userId, message, sentAt,name);
                    messages.add(m);
                    adapter = new ChatAdapter(ChatroomActivity.this, messages, MyApplication.getInstance().getMsgid());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                    editTextMessage.setText("");
                }
            });

        }catch (Exception ex){
            Log.e("chatroom initcallback",ex.getMessage().toString());
        }
    }
    public static String getTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return format.format(new Date());
    }
}

package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.Chat;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/** Displays the correct chat layout depending on the chat contents -> look at getItemViewType method,
 *  which determines if the left or right chat item is rendered. This is then populated in viewholder class via onBindViewHolder**/
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<Chat> userChats;
    private FirebaseUser firebaseUser;
    //private String imageurl;

    //determines whether to render the left or right chat message
    public static final int RIGHT_MSG = 1;
    public static final int LEFT_MSG  = 0;

    public MessageAdapter(Context context, List<Chat> userChats) {
        this.context   = context;
        this.userChats = userChats;
        //this.imageurl  = imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == RIGHT_MSG){
            View view = LayoutInflater.from(context).inflate(R.layout.right_chat_item_layout, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.left_chat_item_layout, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = userChats.get(position);
        holder.userMessage.setText(chat.getMessage());
       /**
        if(imageurl.equals(null)){
            holder.userImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageurl).into(holder.userImage);
        }
        **/
    }

    @Override
    public int getItemCount() {
        return userChats.size();
    }


    //Determines if the message is a left type or right type, depending on the sender's ID
    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(userChats.get(position).getSenderid().equals(firebaseUser.getUid())){
            return RIGHT_MSG;
        } else {
            return LEFT_MSG;
        }
    }

    //populates the layouts called 'left_chat_item_layout' 'right_chat_item_layout'
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userMessage;
        public ImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userMessage = itemView.findViewById(R.id.userMessage);
            userImage   = itemView.findViewById(R.id.userImage);
        }
    }
}

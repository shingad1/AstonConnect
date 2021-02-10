package com.devin.astonconnect.Chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/** An Adapter class to show users on the ChatFragment **/
public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList; //list of users to present
    private FirebaseUser currentUser;


    public ChatUserAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_user_item, parent, false);
        return new ChatUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = userList.get(position);

        //Add more status colours here...
        //Sets the visibility based on the user's status (online or not)
        switch(user.getUserstatus()){
            case "online": //if the other user is online
                holder.statusImageOnline.setVisibility(View.VISIBLE);
                holder.statusImageOffline.setVisibility(View.GONE);
                break;
            case "offline": //if the other user is offline
                holder.statusImageOffline.setVisibility(View.VISIBLE);
                holder.statusImageOnline.setVisibility(View.GONE);
                break;
            case"nostatus": //if the other user set their preferences to have 'no status'
                holder.statusImageOnline.setVisibility(View.GONE);
                holder.statusImageOffline.setVisibility(View.GONE);
        }


        //binding data to viewholder
        Glide.with(context).load(user.getImageurl()).into(holder.profile_image);
        holder.fullname.setText(user.getFullname());

        //hardcoded for now...
        holder.latestMessage.setText("hardcoded");

        /** Other functionality can be added here - check the UserAdapter for more **/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessagingActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile_image;
        public TextView fullname;
        public TextView latestMessage;
        private ImageView statusImageOnline;
        private ImageView statusImageOffline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            statusImageOffline  = itemView.findViewById(R.id.status_image_offline);
            statusImageOnline   = itemView.findViewById(R.id.status_image_online);
            profile_image = itemView.findViewById(R.id.profile_image);
            fullname      = itemView.findViewById(R.id.fullname);
            latestMessage = itemView.findViewById(R.id.latestMessage);
        }
    }
}

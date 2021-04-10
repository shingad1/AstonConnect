package com.devin.astonconnect.Chat;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class ChatUserSearchAdapter extends RecyclerView.Adapter<ChatUserSearchAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;
    private FirebaseUser firebaseUser;

    public ChatUserSearchAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ic_chat_search_user_item, parent, false);
        return new ChatUserSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserSearchAdapter.ViewHolder holder, int position) {
        User user = userList.get(position);
        Glide.with(context).load(user.getImageurl()).into(holder.image_profile);
        holder.fullname.setText(user.getFullname());
        holder.profileId = user.getId();
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class ViewHolder  extends RecyclerView.ViewHolder{

        private ImageView image_profile;
        private TextView fullname;
        private RelativeLayout btn_chat;
        private String profileId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            fullname = itemView.findViewById(R.id.fullname);
            btn_chat = itemView.findViewById(R.id.btn_chat);


            btn_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, MessagingActivity.class);
                    intent.putExtra("userid", profileId);
                    context.startActivity(intent);
                }
            });
        }
    }
}

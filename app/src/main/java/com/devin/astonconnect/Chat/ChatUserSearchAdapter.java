package com.devin.astonconnect.Chat;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devin.astonconnect.Model.User;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatUserSearchAdapter extends RecyclerView.Adapter<ChatUserSearchAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<User> userList;
    private List<User> userListFull;

    public ChatUserSearchAdapter(Context context, List<User> userList){
        this.context = context;
        this.userList = userList;
        userListFull = new ArrayList<>(userList);
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(userListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User item : userListFull) {
                    if (item.getFullname().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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

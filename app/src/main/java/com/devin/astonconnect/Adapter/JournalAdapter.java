package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.devin.astonconnect.Journal.JournalEntry2Fragment;
import com.devin.astonconnect.Journal.ViewJournalEntryFragment;
import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder>{
    private Context context;
    private List<JournalItem> journalItems;

    public JournalAdapter(Context context, List<JournalItem> journalItems){
        this.context = context;
        this.journalItems = journalItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.journal_item, parent, false);
        return new JournalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JournalItem item = journalItems.get(position);

        String submittedEpoch = item.getJournalEntrySubmitted();
        Date submittedDate = new Date(Long.parseLong(submittedEpoch) * 1000);

        //holder.monthText.setText(DateFormatSymbols.getInstance().getMonths()[submittedDate.getMonth() - 1]);
        //holder.dayText.setText(submittedDate.getDay());
        holder.entryName.setText(item.getEntryName());
        holder.descriptionText.setText(item.getEntryTime() + " at " + item.getEntryLocation());
        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return journalItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView entryName;
        public TextView descriptionText;
        public ImageView trashCan;
        public JournalItem item;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.entryName);
            descriptionText = itemView.findViewById(R.id.descriptionText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("JournalItem", item);

                    Navigation.findNavController(view).navigate(R.id.action_journalFragment_to_viewJournalEntryFragment, bundle);
                }
            });
        }
    }
}

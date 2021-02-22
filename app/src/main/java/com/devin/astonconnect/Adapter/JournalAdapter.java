package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        holder.monthText.setText("month");
        holder.dayText.setText("day");
        holder.moodText.setText(item.getEntryMood());
        holder.descriptionText.setText(item.getEntryTime() + " - " + item.getEntryLocation());
    }

    @Override
    public int getItemCount() {
        return journalItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView monthText;
        public TextView dayText;
        public TextView moodText;
        public TextView descriptionText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            monthText = itemView.findViewById(R.id.monthText);
            dayText   = itemView.findViewById(R.id.dayText);
            moodText  = itemView.findViewById(R.id.moodText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
        }
    }
}

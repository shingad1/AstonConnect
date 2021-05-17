package com.devin.astonconnect.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

/**
 * Used to populate the recyclerview within the JournalFragment
 */
public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private Context context;
    private List<JournalItem> journalItems;
    private RecyclerView recyclerView; //the recyclerview that the adapter is attached to

    public JournalAdapter(Context context, List<JournalItem> journalItems) {
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

        holder.entryName.setText(item.getEntryName());
        holder.descriptionText.setText(item.getEntryTime() + " at " + item.getEntryLocation());
        holder.item = item;
        holder.entryId = item.getEntryId();
    }

    public void updateList(JournalItem removedItem) {
        for (int i = 0; i < journalItems.size(); i++) {
            if (journalItems.get(i).getEntryId().equals(removedItem.getEntryId())) {
                journalItems.remove(i);
                this.notifyDataSetChanged();
                recyclerView.invalidate();
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return journalItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView entryName;
        public TextView descriptionText;
        public RelativeLayout deleteEntry;
        public JournalItem item;
        public String entryId;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            entryName = itemView.findViewById(R.id.entryName);
            descriptionText = itemView.findViewById(R.id.descriptionText);
            deleteEntry = itemView.findViewById(R.id.deleteEntry);

            /**
             * Deleting an entry means creating a new database reference and removing it from the database by calling .removeValue()
             */
            deleteEntry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Journal").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(entryId);
                    reference.removeValue();
                    updateList(item);
                }
            });

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

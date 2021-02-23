package com.devin.astonconnect.Journal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;

import org.w3c.dom.Text;

public class ViewJournalEntryFragment extends Fragment {
    private JournalItem item;
    private TextView entryOverViewText;
    private TextView thoughtsBeforeReflecting;
    private TextView thoughtsAfterReflecting;
    private TextView moodChangedText;
    private ImageView goBackBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_view_journal_entry, container, false);

        Bundle bundle = getArguments();
        this.item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryMood(), Toast.LENGTH_SHORT).show();

        entryOverViewText = view.findViewById(R.id.entryOverViewText);
        thoughtsBeforeReflecting = view.findViewById(R.id.thoughtsBeforeReflecting);
        thoughtsAfterReflecting = view.findViewById(R.id.thoughtsAfterReflecting);
        moodChangedText = view.findViewById(R.id.moodChangedText);
        goBackBtn = view.findViewById(R.id.backBtn);

        entryOverViewText.setText("You felt " + item.getEntryMood() + ", " + "Strength " + item.getEntryIntensity() + " on " +
                                  item.getEntryTime() + " at " + item.getEntryLocation());

        thoughtsBeforeReflecting.setText(item.getEntryThoughts());
        thoughtsAfterReflecting.setText(item.getOutlookChanged());

        Integer beforeReflectingIntensity = Integer.parseInt(item.getEntryIntensity());
        Integer afterReflectingIntensity  = Integer.parseInt(item.getChangedEntryIntensity());

        if(beforeReflectingIntensity == afterReflectingIntensity){
            moodChangedText.setText("Your mood stayed the same throughout reflecting");
        } else if (beforeReflectingIntensity > afterReflectingIntensity){
            moodChangedText.setText("Your mood decreased by " + String.valueOf((beforeReflectingIntensity - afterReflectingIntensity)) + " strengths through reflection");
        } else if (beforeReflectingIntensity < afterReflectingIntensity){
            moodChangedText.setText("Your mood increased by " + String.valueOf((afterReflectingIntensity - beforeReflectingIntensity)) + " strengths through reflection");
        }

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
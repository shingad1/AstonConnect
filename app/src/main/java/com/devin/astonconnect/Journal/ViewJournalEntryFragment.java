package com.devin.astonconnect.Journal;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;

public class ViewJournalEntryFragment extends Fragment {
    private JournalItem item;
    private TextView entryOverViewText, whatHappenedText, thoughtsText, addReflectionText;
    private ImageView goBackImageView, emoji;
    private RelativeLayout goBackBtn;
    private RelativeLayout addReflectionBtn;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_view_journal_entry, container, false);

        //Get data from the previous fragment.
        Bundle bundle = getArguments();
        this.item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryMood(), Toast.LENGTH_SHORT).show();

        //Set data.
        whatHappenedText = view.findViewById(R.id.whatHappenedText);
        thoughtsText     = view.findViewById(R.id.thoughtsText);
        entryOverViewText = view.findViewById(R.id.entryOverViewText);
        goBackImageView = view.findViewById(R.id.backImageView);
        goBackBtn = view.findViewById(R.id.goBackBtn);
        addReflectionBtn = view.findViewById(R.id.addReflectionButton);
        addReflectionText = view.findViewById(R.id.addReflectionText);
        entryOverViewText.setText("You felt " + item.getEntryMood() + ", " + "Strength " + item.getEntryIntensity() + " on " + item.getEntryTime() + " at " + item.getEntryLocation());
        whatHappenedText.setText(item.getEntryWhatHappened());
        thoughtsText.setText(item.getEntryThoughts());
        emoji = view.findViewById(R.id.emoji);
        goBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        if(item.getOutlookReflection() != null){
            addReflectionText.setText("Update/View Reflection");
        }

        addReflectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the next fragment and add bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable("JournalItem", item);

                Navigation.findNavController(view).navigate(R.id.action_viewJournalEntryFragment_to_addReflectionFragment, bundle);
            }
        });

        if(item.getEntryMood() != null){
            if(item.getEntryMood() == "Anxious"){
                emoji.setImageResource(R.drawable.ic_anxious);
            } else if (item.getEntryMood() == "Depressed"){
                emoji.setImageResource(R.drawable.ic_depressed);
            } else if (item.getEntryMood() == "Angry"){
                emoji.setImageResource(R.drawable.ic_angry);
            } else if (item.getEntryMood() == "Happy"){
                emoji.setImageResource(R.drawable.ic_happy);
            } else if (item.getEntryMood() == "Relaxed"){
                emoji.setImageResource(R.drawable.ic_relaxed);
            } else if (item.getEntryMood() == "Confused"){
                emoji.setImageResource(R.drawable.ic_confused);
            }
        }




        return view;
    }
}
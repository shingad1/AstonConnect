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
import android.widget.TextView;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;

public class ViewJournalEntryFragment extends Fragment {
    private JournalItem item;
    private TextView entryOverViewText;
    private ImageView goBackImageView;
    private Button goBackBtn;
    private Button addReflectionBtn;

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
        entryOverViewText = view.findViewById(R.id.entryOverViewText);
        goBackImageView = view.findViewById(R.id.backImageView);
        goBackBtn = view.findViewById(R.id.goBackBtn);
        addReflectionBtn = view.findViewById(R.id.addReflectionButton);
        entryOverViewText.setText("You felt " + item.getEntryMood() + ", " + "Strength " + item.getEntryIntensity() + " on " + item.getEntryTime() + " at " + item.getEntryLocation());
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
            addReflectionBtn.setText("Update/View Reflection");
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




        return view;
    }
}
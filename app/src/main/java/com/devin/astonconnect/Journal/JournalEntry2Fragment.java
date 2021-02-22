package com.devin.astonconnect.Journal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;

public class JournalEntry2Fragment extends Fragment {

    private EditText moodWhatHappened, moodThoughts;
    private Button nextButton;
    private ImageView backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_journal_entry2, container, false);

        //get object from previous fragment
        Bundle bundle = getArguments();
        JournalItem item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryMood(), Toast.LENGTH_SHORT).show();

        moodWhatHappened = view.findViewById(R.id.moodWhatHappened);
        moodThoughts     = view.findViewById(R.id.moodThoughts);
        nextButton       = view.findViewById(R.id.nextButton);

        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moodWhatHappened != null && moodThoughts != null){
                    item.setEntryThoughts(moodThoughts.getText().toString());
                    item.setEntryWhatHappened(moodWhatHappened.getText().toString());


                    Fragment fragment = new JournalEntry3Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("JournalItem", item);
                    fragment.setArguments(bundle);

                    //navigate!
                    Navigation.findNavController(view).navigate(R.id.action_journalEntry2Fragment_to_journalEntry3Fragment, bundle);

                } else {
                    Toast.makeText(getActivity(), "Please ensure all fields are completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
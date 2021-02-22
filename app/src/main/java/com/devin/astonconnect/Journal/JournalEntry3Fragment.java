package com.devin.astonconnect.Journal;

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


public class JournalEntry3Fragment extends Fragment {

    private TextView thoughtsTextView;
    private Button nextButton;
    private ImageView backBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_entry3, container, false);

        //get object from previous fragment
        Bundle bundle = getArguments();
        JournalItem item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryThoughts(), Toast.LENGTH_SHORT).show();

        nextButton = view.findViewById(R.id.nextButton);
        thoughtsTextView = view.findViewById(R.id.thoughtsTextView);
        thoughtsTextView.setText(item.getEntryThoughts());


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
                Fragment fragment = new JournalEntry3Fragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("JournalItem", item);
                fragment.setArguments(bundle);

                Navigation.findNavController(view).navigate(R.id.action_journalEntry3Fragment_to_journalEntry4Fragment, bundle);
            }
        });
        return view;
    }
}
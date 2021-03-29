package com.devin.astonconnect.Journal;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.devin.astonconnect.Model.JournalItem;
import com.devin.astonconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class JournalEntry2Fragment extends Fragment {

    private EditText moodWhatHappened, moodThoughts;
    private RelativeLayout submitBtn;
    private ImageView backBtn;
    private RelativeLayout selectDateButton;
    private String entryTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_journal_entry2, container, false);

        //get object from previous fragment
        Bundle bundle = getArguments();
        JournalItem item = bundle.getParcelable("JournalItem");
        Toast.makeText(getActivity(), item.getEntryMood(), Toast.LENGTH_SHORT).show();

        moodWhatHappened = view.findViewById(R.id.moodWhatHappened);
        moodThoughts     = view.findViewById(R.id.moodThoughts);
        submitBtn = view.findViewById(R.id.submitBtn);

        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        selectDateButton = view.findViewById(R.id.selectDateButton);
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);

                                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yy K:mm a");
                                Toast.makeText(getActivity(), simpleDateFormat.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                                entryTime = simpleDateFormat.format(calendar.getTime());
                                //dateTimeText.setText(entryTime);
                            }
                        };
                        new TimePickerDialog(getActivity(),timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
                    }
                };
                new DatePickerDialog(getActivity(),dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(moodWhatHappened != null && moodThoughts != null) {
                    item.setEntryThoughts(moodThoughts.getText().toString());
                    item.setEntryWhatHappened(moodWhatHappened.getText().toString());
                    item.setEntryTime(entryTime);

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Journal").child(firebaseUser.getUid());
                    String entryId = databaseReference.push().getKey();

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("journalEntrySubmitted", String.valueOf(System.currentTimeMillis()));
                    hashMap.put("entryMood", item.getEntryMood());
                    hashMap.put("entryLocation", item.getEntryLocation());
                    hashMap.put("entryIntensity", item.getEntryIntensity());
                    hashMap.put("entryTime", item.getEntryTime());
                    hashMap.put("entryName", item.getEntryName());
                    hashMap.put("entryThoughts", item.getEntryThoughts());
                    hashMap.put("entryWhatHappened", item.getEntryWhatHappened());
                    hashMap.put("entryId", entryId);

                    databaseReference.child(entryId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(), "Journal entry saved! " + ("\ud83d\ude01"), Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_journalEntry2Fragment_to_journalFragment);
                            }
                        }
                    });
                    /**
                    //Fragment fragment = new JournalEntry3Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("JournalItem", item);
                    //fragment.setArguments(bundle);

                    //navigate!
                    Navigation.findNavController(view).navigate(R.id.action_journalEntry2Fragment_to_journalEntry3Fragment, bundle);
                    **/
                } else {
                    Toast.makeText(getActivity(), "Please ensure all fields are completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
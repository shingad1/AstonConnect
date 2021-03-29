package com.devin.astonconnect.Journal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devin.astonconnect.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> spinnerItemList){
        super(context, 0, spinnerItemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.mood_spinner_row, parent, false
            );
        }
        ImageView imageView = convertView.findViewById(R.id.image_view_mood);
        TextView textViewName = convertView.findViewById(R.id.mood_text);

        SpinnerItem currentItem = getItem(position);

        if(currentItem != null){
            imageView.setImageResource(currentItem.getSpinnerImage());
            textViewName.setText(currentItem.getSpinnerItemName());
        }

        return convertView;
    }
}

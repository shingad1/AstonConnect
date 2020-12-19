package com.devin.astonconnect;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewsfeedFragment extends Fragment {

    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private FloatingActionButton add_btn, createPostBtn, createImagePostBtn;
    private Boolean clicked = false; //for animation purposes



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        add_btn            = view.findViewById(R.id.add_btn);
        createPostBtn      = view.findViewById(R.id.createPostBtn);
        createImagePostBtn = view.findViewById(R.id.createImagePostBtn);

        //Animations for the buttons
        rotateOpen  = AnimationUtils.loadAnimation((getActivity()), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation((getActivity()), R.anim.rotate_close_anim);
        fromBottom  = AnimationUtils.loadAnimation((getActivity()), R.anim.from_bottom_anim);
        toBottom    = AnimationUtils.loadAnimation((getActivity()), R.anim.to_bottom_anim);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddButtonClicked(); //Triggers the animation
            }
        });

        createPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Create text post clicked", Toast.LENGTH_SHORT).show();
            }
        });

        createImagePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Create image post clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(Boolean clicked){
        if(!clicked){
            createPostBtn.setVisibility(View.VISIBLE);
            createImagePostBtn.setVisibility(View.VISIBLE);
        } else {
            createPostBtn.setVisibility(View.INVISIBLE);
            createImagePostBtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(Boolean clicked){
        if(!clicked){
            createPostBtn.startAnimation(fromBottom);
            createImagePostBtn.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        } else {
            createPostBtn.startAnimation(toBottom);
            createImagePostBtn.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }
}
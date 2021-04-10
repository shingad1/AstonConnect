package com.devin.astonconnect.Chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devin.astonconnect.R;
import com.google.android.material.tabs.TabLayout;

//holds the active chats that the user can click on and open
public class ChatFragment extends Fragment {

    //Tab layout stuff
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_chat, container, false);

        //Tablayout
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewpager);
        setAdapter();

        return view;
    }

    private void setAdapter(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        Fragment chatCreateFragment = new ChatUserSearchFragment();
        Fragment myChatsFragment    = new MyChatsFragment();

        adapter.addFragment(chatCreateFragment, "Start a new chat");
        adapter.addFragment(myChatsFragment, "My Chats");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
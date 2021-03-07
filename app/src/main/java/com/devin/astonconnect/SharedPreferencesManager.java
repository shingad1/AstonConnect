package com.devin.astonconnect;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Handles the storing and retrieval of local user data
 * Should be used to store local user data when the user logs in (in LoginActivity)
 * Should be cleared when the user logs out (in NewsfeedFragment)
 */
public class SharedPreferencesManager {

    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public SharedPreferencesManager(Context context){
        preferences = context.getSharedPreferences("PREFS", 0);
        editor      = preferences.edit();
    }

    public void setId(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public void setisStaff(Boolean isStaff) {
        editor.putBoolean("isStaff", isStaff);
        editor.commit();
    }

    public void setUsername(String username) {
        editor.putString("username", username);
        editor.commit();
    }

    public void setFullname(String fullname) {
        editor.putString("fullname", fullname);
        editor.commit();
    }

    public void setImageurl(String imageurl) {
        editor.putString("imageurl", imageurl);
        editor.commit();
    }

    public void setBio(String bio) {
        editor.putString("bio", bio);
        editor.commit();
    }

    public void setModules(String modules) {
        editor.putString("modules", modules);
        editor.commit();
    }

    public void setEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public void setUserstatus(String userstatus) {
        editor.putString("userstatus", userstatus);
        editor.commit();
    }

    public void setCustomuserstatus(String customuserstatus) {
        editor.putString("customuserstatus", customuserstatus);
        editor.commit();
    }

    public String getString(String id){
        return preferences.getString(id, null);
    }

    public Boolean getisStaff(){
        return preferences.getBoolean("isStaff", false);
    }

    //Called when the user leaves the app (In Newsfeed Fragment)
    public void clearPrefs(){
        editor.clear();
        editor.commit();
    }
}

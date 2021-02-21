package com.devin.astonconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class JournalItem implements Parcelable {
    private String entryMood;
    private String entryLocation;
    private String entryIntensity;
    private String entryTime;
    private String entryWhatHappened;
    private String entryThoughts;
    private String outlookChanged;
    private String changedEntryIntensity;

    public JournalItem() {

    }

    protected JournalItem(Parcel in) {
        entryMood = in.readString();
        entryLocation = in.readString();
        entryIntensity = in.readString();
        entryTime = in.readString();
        entryWhatHappened = in.readString();
        entryThoughts  = in.readString();
        outlookChanged = in.readString();
        changedEntryIntensity = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entryMood);
        dest.writeString(entryLocation);
        dest.writeString(entryIntensity);
        dest.writeString(entryTime);
        dest.writeString(entryWhatHappened);
        dest.writeString(entryThoughts);
        dest.writeString(outlookChanged);
        dest.writeString(changedEntryIntensity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<JournalItem> CREATOR = new Creator<JournalItem>() {
        @Override
        public JournalItem createFromParcel(Parcel in) {
            return new JournalItem(in);
        }

        @Override
        public JournalItem[] newArray(int size) {
            return new JournalItem[size];
        }
    };

    public String getOutlookChanged() {
        return outlookChanged;
    }

    public void setOutlookChanged(String outlookChanged) {
        this.outlookChanged = outlookChanged;
    }

    public String getChangedEntryIntensity() {
        return changedEntryIntensity;
    }

    public void setChangedEntryIntensity(String changedEntryIntensity) {
        this.changedEntryIntensity = changedEntryIntensity;
    }

    public String getEntryWhatHappened() {
        return entryWhatHappened;
    }

    public void setEntryWhatHappened(String entryWhatHappened) {
        this.entryWhatHappened = entryWhatHappened;
    }

    public String getEntryThoughts() {
        return entryThoughts;
    }

    public void setEntryThoughts(String entryThoughts) {
        this.entryThoughts = entryThoughts;
    }

    public String getEntryMood() { return entryMood; }

    public void setEntryMood(String entryMood) { this.entryMood = entryMood; }

    public String getEntryLocation() { return entryLocation; }

    public void setEntryLocation(String entryLocation) { this.entryLocation = entryLocation; }

    public String getEntryIntensity() { return entryIntensity; }

    public void setEntryIntensity(String entryIntensity) { this.entryIntensity = entryIntensity; }

    public String getEntryTime() { return entryTime; }

    public void setEntryTime(String entryTime) { this.entryTime = entryTime; }
}

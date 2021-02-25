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
    private String outlookReflection;
    private String changedEntryIntensity;
    private String journalEntrySubmitted; //the time the journal entry was submitted
    private String entryName;
    private String entryId;

    public JournalItem() {
    }

    protected JournalItem(Parcel in) {
        entryMood = in.readString();
        entryLocation = in.readString();
        entryIntensity = in.readString();
        entryTime = in.readString();
        entryWhatHappened = in.readString();
        entryThoughts  = in.readString();
        outlookReflection = in.readString();
        changedEntryIntensity = in.readString();
        journalEntrySubmitted = in.readString();
        entryName      = in.readString();
        entryId        = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entryMood);
        dest.writeString(entryLocation);
        dest.writeString(entryIntensity);
        dest.writeString(entryTime);
        dest.writeString(entryWhatHappened);
        dest.writeString(entryThoughts);
        dest.writeString(outlookReflection);
        dest.writeString(changedEntryIntensity);
        dest.writeString(journalEntrySubmitted);
        dest.writeString(entryName);
        dest.writeString(entryId);
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

    public String getEntryId() { return entryId; }

    public void setEntryId(String entryId) { this.entryId = entryId; }

    public String getEntryName() { return entryName; }

    public void setEntryName(String entryName) { this.entryName = entryName; }

    public String getJournalEntrySubmitted() { return journalEntrySubmitted; }

    public void setJournalEntrySubmitted(String journalEntrySubmitted) { this.journalEntrySubmitted = journalEntrySubmitted; }

    public String getOutlookReflection() {
        return outlookReflection;
    }

    public void setOutlookReflection(String outlookReflection) {
        this.outlookReflection = outlookReflection;
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

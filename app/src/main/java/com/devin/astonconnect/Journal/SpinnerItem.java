package com.devin.astonconnect.Journal;

public class SpinnerItem {
    private String spinnerItemName;
    private int spinnerImage;

    public SpinnerItem(String spinnerItemName, int spinnerImage){
        this.spinnerImage = spinnerImage;
        this.spinnerItemName = spinnerItemName;
    }

    public String getSpinnerItemName() { return spinnerItemName; }
    public int getSpinnerImage()       { return spinnerImage; }
}

package com.acubeapps.childconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class McqOptions implements Parcelable{
    public int optionSeq;
    public String optionText;

    public McqOptions(int optionSeq, String optionText) {
        this.optionSeq = optionSeq;
        this.optionText = optionText;
    }

    protected McqOptions(Parcel in) {
        optionSeq = in.readInt();
        optionText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(optionSeq);
        dest.writeString(optionText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<McqOptions> CREATOR = new Creator<McqOptions>() {
        @Override
        public McqOptions createFromParcel(Parcel in) {
            return new McqOptions(in);
        }

        @Override
        public McqOptions[] newArray(int size) {
            return new McqOptions[size];
        }
    };
}

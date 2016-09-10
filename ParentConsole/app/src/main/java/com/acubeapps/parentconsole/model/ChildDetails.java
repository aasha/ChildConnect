package com.acubeapps.parentconsole.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aasha.medhi on 9/11/16.
 */
public class ChildDetails implements Parcelable{
    public String childId;
    public String name;
    public String email;
    public String token;
    public String source;
    public String parentUserId;
    public String gcmToken;

    protected ChildDetails(Parcel in) {
        childId = in.readString();
        name = in.readString();
        email = in.readString();
        token = in.readString();
        source = in.readString();
        parentUserId = in.readString();
        gcmToken = in.readString();
    }

    public static final Creator<ChildDetails> CREATOR = new Creator<ChildDetails>() {
        @Override
        public ChildDetails createFromParcel(Parcel in) {
            return new ChildDetails(in);
        }

        @Override
        public ChildDetails[] newArray(int size) {
            return new ChildDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(childId);
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(token);
        parcel.writeString(source);
        parcel.writeString(parentUserId);
        parcel.writeString(gcmToken);
    }
}

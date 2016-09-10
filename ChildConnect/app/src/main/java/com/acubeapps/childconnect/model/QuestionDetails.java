package com.acubeapps.childconnect.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class QuestionDetails implements Parcelable{
    public String questionId;
    public String questionText;
    public String questionType;
    public List<McqOptions> options;
    public int solution;

    public QuestionDetails() {

    }

    protected QuestionDetails(Parcel in) {
        questionId = in.readString();
        questionText = in.readString();
        questionType = in.readString();
        solution = in.readInt();
    }

    public static final Creator<QuestionDetails> CREATOR = new Creator<QuestionDetails>() {
        @Override
        public QuestionDetails createFromParcel(Parcel in) {
            return new QuestionDetails(in);
        }

        @Override
        public QuestionDetails[] newArray(int size) {
            return new QuestionDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(questionId);
        parcel.writeString(questionText);
        parcel.writeString(questionType);
        parcel.writeInt(solution);
    }
}

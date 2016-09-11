package com.acubeapps.parentconsole.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by aasha.medhi on 9/11/16.
 */
public class ChildCourseResult implements Serializable {
    public String childId;
    public String courseId;
    public List<QuestionDetails> questionList;
    public String completionStatus;
    public String percentile;

    protected ChildCourseResult(Parcel in) {
        childId = in.readString();
        courseId = in.readString();
        completionStatus = in.readString();
        percentile = in.readString();
    }
}

package com.acubeapps.parentconsole.model;

import java.io.Serializable;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class McqOptions implements Serializable {

    public int optionSeq;
    public String optionText;

    public McqOptions(int optionSeq, String optionText) {
        this.optionSeq = optionSeq;
        this.optionText = optionText;
    }

    public McqOptions() {
    }

    public int getOptionSeq() {
        return optionSeq;
    }

    public void setOptionSeq(int optionSeq) {
        this.optionSeq = optionSeq;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}

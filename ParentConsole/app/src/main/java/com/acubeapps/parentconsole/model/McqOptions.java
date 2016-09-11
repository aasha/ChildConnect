package com.acubeapps.parentconsole.model;

import java.io.Serializable;

/**
 * Created by aasha.medhi on 9/10/16.
 */
public class McqOptions implements Serializable {

    public String optionSeq;
    public String optionText;

    public McqOptions(String optionSeq, String optionText) {
        this.optionSeq = optionSeq;
        this.optionText = optionText;
    }

    public McqOptions() {
    }

    public String getOptionSeq() {
        return optionSeq;
    }

    public void setOptionSeq(String optionSeq) {
        this.optionSeq = optionSeq;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }
}

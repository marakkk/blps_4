package com.blps.lab4.resourceAdapter;

import jakarta.resource.cci.Record;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JiraResponseRecord implements Record {
    private String issueId;
    private String status;
//    private String issueKey;


    @Override
    public String getRecordName() {
        return "";
    }

    @Override
    public void setRecordName(String s) {

    }

    @Override
    public void setRecordShortDescription(String s) {

    }

    @Override
    public String getRecordShortDescription() {
        return "";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return null;
    }

}
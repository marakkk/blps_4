package com.blps.lab4.resourceAdapter;

import jakarta.resource.cci.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class JiraRequestRecord implements Record {
    private String appName;
    private Long appId;
    private String recordName;
    private String description;

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




package com.college.freelancestartup;

public class HistoryCollabsThree {
    private String posterTitle;
    private String projectTitle;
    private String projectDesc;
    private String collabDate;
    private String skills;
    private String openFor;
    private String collabStatus;
    private String projectID;

    public HistoryCollabsThree(String posterTitle, String projectTitle, String projectDesc, String collabDate, String skills, String openFor, String collabStatus, String projectID) {
        this.posterTitle = posterTitle;
        this.projectTitle = projectTitle;
        this.projectDesc = projectDesc;
        this.collabDate = collabDate;
        this.skills = skills;
        this.openFor = openFor;
        this.collabStatus = collabStatus;
        this.projectID = projectID;
    }

    public String getProjectID() {
        return projectID;
    }

    public String getPosterTitle() {
        return posterTitle;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public String getCollabDate() {
        return collabDate;
    }

    public String getSkills() {
        return skills;
    }

    public String getOpenFor() {
        return openFor;
    }

    public String getCollabStatus() {
        return collabStatus;
    }
}

package com.college.freelancestartup;

public class AllColabsOne {
    private String posterTitle;
    private String projectTitle;
    private String projectDesc;
    private String collabDate;
    private String skills;
    private String openFor;
    private String projectID;
    private int flag;

    public AllColabsOne(String posterTitle, String projectTitle, String projectDesc, String collabDate, String skills, String openFor, String projectID, int flag) {
        this.posterTitle = posterTitle;
        this.projectTitle = projectTitle;
        this.projectDesc = projectDesc;
        this.collabDate = collabDate;
        this.skills = skills;
        this.openFor = openFor;
        this.projectID = projectID;
        this.flag = flag;
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

    public String getProjectID() {
        return projectID;
    }

    public int getFlag() {
        return flag;
    }

}

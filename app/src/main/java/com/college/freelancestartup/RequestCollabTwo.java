package com.college.freelancestartup;

class RequestCollabTwo {
    private String posterTitle;
    private String projectTitle;
    private String projectDesc;
    private String collabDate;
    private String skills;
    private String openFor;

    public RequestCollabTwo(String posterTitle, String projectTitle, String projectDesc, String collabDate, String skills, String openFor) {
        this.posterTitle = posterTitle;
        this.projectTitle = projectTitle;
        this.projectDesc = projectDesc;
        this.collabDate = collabDate;
        this.skills = skills;
        this.openFor = openFor;
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
}
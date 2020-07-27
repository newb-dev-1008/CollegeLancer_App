package com.college.freelancestartup;

class MyCollabsFour {

    private String posterTitle;
    private String projectTitle;
    private String projectDesc;
    private String postedDate;
    private String projectVisible;
    private String numberApplicants;
    private String numberSelectedApplicants;
    private String projectStatus;

    public MyCollabsFour(String posterTitle, String projectTitle, String projectDesc, String postedDate, String projectVisible, String numberApplicants, String numberSelectedApplicants, String projectStatus) {
        this.posterTitle = posterTitle;
        this.projectTitle = projectTitle;
        this.projectDesc = projectDesc;
        this.postedDate = postedDate;
        this.projectVisible = projectVisible;
        this.numberApplicants = numberApplicants;
        this.numberSelectedApplicants = numberSelectedApplicants;
        this.projectStatus = projectStatus;
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

    public String getPostedDate() {
        return postedDate;
    }

    public String getProjectVisible() {
        return projectVisible;
    }

    public String getNumberApplicants() {
        return numberApplicants;
    }

    public String getNumberSelectedApplicants() {
        return numberSelectedApplicants;
    }

    public String getProjectStatus() {
        return projectStatus;
    }
}

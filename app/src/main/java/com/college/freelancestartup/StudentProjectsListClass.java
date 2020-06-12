package com.college.freelancestartup;

public class StudentProjectsListClass {

    private String projectTitle;
    private String projectDescription;
    private int priority;

    public StudentProjectsListClass(){
        // Empty constructor required
    }

    public StudentProjectsListClass(String projectTitle, String projectDescription, int priority){
        this.projectTitle = projectTitle;
        this.projectDescription = projectDescription;
        this.priority = priority;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public int getPriority() {
        return priority;
    }
}

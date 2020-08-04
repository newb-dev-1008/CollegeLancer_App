package com.college.freelancestartup;

public class AvailableCollabsFive {
    private String name, department, skills, noCollabs, noProjects, semester, userEmail, projectID;
    private int flag;

    public AvailableCollabsFive(String name, String department, String skills, String noCollabs, String noProjects, String semester, String userEmail, int flag, String projectID) {
        this.name = name;
        this.department = department;
        this.skills = skills;
        this.noCollabs = noCollabs;
        this.noProjects = noProjects;
        this.semester = semester;
        this.userEmail = userEmail;
        this.flag = flag;
        this.projectID = projectID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getSkills() {
        return skills;
    }

    public String getNoCollabs() {
        return noCollabs;
    }

    public String getNoProjects() {
        return noProjects;
    }

    public String getSemester() {
        return semester;
    }

    public int getFlag() {
        return flag;
    }

    public String getProjectID() {
        return projectID;
    }
}

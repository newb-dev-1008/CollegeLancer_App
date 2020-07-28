package com.college.freelancestartup;

class AvailableCollabsFive {
    private String name, department, skills, noCollabs, noProjects, semester;

    public AvailableCollabsFive(String name, String department, String skills, String noCollabs, String noProjects, String semester) {
        this.name = name;
        this.department = department;
        this.skills = skills;
        this.noCollabs = noCollabs;
        this.noProjects = noProjects;
        this.semester = semester;
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
}

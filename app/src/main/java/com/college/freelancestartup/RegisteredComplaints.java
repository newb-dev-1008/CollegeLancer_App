package com.college.freelancestartup;

class RegisteredComplaints {
    private String complaintTitle;
    private String complaintDate;
    private String complaintStatus;

    public RegisteredComplaints(String complaintTitle, String complaintDate, String complaintStatus) {
        this.complaintTitle = complaintTitle;
        this.complaintDate = complaintDate;
        this.complaintStatus = complaintStatus;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }
}

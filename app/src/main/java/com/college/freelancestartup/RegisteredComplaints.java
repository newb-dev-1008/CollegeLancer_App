package com.college.freelancestartup;

class RegisteredComplaints {
    private String complaintTitle;
    private String complaintDate;
    private String complaintStatus;
    private String complaint;

    public RegisteredComplaints(String complaintTitle, String complaintDate, String complaintStatus, String complaint) {
        this.complaintTitle = complaintTitle;
        this.complaintDate = complaintDate;
        this.complaintStatus = complaintStatus;
        this.complaint = complaint;
    }

    public String getComplaint() {
        return complaint;
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

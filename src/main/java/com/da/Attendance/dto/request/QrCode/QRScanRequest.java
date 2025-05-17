package com.da.Attendance.dto.request.QrCode;

public class QRScanRequest {
    private String qrCode;
    private double latitude;
    private double longitude;
    private String studentId;

    public QRScanRequest() {
    }

    public QRScanRequest(String qrCode, double latitude, double longitude, String studentId) {
        this.qrCode = qrCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.studentId = studentId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}

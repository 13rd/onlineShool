package com.example.demo.requests;

public enum ReportStatus {
    ONCHECK("На проверке"), REJECTED("Отклонено"), VERIFIED("Завершено");



    ReportStatus(String name) {
    }

    @Override
    public String toString() {
        return name();
    }
}

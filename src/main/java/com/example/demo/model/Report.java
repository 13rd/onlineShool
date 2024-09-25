package com.example.demo.model;

import com.example.demo.requests.ReportStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "reports")
public class Report {
    @Id
    private String id;
    private String studentId;
    private String teacherId;
    private String lessonId;
    private String message;
    private ReportStatus reportStatus = ReportStatus.ONCHECK;
}

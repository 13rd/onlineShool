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
    private String fromId;
    private String toId;
    private String lesson;
    private String message;
    private ReportStatus reportStatus;
}

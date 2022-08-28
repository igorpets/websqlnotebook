package com.example.websqlnotebook.service;

import java.sql.ResultSet;

public interface ExportService {
    void exportExcelLoc(ResultSet rs, String filename);
    long getCountRow();
    String getRepositoryPath();
}

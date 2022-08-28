package com.example.websqlnotebook.service;

import java.io.InputStream;
import java.sql.ResultSet;

public interface QueryReportService {
    ResultSet findByAllPrSt(String query);
    void uploadFile(InputStream inputStream);
    void uploadFileMapped(String nameFile);
    void saveTmpBatch();
    void saveTmp();
}

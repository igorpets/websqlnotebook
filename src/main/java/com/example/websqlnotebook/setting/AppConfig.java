package com.example.websqlnotebook.setting;

import com.example.websqlnotebook.export.ResultSetToExcelLoc;
import com.example.websqlnotebook.service.impl.ExportServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${reports.outfile}")
    private String reportFilePath;

    @Bean
    public ResultSetToExcelLoc setToExcelLoc() {
        ResultSetToExcelLoc toExcelLoc = new ResultSetToExcelLoc();
        toExcelLoc.setFilePath(reportFilePath);
        return toExcelLoc;
    }

    @Bean
    public ExportServiceImpl exportService() {
        return new ExportServiceImpl(setToExcelLoc());
    }


}

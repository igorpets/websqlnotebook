package com.example.websqlnotebook.service.impl;

import com.example.websqlnotebook.export.ResultSetToExcelLoc;
import com.example.websqlnotebook.service.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;

@Slf4j
@Service
public class ExportServiceImpl implements ExportService {

    private final ResultSetToExcelLoc resultSetToExcelLoc;
    private long countRow;
    private String repositoryPath;


    @Autowired
    public ExportServiceImpl(ResultSetToExcelLoc resultSetToExcelLoc) {
        this.resultSetToExcelLoc = resultSetToExcelLoc;
        this.repositoryPath = this.resultSetToExcelLoc.getRepositoryPath();
    }

    @Override
    public void exportExcelLoc(ResultSet rs, String filename) {
        log.info("=Start execution of scheduled task");
        try {
            log.info("====start export excel====");
            this.repositoryPath = this.resultSetToExcelLoc.getRepositoryPath();
            log.info("==repositoryPath==" + this.repositoryPath);
            resultSetToExcelLoc.setResultSet(rs);
            //
            resultSetToExcelLoc.writeIntoExcelSXSSFMulti(filename);
            //count rows
            countRow = resultSetToExcelLoc.getiRow();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        log.info("=Complete execution of scheduled task");
    }

    @Override
    public long getCountRow() {
        return countRow;
    }

    @Override
    public String getRepositoryPath() {
        return repositoryPath;
    }

}

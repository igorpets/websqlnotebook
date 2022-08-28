package com.example.websqlnotebook.export;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ResultSetToExcelLoc {
    private static final Logger log = LoggerFactory.getLogger(ResultSetToExcelLoc.class);
    private ResultSet resultSet;
    private int iRow;
    private int iCollSheet;
    private boolean bFlagHeader;
    private String nameSheet;
    private String repositoryPath;

    public ResultSetToExcelLoc() {
        this.iRow = 1;
        this.iCollSheet = 1;
        this.bFlagHeader = false;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
    public void setNameSheet(String nameSheet) {
        this.nameSheet = nameSheet;
    }
    public void setFilePath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }

    public String getNameSheet() {
        if (nameSheet == null || nameSheet.equals("")) {
            nameSheet = "Report" + iCollSheet;
        }
        return nameSheet;
    }

    private int getCountColumn() {
        try {
            return resultSet.getMetaData().getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return -1;
        }
    }

    private List<String> getHeaderNames() {
        List<String> names = new ArrayList<>();
        try {
            int columns = getCountColumn();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 0; i < columns; i++) {
                names.add(metaData.getColumnName(i + 1));
            }
            return names;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public void writeIntoExcelSXSSFMulti(String filename) {
        //
        SXSSFWorkbook workbook = new SXSSFWorkbook(-1);
        workbook.setCompressTempFiles(true);
        Sheet sheet = workbook.createSheet(getNameSheet());
        log.info("Open book and list:=" + getNameSheet());
        //
        iRow = 1;
        Row row;
        Cell cell;
        int columns = getCountColumn();
        List<String> names = getHeaderNames();
        //
        createHeaderSheet(names, sheet);
        //
        try {

            resultSet.setFetchSize(100);
            while (resultSet.next()) {
                row = sheet.createRow(iRow);
                for (int j = 0; j <= columns - 1; j++) {
                    cell = row.createCell(j);
                    //String address = new CellReference(cell).formatAsString();
                    cell.setCellValue(resultSet.getString(j + 1));
                }
                iRow++;
                if (iRow % 2000 == 0) {
                    System.out.println("Записей: " + iRow);
                    ((SXSSFSheet) sheet).flushRows(2000);
                }
                //
                if(iRow % 1000000 == 0) {
                    iRow = 1;
                    iCollSheet++;
                    setNameSheet("Report" + iCollSheet);
                    sheet = workbook.createSheet(getNameSheet());
                    createHeaderSheet(names, sheet);
                    log.info("Новый лист:=" + getNameSheet());
                }
            }
            //
            //String home = System.getProperty("user.home");
            //String file_out = home + "/Downloads/" + filename;
            String file_out = repositoryPath + "\\" + filename;
            FileOutputStream stream = new FileOutputStream(file_out);
            log.info("file_out:=!" + file_out);
            //
            workbook.write(stream);
            stream.close();
            //
            workbook.dispose();
            log.info("==Write to excel ok!");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }

    public void createHeaderSheet(List<String> names, Sheet sheet) {
        Cell cell;
        //Заполняем загаловка таблицы
        int i = 0;
        Row row = sheet.createRow(0);
        for (String headCell : names) {
            cell = row.createCell(i);
            cell.setCellValue(headCell);
            i++;
        }
    }

    public int getiRow() {
        return iRow;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

}

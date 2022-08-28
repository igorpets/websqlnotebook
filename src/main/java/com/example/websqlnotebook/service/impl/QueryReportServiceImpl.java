package com.example.websqlnotebook.service.impl;

import com.example.websqlnotebook.service.QueryReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.HashSet;

@Slf4j
@Service
public class QueryReportServiceImpl implements QueryReportService {
    private final JdbcTemplate jdbcTemplate;
    private HashSet<String> hashSet;
    private static final String SQL_INSERT_TMP = "INSERT INTO INN_TMP " +
            "( " +
            "INN " +
            ") " +
            "VALUES (?) ";

    private static final String SQL_INSERT_NOTMP = "INSERT INTO INN_NOTMP " +
            "( " +
            "INN " +
            ") " +
            "VALUES (?) ";

    public QueryReportServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        hashSet = new HashSet<>();
    }

    @Override
    public ResultSet findByAllPrSt(String query) {
        ResultSetWrappingSqlRowSet rowSet = (ResultSetWrappingSqlRowSet) jdbcTemplate.queryForRowSet(query);
        ResultSet rs = rowSet.getResultSet();
        return rs;
    }

    @Override
    public void uploadFile(InputStream inputStream) {
        hashSet = new HashSet<>();
        try{
            BufferedReader b = new BufferedReader(new InputStreamReader(inputStream));
            String readLine = "";
            int i=0;
            while((readLine = b.readLine()) != null) {
                hashSet.add(readLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public void uploadFileMapped(String nameFile) {
        Path pathFile = Paths.get(nameFile);
        //
        try ( FileChannel fileChannel = (FileChannel.open(pathFile, EnumSet.of(StandardOpenOption.READ)))) {

            MappedByteBuffer mbBuffer = fileChannel.map(
                    FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

            if (mbBuffer != null) {
                String bufferContent = StandardCharsets.UTF_16.decode(mbBuffer).toString();
                log.info(bufferContent);
                mbBuffer.clear();
            }
            //
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

    }

    public void saveTmpBatch() {

        try {
            truncateTmp();
            int[] updateCounts = jdbcTemplate.batchUpdate(SQL_INSERT_NOTMP, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    String inn = null;
                    int j = 0;
                    for (String row : hashSet) {
                        inn = row;
                        j++;
                        if (j == i) {
                            break;
                        }
                    }
                    preparedStatement.setString(1, inn);
                }

                @Override
                public int getBatchSize() {
                    return hashSet.size();
                }
            });
            log.info(updateCounts.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        }
    }

    public void truncateTmp() {
        jdbcTemplate.execute("TRUNCATE TABLE INN_NOTMP");
    }

    public void saveTmp() {
        try {
            truncateTmp();
            for (String row : hashSet) {
                jdbcTemplate.update(SQL_INSERT_NOTMP, row);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex.getMessage(), ex);
        }
    }

}

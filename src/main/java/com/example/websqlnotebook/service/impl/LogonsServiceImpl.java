package com.example.websqlnotebook.service.impl;


import com.example.websqlnotebook.domain.Logons;
import com.example.websqlnotebook.repository.LogonsRepository;
import com.example.websqlnotebook.service.LogonsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class LogonsServiceImpl implements LogonsService {

    private static final String SQL_QUERY_FIND_ALL_25 = "SELECT " +
            "ID, " +
            "DATEIN, " +
            "PROCNAME, " +
            "SCRIPT, " +
            "NROWCOUNT, " +
            "LOGTEXT, " +
            "VUSERNAME, " +
            "VFILEOUT, " +
            "NFLAGOUT " +
            "FROM LOGREPORT " +
            "WHERE 1=1 " +
            "AND DATEIN >= TRUNC(SYSDATE) " +
            "AND ROWNUM <= 25 " +
            "ORDER BY DATEIN DESC ";

    private static final String SQL_QUERY_FIND_SCRIPT = "SELECT " +
            "ID, " +
            "DATEIN, " +
            "PROCNAME, " +
            "SCRIPT, " +
            "NROWCOUNT, " +
            "LOGTEXT, " +
            "VUSERNAME, " +
            "VFILEOUT, " +
            "NFLAGOUT " +
            "FROM .LOGREPORT " +
            "WHERE 1=1 " +
            "AND SCRIPT LIKE '%' || ? || '%' " +
            "AND NFLAGOUT = 1 " +
            "ORDER BY DATEIN DESC ";

    private static final String SQL_INSERT_TMP = "INSERT INTO  LOGREPORT " +
            "( " +
            "ID, " +
            ") " +
            "VALUES (?) ";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private LogonsRepository logonsRepository;

    @Autowired
    public LogonsServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Logons mapRowToLogons(ResultSet row, int rowNum) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return new Logons(row.getLong("ID"),
                Timestamp.valueOf(row.getString("DATEIN")).toLocalDateTime(),
                row.getString("PROCNAME"),
                row.getString("SCRIPT"),
                row.getLong("NROWCOUNT"),
                row.getString("LOGTEXT"),
                row.getString("VUSERNAME"),
                row.getString("VFILEOUT"),
                row.getInt("NFLAGOUT")
        );
    }

    @Override
    public Iterable<Logons> findSort25() {
        //
        return jdbcTemplate.query(SQL_QUERY_FIND_ALL_25,
                this::mapRowToLogons);
    }

    @Override
    public Iterable<Logons> findScript(String script) {
        //
        return jdbcTemplate.query(SQL_QUERY_FIND_SCRIPT,
                this::mapRowToLogons,
                script
        );
    }

    @Override
    public void save(Logons logon) {
        logon.setFlagOut(0);
        logonsRepository.save(logon);
    }

    @Override
    public void update(Logons logon)
    {
        logon.setFlagOut(1);
        logonsRepository.save(logon);
    }

    @Override
    public void clearAll() {
        try {
            logonsRepository.deleteAll();
            //
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public boolean deletedFiles(String directory) {
        //
        try {
            File file = new File(directory);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File fl : files) {
                    if (fl.toString().toLowerCase().endsWith(".xlsx".toLowerCase())) {
                        fl.delete();
                        log.info("Файл: " + fl.getName() + " удален");
                    }
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return false;
        }
    }

}

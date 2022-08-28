package com.example.websqlnotebook.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logreport")
public class Logons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dateIn;

    private String processName;
    private String script;
    private Long _rowCount;
    private String logText;
    private String userName;
    private String fileNameOut;
    private int flagOut;

    public Logons() {
    }

    public Logons(Long id, LocalDateTime dateIn, String processName, String script, Long _rowCount, String logText, String userName, String fileNameOut, int flagOut) {
        this.id = id;
        this.dateIn = dateIn;
        this.processName = processName;
        this.script = script;
        this._rowCount = _rowCount;
        this.logText = logText;
        this.userName = userName;
        this.fileNameOut = fileNameOut;
        this.flagOut = flagOut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateIn() {
        return dateIn;
    }

    public void setDateIn(LocalDateTime dateIn) {
        this.dateIn = dateIn;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Long get_rowCount() {
        return _rowCount;
    }

    public void set_rowCount(Long _rowCount) {
        this._rowCount = _rowCount;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFileNameOut() {
        return fileNameOut;
    }

    public void setFileNameOut(String fileNameOut) {
        this.fileNameOut = fileNameOut;
    }

    public int getFlagOut() {
        return flagOut;
    }

    public void setFlagOut(int flagOut) {
        this.flagOut = flagOut;
    }

}

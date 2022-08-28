package com.example.websqlnotebook.domain;

import javax.persistence.*;

@Entity
@Table(name = "reportlist")
public class ReportsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nameQuery")
    private String nameQuery;

    @Column(name = "description")
    private String description;

    @Column(name = "enableReport")
    private int enableReport;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameQuery() {
        return nameQuery;
    }

    public void setNameQuery(String nameQuery) {
        this.nameQuery = nameQuery;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getEnableReport() {
        return enableReport;
    }

    public void setEnableReport(int enableReport) {
        this.enableReport = enableReport;
    }
}

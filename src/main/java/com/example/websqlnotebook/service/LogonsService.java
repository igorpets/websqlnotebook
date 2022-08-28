package com.example.websqlnotebook.service;

import com.example.websqlnotebook.domain.Logons;

public interface LogonsService {
    Iterable<Logons> findSort25();
    Iterable<Logons> findScript(String script);
    void save(Logons logons);
    void update(Logons logon);
    void clearAll();
    boolean deletedFiles(String directory);
}

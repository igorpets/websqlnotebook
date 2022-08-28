package com.example.websqlnotebook.controller;

import com.example.websqlnotebook.domain.Logons;
import com.example.websqlnotebook.repository.LogonsRepository;
import com.example.websqlnotebook.service.ExportService;
import com.example.websqlnotebook.service.LogonsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.Callable;

@Slf4j
@Controller
public class LogonsController {

    private final LogonsRepository logonsRepository;
    private final LogonsService logonsService;
    private final ExportService exportService;

    @Autowired
    public LogonsController(LogonsRepository logonsRepository, LogonsService logonsService, ExportService exportService) {
        this.logonsRepository = logonsRepository;
        this.logonsService = logonsService;
        this.exportService = exportService;
    }

    @GetMapping("logons")
    public String getLogs(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC, size = 10) Pageable pageable) {

        //По страницам
        Page<Logons> pageLogons = logonsRepository.findByLogAll(pageable);
        int pagesCount = pageLogons.getTotalPages();
        int[] pages = new int[pagesCount];
        for(int i=0; i<pagesCount; i++) pages[i] = i;

        //количество страниц
        model.addAttribute("pages", pages);
        //текущая страница
        model.addAttribute("pageCourante", pageLogons.getNumber());
        //Сам список с банками разбитый на страницы.
        model.addAttribute("logons", pageLogons);
        //model.addAttribute("logons", logonsService.findSort25());
        return "logons/logonsList";
    }

    @PostMapping(value = "/filterLogon")
    public String filterReport(@RequestParam() String filter, Model model) {
        log.info("====filter report:=" + filter);

        if(filter != null && !filter.isEmpty()) {
            model.addAttribute("logons", logonsRepository.findByQuery(filter));
        } else {
            model.addAttribute("logons", logonsService.findSort25());
        }
        return "logons/logonsList";
    }

    @PostMapping(value = "/clearLogons")
    public Callable<String> clearLogon(Model model) {
        log.info("====clear all logons");
        //String home = System.getProperty("user.home");
        //String file_path = home + "/Downloads/";
        String file_path = exportService.getRepositoryPath();
        log.info("==clear file name path:=" + exportService.getRepositoryPath());
        return () -> {
            log.info("thread name:=" + Thread.currentThread().getName());
            //delete files
            logonsService.deletedFiles(file_path);
            //truncate logons
            logonsService.clearAll();
            //
            model.addAttribute("logons", logonsService.findSort25());
            return "logons/logonsList";
        };
    }

}


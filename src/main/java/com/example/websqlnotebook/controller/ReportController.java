package com.example.websqlnotebook.controller;

import com.example.websqlnotebook.domain.ReportsEntity;
import com.example.websqlnotebook.repository.ReportRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Slf4j
@Controller
public class ReportController {

    @Autowired
    private final ReportRepository reportRepository;

    public ReportController(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/reportStart")
    public String reportStart() {
        log.info(" Start report");
        return "detailsStart";
    }


    @GetMapping("/reports")
    public String allReports(Model model) {
        log.info("reports list");
        model.addAttribute("reports", reportRepository.findAll());
        return "reports/listReports";
    }

    @PostMapping(value = "/createReport")
    public String createReport(Model model) {
        log.info("=====create report=====");
        Optional<ReportsEntity> op = Optional.empty();
        model.addAttribute("report", op);
        model.addAttribute("messageReport", "Новый запрос");
        return "/reports/detailsReport";
    }

    @PostMapping(value = "updateReport", params = "id")
    public String updateReport(Long id, Model model) {
        log.info("update report");
        Optional<ReportsEntity> report = reportRepository.findById(id);
        model.addAttribute("report", report);
        return "/reports/detailsReport";
    }


    @PostMapping(value = "/newUpdateReport")
    public String createUpdateReport(ReportsEntity newReport, Model model) {
        log.info("=====create update report=====report:=" + newReport.getNameQuery());
        if( newReport.getNameQuery() != null && newReport.getId() == null) {
            reportRepository.save(newReport);
        }
        if(newReport.getId() != null) {
            reportRepository.save(newReport);
        }
        model.addAttribute("reports", reportRepository.findAll());
        return "/reports/listReports";
    }

    @PostMapping(value = "/filterReport")
    public String filterOpok(@RequestParam() String filter, Model model) {
        log.info("====filter report:=" + filter);

        if(filter != null && !filter.isEmpty()) {
            model.addAttribute("reports", reportRepository.findByNameQuery(filter).orElse(new ReportsEntity()));
        } else {
            model.addAttribute("reports", reportRepository.findAll());
        }
        //
        return "/reports/listReports";
    }

}

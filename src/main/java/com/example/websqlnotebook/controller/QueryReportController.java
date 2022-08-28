package com.example.websqlnotebook.controller;

import com.example.websqlnotebook.domain.Logons;
import com.example.websqlnotebook.domain.QueryEntity;
import com.example.websqlnotebook.domain.ReportsEntity;
import com.example.websqlnotebook.repository.LogonsRepository;
import com.example.websqlnotebook.repository.QueryReportRepository;
import com.example.websqlnotebook.repository.ReportRepository;
import com.example.websqlnotebook.service.ExportService;
import com.example.websqlnotebook.service.LogonsService;
import com.example.websqlnotebook.service.QueryReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Controller
public class QueryReportController {

    private final QueryReportRepository queryRepository;
    private final ReportRepository reportRepository;
    private final QueryReportService queryReportService;
    private final LogonsRepository logonsRepository;
    private final LogonsService logonsService;
    private final ExportService exportService;


    @Autowired
    public QueryReportController(QueryReportRepository queryRepository, ReportRepository reportRepository, QueryReportService queryReportService, LogonsRepository logonsRepository, LogonsService logonsService, ExportService exportService) {
        this.queryRepository = queryRepository;
        this.reportRepository = reportRepository;
        this.queryReportService = queryReportService;
        this.logonsRepository = logonsRepository;
        this.logonsService = logonsService;
        this.exportService = exportService;
    }

    @PostMapping(value = "detailsScript", params = "id")
    public String detailsScript(Long id, Model model) {
        log.info("details Script id:=" + id);
        Optional<ReportsEntity> reportData = queryRepository.findByIdRep(id);
        log.info("details Script idReport:=" + reportData.get().getId());

        Optional<QueryEntity> queryEntity = queryRepository.findByIdReport(id);
        if (queryEntity.isPresent()) {
            log.info("details Script script:=" + queryEntity.get().getQuery());
        } else {
            log.info("details Script script:= null");
        }
        //
        model.addAttribute("query", queryEntity.orElse(new QueryEntity()));
        model.addAttribute("report", reportData.get());
        return "reports/updateScript";
    }

    @PostMapping(value = "/updateQuery/{uid}")
    public String updateReport(@PathVariable(name = "uid") Long uid, QueryEntity queryEntity, Model model) {
        log.info("=====update Query:=" + queryEntity.getQuery());
        log.info("====save Query Report id:=" + uid);
        Optional<ReportsEntity> uploadData = queryRepository.findByIdRep(uid);
        log.info("=====update report script:=" + uploadData.get().getNameQuery());

        queryEntity.setIdReport(uploadData.get().getId());
        //
        if (queryEntity.getQuery() != null && queryEntity.getId() == null) {
            //log.info("=====save new Query script:=" + queryEntity.getQuery() + " ==script id:=" + queryEntity.getId());
            queryRepository.save(queryEntity);
            log.info("=====save new Query Ok");
        }
        if (queryEntity.getQuery() != null && queryEntity.getId() != null) {
            //log.info("=====update Query script:=" + queryEntity.getQuery() + " ==script id:=" + queryEntity.getId());
            queryRepository.save(queryEntity);
            log.info("=====update Query Ok");
        }

        //Модель
        model.addAttribute("reports", reportRepository.findById(uid).orElse(new ReportsEntity()));
        return "reports/listReports";
    }

    @PostMapping(value = "exportExcel", params = "id")
    public Callable<String> exportExcelSS(Long id, HttpServletResponse response, HttpServletRequest request, Model model) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("ddMMyyyy");
        String cuDate = LocalDate.now().format(formatter1);
        return () -> {
            //
            log.info("thread name:=" + Thread.currentThread().getName());
            Optional<ReportsEntity> reportData = queryRepository.findByIdRep(id);
            log.info("export Script report:=" + reportData.get().getNameQuery());
            //
            Optional<QueryEntity> queryEntity = queryRepository.findByIdReport(id);
            //
            if (queryEntity.isPresent()) {
                log.info("details Script script:=" + queryEntity.get().getQuery());
                //fo parse query
                Pattern pattern = Pattern.compile("@TMFL1");
                Matcher matcher = pattern.matcher(queryEntity.get().getQuery());
                boolean flgEmpty = true;
                if (matcher.find()) {
                    //
                    log.info("Найден мак:" + matcher.group(0));
                    model.addAttribute("query", queryEntity.get().getQuery());
                    return "metadop/inFile";
                }

                String filename = "reports_" + cuDate + "_" + Instant.now().getEpochSecond() + ".xlsx";
                //
                //logons
                Logons logons = new Logons();
                setLoginToBd(logons, reportData.get().getNameQuery(), queryEntity.get().getQuery(), filename, request);
                //
                ResultSet rs = queryReportService.findByAllPrSt(queryEntity.get().getQuery());
                long iCountRoe = 0;
                if (rs != null) {
                    log.info("====rs====");
                    exportService.exportExcelLoc(rs, filename);
                    iCountRoe = exportService.getCountRow();
                }
                //
                log.info("export to Excel ok! row all:=" + iCountRoe);
                //log update
                updateLoginToBd(logons, iCountRoe);
                //
            }
            //
            model.addAttribute("logons", logonsService.findSort25());
            return "logons/logonsList";
        };
    }

    private void setLoginToBd(Logons logons, String nameQuery, String script, String filename, HttpServletRequest request) {
        //log save
        logons.setDateIn(LocalDateTime.now());
        logons.setFileNameOut(filename);
        logons.setProcessName(nameQuery);
        logons.setScript(script);
        logons.setLogText("Export data to Excel");
        //
        if(request != null) {
            log.info("user:=" + request.getRemoteUser() + " ip:=" + request.getRemoteAddr());
            String username = "" + request.getRemoteUser() + "\\" + request.getRemoteAddr();
            logons.setUserName(username);
        }
        logonsService.save(logons);
        log.info("===========logon new save ok!");
    }

    private void updateLoginToBd(Logons logons, long iCountRoe) {
        //log
        logons.set_rowCount(iCountRoe);
        logonsService.update(logons);
        //
    }

    @PostMapping(value = "/uploadFile")
    public Callable<String> filterReport(@RequestParam("file") MultipartFile file, @ModelAttribute("query") String query, HttpServletRequest request, SessionStatus sessionStatus, Model model) {
        //
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("ddMMyyyy");
        String cuDate = LocalDate.now().format(formatter1);
        String filename = "reports_" + cuDate + "_" + Instant.now().getEpochSecond() + ".xlsx";
        String strQuery = query.replace("@TMFL1", "INN");
        //
        log.info("====form import file=");
        return () -> {
            //
            if (file != null && !file.isEmpty()) {
                log.info("====import file name:=" + file.getOriginalFilename());
                try {
                    queryReportService.uploadFile(file.getInputStream());
                    //queryReportService.uploadFileMapped(file.getOriginalFilename());
                    //
                    queryReportService.saveTmpBatch();
                    //logons
                    Logons logons = new Logons();
                    setLoginToBd(logons, "Запрос с макросом @TMFL1", strQuery, filename, request);
                    //
                    ResultSet rs = queryReportService.findByAllPrSt(strQuery);
                    long iCountRoe = 0;
                    if(rs != null) {
                        log.info("====rs====");
                        exportService.exportExcelLoc(rs, filename);
                        iCountRoe = exportService.getCountRow();
                    }
                    //
                    log.info("export to Excel ok! row all:=" + iCountRoe);
                    //log update
                    updateLoginToBd(logons, iCountRoe);
                    //
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                //model.addAttribute("logons", logonsService.findSort25());
            }
            sessionStatus.setComplete();
            model.addAttribute("logons", logonsService.findSort25());
            return "logons/logonsList";
        };
    }


    @PostMapping(value = "findLogons", params = "id")
    public String findRepLogons(Long id, Model model) {
        log.info("==find reports fo logons id:=" + id);
        Optional<ReportsEntity> reportData = queryRepository.findByIdRep(id);
        log.info("details Script idReport:=" + reportData.get().getId());

        Optional<QueryEntity> queryEntity = queryRepository.findByIdReport(id);
        if(queryEntity.isPresent()) {
            String query = queryEntity.get().getQuery();
            log.info("find script:=" + query.replace("@TMFL1", "INN"));
            model.addAttribute("logons", logonsRepository.findByTemplateQuery(query.replace("@TMFL1", "INN")));
            return "logons/logonsList";

        } else {
            log.info("find script:= null");
            model.addAttribute("logons", logonsService.findSort25());
            return "logons/logonsList";
        }
    }

}

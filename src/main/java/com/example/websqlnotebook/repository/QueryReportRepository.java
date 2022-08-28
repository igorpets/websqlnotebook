package com.example.websqlnotebook.repository;

import com.example.websqlnotebook.domain.QueryEntity;
import com.example.websqlnotebook.domain.ReportsEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QueryReportRepository extends CrudRepository<QueryEntity, Long> {

    @Query("Select upl from ReportsEntity upl where upl.id = :idd")
    Optional<ReportsEntity> findByIdRep(@Param("idd") Long id);

    Optional<QueryEntity> findByIdReport(Long id);

}

package com.example.websqlnotebook.repository;

import com.example.websqlnotebook.domain.ReportsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReportRepository extends CrudRepository<ReportsEntity, Long> {

    Optional<ReportsEntity> findByNameQuery(String filter);
}

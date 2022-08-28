package com.example.websqlnotebook.repository;

import com.example.websqlnotebook.domain.Logons;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface LogonsRepository extends CrudRepository<Logons, Long> {

    @Query("Select lgs from Logons lgs where lgs.processName like '%' || :idd || '%'")
    Iterable<Logons> findByQuery(@Param("idd") String filter);

    @Query("Select lgs from Logons lgs where lgs.script like '%' || :script || '%' and flagOut = 1")
    Iterable<Logons> findByTemplateQuery(@Param("script") String filter);

    //@Query("Select lgs from Logons lgs Limit 25 ")
    //Iterable<Logons> findByQuery(@Param("idd") String filter);

    @Query("Select lgs from Logons lgs ")
    Page<Logons> findByLogAll(Pageable pageable);

}

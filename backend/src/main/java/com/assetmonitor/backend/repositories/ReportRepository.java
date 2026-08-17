package com.assetmonitor.backend.repositories;

import com.assetmonitor.backend.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByOrderByTimestampDesc();
}
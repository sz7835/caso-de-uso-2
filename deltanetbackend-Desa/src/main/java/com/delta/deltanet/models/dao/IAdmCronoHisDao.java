package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AdmCronogramaHist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAdmCronoHisDao extends JpaRepository<AdmCronogramaHist,Long> {
}

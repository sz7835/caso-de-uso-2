package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.AdmFile;

public interface IAdmFileDao extends JpaRepository<AdmFile, Long> {
	@Query("from AdmFile where name = ?1")
	public AdmFile buscaFile(String nomFile);
}

package com.delta.deltanet.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.delta.deltanet.models.entity.AdmFolder;

public interface IAdmFolderDao extends JpaRepository<AdmFolder, Long> {

	@Query("from AdmFolder where pathfolder = ?1 and nomfolder = ?2")
	public AdmFolder buscaFolder(String path, String carpeta);
	
	@Query("from AdmFolder where pathfolder like %?1%")
	public List<AdmFolder> listaFolderes(String criterio);
}

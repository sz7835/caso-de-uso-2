package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.AdmFolder;

import java.util.List;

public interface IAdmFolderService {
    List<AdmFolder> findAll();
    AdmFolder save(AdmFolder folder);
    AdmFolder findById(Long id);

}

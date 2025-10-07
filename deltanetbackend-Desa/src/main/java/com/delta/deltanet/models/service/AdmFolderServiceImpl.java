package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAdmFolderDao;
import com.delta.deltanet.models.entity.AdmFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdmFolderServiceImpl implements IAdmFolderService{

    @Autowired
    private IAdmFolderDao admFolderDao;

    @Override
    public List<AdmFolder> findAll() {
        return admFolderDao.findAll();
    }

    @Override
    public AdmFolder save(AdmFolder folder) {
        return admFolderDao.save(folder);
    }

    @Override
    public AdmFolder findById(Long id) {
        return admFolderDao.findById(id).orElse(null);
    }

}

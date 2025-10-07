package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IAreasDao;
import com.delta.deltanet.models.entity.Areas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreasServiceImpl {
    @Autowired
    IAreasDao areasDao;

    public List<Areas> getAreasGer(Integer id){
        return areasDao.getAreasGer(id);
    }

    public List<Areas> getAll(){
        return areasDao.findAll();
    }

    public Optional<Areas> getById(Integer id){
        return areasDao.findById(id);
    }

    public Areas save(Areas areas){
        return areasDao.save(areas);
    }
}

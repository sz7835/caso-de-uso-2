package com.delta.deltanet.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IOrgAreasDao;
import com.delta.deltanet.models.entity.OrgAreas;

@Service
public class OrgAreasServiceImpl implements IOrgAreaService {

    @Autowired
    private IOrgAreasDao orgAreasDao;

    @Override
	public List<OrgAreas> findAll() {
		return orgAreasDao.findAll();
	}

    @Override
	public OrgAreas findById(Long id) {
		return orgAreasDao.findById(id).orElse(null);
	}

    @Override
	public OrgAreas save(OrgAreas OrgAreas) {
		return orgAreasDao.save(OrgAreas);
	}

	@Override
	public void delete(Long id) {
		orgAreasDao.deleteById(id);
	}

}
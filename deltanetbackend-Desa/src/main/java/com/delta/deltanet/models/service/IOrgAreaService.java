package com.delta.deltanet.models.service;

import java.util.List;

import com.delta.deltanet.models.entity.OrgAreas;

public interface IOrgAreaService {

    public List<OrgAreas> findAll();
	public OrgAreas findById(Long id);
	public OrgAreas save(OrgAreas area);
	public void delete(Long id);
}

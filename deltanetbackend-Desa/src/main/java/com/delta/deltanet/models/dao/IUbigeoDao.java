package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delta.deltanet.models.entity.Ubigeo;

public interface IUbigeoDao extends JpaRepository<Ubigeo, String> {
}

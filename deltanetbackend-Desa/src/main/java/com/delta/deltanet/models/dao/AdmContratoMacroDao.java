package com.delta.deltanet.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.delta.deltanet.models.entity.AdmContratoMacro;

@Repository
public interface AdmContratoMacroDao
        extends JpaRepository<AdmContratoMacro, Long>, JpaSpecificationExecutor<AdmContratoMacro> {
}

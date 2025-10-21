package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;

import java.util.Optional;

public interface AdmAleMsjCumpleDao {

    Optional<AdmAleMsjCumple> findLatestBySexo(Integer sexo);

    AdmAleMsjCumple upsert(Integer sexo,
                           String saludo,
                           String cuerpo,
                           String footer,
                           byte[] bannerPng,
                           byte[] headerImagePng,
                           String usuario);
}

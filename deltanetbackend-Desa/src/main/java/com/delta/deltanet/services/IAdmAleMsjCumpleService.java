package com.delta.deltanet.services;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;

public interface IAdmAleMsjCumpleService {

    AdmAleMsjCumple findLatestBySexo(Integer sexo);

    /**
     * Partial upsert (PATCH-style): only non-null params overwrite existing values.
     */
    AdmAleMsjCumple upsertPlantilla(
        Integer sexo,
        String descripcion,
        String ccDefecto,
        String saludoBreve,
        byte[] headerBanner,
        byte[] headerImage,
        String footer,
        String createUser
    );
}

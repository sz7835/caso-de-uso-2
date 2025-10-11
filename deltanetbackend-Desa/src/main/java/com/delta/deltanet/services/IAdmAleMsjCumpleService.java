package com.delta.deltanet.services;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;

public interface IAdmAleMsjCumpleService {
    /**
     * Returns the latest active birthday template for the given sexo ID.
     * @param sexoId value matching id_sexo in DB (e.g., 1, 2)
     * @return AdmAleMsjCumple or null if none
     */
    AdmAleMsjCumple findLatestBySexo(Integer sexoId);
}

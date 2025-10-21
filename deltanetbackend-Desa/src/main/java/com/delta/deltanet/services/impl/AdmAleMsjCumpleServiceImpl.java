package com.delta.deltanet.services.impl;

import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import com.delta.deltanet.models.repository.AdmAleMsjCumpleRepository;
import com.delta.deltanet.services.IAdmAleMsjCumpleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional // write-enabled by default
public class AdmAleMsjCumpleServiceImpl implements IAdmAleMsjCumpleService {

    private final AdmAleMsjCumpleRepository repo;

    public AdmAleMsjCumpleServiceImpl(AdmAleMsjCumpleRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public AdmAleMsjCumple findLatestBySexo(Integer sexo) {
        return repo.findTopByIdSexoOrderByIdDesc(sexo).orElse(null);
    }

    @Override
    public AdmAleMsjCumple upsertPlantilla(
            Integer sexo,
            String descripcion,
            String ccDefecto,
            String saludoBreve,
            byte[] headerBanner,
            byte[] headerImage,
            String footer,
            String createUser
    ) {
        // Fetch latest by sexo or start a new entity
        AdmAleMsjCumple e = repo.findTopByIdSexoOrderByIdDesc(sexo)
                .orElseGet(AdmAleMsjCumple::new);

        // Required
        e.setIdSexo(sexo);

        // PATCH-style overwrite: only set when provided (avoid null-wiping)
        if (descripcion != null)   e.setDescripcion(descripcion);
        if (ccDefecto != null)     e.setListaCorrDefecto(ccDefecto);
        if (saludoBreve != null)   e.setMsgBody(saludoBreve);
        if (footer != null)        e.setMsgFooter(footer);
        if (headerBanner != null)  e.setMsgHeaderBanner(headerBanner);
        if (headerImage != null)   e.setMsgHeaderImage(headerImage);

        if (e.getCreateUser() == null) {
            e.setCreateUser(createUser != null ? createUser : "api");
        }

        // Persist to DB
        return repo.save(e);
    }
}

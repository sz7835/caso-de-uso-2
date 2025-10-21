package com.delta.deltanet.models.dao.impl;

import com.delta.deltanet.models.dao.AdmAleMsjCumpleDao;
import com.delta.deltanet.models.entity.AdmAleMsjCumple;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class AdmAleMsjCumpleDaoImpl implements AdmAleMsjCumpleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public Optional<AdmAleMsjCumple> findLatestBySexo(Integer sexo) {
        var list = em.createQuery(
                "SELECT a FROM AdmAleMsjCumple a WHERE a.idSexo = :sexo ORDER BY a.id DESC",
                AdmAleMsjCumple.class)
            .setParameter("sexo", sexo)
            .setMaxResults(1)
            .getResultList();

        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    @Transactional
    public AdmAleMsjCumple upsert(Integer sexo,
                                  String saludo,
                                  String cuerpo,
                                  String footer,
                                  byte[] bannerPng,
                                  byte[] headerImagePng,
                                  String usuario) {

        AdmAleMsjCumple e = findLatestBySexo(sexo).orElseGet(AdmAleMsjCumple::new);
        e.setIdSexo(sexo);

        if (hasText(saludo)) e.setDescripcion(limit(saludo, 255));
        if (hasText(cuerpo)) e.setMsgBody(limit(cuerpo, 1000));
        if (hasText(footer)) e.setMsgFooter(limit(footer, 250));

        if (bannerPng != null && bannerPng.length > 0) e.setMsgHeaderBanner(bannerPng);
        if (headerImagePng != null && headerImagePng.length > 0) e.setMsgHeaderImage(headerImagePng);

        if (e.getId() == null) {
            e.setCreateUser(hasText(usuario) ? usuario.trim() : "api");
            em.persist(e);
        } else {
            e = em.merge(e);
        }
        em.flush();
        return e;
    }

    /* ----------------------------- helpers ------------------------------ */

    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String limit(String s, int max) {
        String t = s.trim();
        return t.length() <= max ? t : t.substring(0, max);
    }
}

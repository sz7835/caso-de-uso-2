package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.PostulanteSkillDao;
import com.delta.deltanet.models.entity.PostulanteSkill;

@Service
public class PostulanteSkillServiceImpl implements PostulanteSkillService {
    @Autowired
    private PostulanteSkillDao postulanteSkillDao;

    @Override
    public boolean existsDescripcionActivo(String descripcion, Long exceptId) {
        if (descripcion == null) return false;
        String descripcionTrim = descripcion.trim();
        long count = postulanteSkillDao.countByDescripcionActivo(descripcionTrim, exceptId);
        return count > 0;
    }

    @Override
    public List<PostulanteSkill> buscarPorNombreYEstado(String descripcion, Integer estado) {
        return postulanteSkillDao.findByNombreAndEstado(descripcion, estado);
    }

    @Override
    public PostulanteSkill buscaId(Long idPer) {
        return postulanteSkillDao.findById(idPer).orElse(null);
    }

    @Override
    public List<PostulanteSkill> findAll() {
        return postulanteSkillDao.findAll();
    }

    @Override
    public PostulanteSkill findById(Long id) {
        return postulanteSkillDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PostulanteSkill save(PostulanteSkill postulanteSkill) {
        return postulanteSkillDao.save(postulanteSkill);
    }

    @Override
    @Transactional
    public PostulanteSkill update(PostulanteSkill postulanteSkill) {
        if (!postulanteSkillDao.existsById(postulanteSkill.getId())) {
            throw new RuntimeException("No se encontró skill de postulante con ID: " + postulanteSkill.getId());
        }
        return postulanteSkillDao.save(postulanteSkill);
    }

    @Override
    @Transactional
    public PostulanteSkill changeEstado(Long id, Integer nuevoEstado, String username) {
        PostulanteSkill postulanteSkill = postulanteSkillDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró skill de postulante con ID: " + id));

        postulanteSkill.setUpdateUser(username);
        postulanteSkill.setUpdateDate(new Date());
        postulanteSkill.setEstado(nuevoEstado);

        return postulanteSkillDao.save(postulanteSkill);
    }
}

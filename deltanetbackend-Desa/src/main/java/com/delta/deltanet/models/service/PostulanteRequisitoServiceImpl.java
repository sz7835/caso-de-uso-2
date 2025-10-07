package com.delta.deltanet.models.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.PostulanteRequisitoDao;
import com.delta.deltanet.models.entity.PostulanteRequisito;

@Service
public class PostulanteRequisitoServiceImpl implements PostulanteRequisitoService {
    @Autowired
    private PostulanteRequisitoDao postulanteRequisitoDao;

    @Override
    public boolean existsDescripcionActivo(String descripcion, Long exceptId) {
        if (descripcion == null) return false;
        String descripcionTrim = descripcion.trim();
        long count = postulanteRequisitoDao.countByDescripcionActivo(descripcionTrim, exceptId);
        return count > 0;
    }

    @Override
    public List<PostulanteRequisito> buscarPorNombreYEstado(String descripcion, Integer estado) {
        return postulanteRequisitoDao.findByNombreAndEstado(descripcion, estado);
    }

    @Override
    public PostulanteRequisito buscaId(Long idPer) {
        return postulanteRequisitoDao.findById(idPer).orElse(null);
    }

    @Override
    public List<PostulanteRequisito> findAll() {
        return postulanteRequisitoDao.findAll();
    }

    @Override
    public PostulanteRequisito findById(Long id) {
        return postulanteRequisitoDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public PostulanteRequisito save(PostulanteRequisito postulanteRequisito) {
        return postulanteRequisitoDao.save(postulanteRequisito);
    }

    @Override
    @Transactional
    public PostulanteRequisito update(PostulanteRequisito postulanteRequisito) {
        if (!postulanteRequisitoDao.existsById(postulanteRequisito.getId())) {
            throw new RuntimeException("No se encontró requisito de postulante con ID: " + postulanteRequisito.getId());
        }
        return postulanteRequisitoDao.save(postulanteRequisito);
    }

    @Override
    @Transactional
    public PostulanteRequisito changeEstado(Long id, Integer nuevoEstado, String username) {
        PostulanteRequisito postulanteRequisito = postulanteRequisitoDao.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encontró requisito de postulante con ID: " + id));

        postulanteRequisito.setUpdateUser(username);
        postulanteRequisito.setUpdateDate(new Date());
        postulanteRequisito.setEstado(nuevoEstado);

        return postulanteRequisitoDao.save(postulanteRequisito);
    }
}

package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.*;
import com.delta.deltanet.models.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillSeviceImpl implements ISkillService{
    @Autowired
    private ISkillBlandaClasDao blandaClasDao;

    @Autowired
    private ISkillBlandaDao blandaDao;

    @Autowired
    private ISkillDuraClasDao duraClasDao;

    @Autowired
    private ISkillDuraDao duraDao;

    @Autowired
    private ISkillDuraFWDao duraFWDao;

    @Autowired
    private IBlandaPersDao blandaPersDao;

    @Autowired
    private IDuraPersDao duraPersDao;

    /*------ SkillBlandaClas ------*/
    @Override
    public boolean existsDescripcionActivoBlandaClas(String descripcion, Long idExcluir) {
        if (descripcion == null) return false;
        String descTrim = descripcion.trim();
        Long count = blandaClasDao.countByDescripcionActivo(descTrim);
        if (idExcluir != null) {
            SkillBlandaClas actual = blandaClasDao.findById(idExcluir).orElse(null);
            if (actual != null && actual.getDescripcion() != null && actual.getDescripcion().trim().equalsIgnoreCase(descTrim) && actual.getEstado() == 1) {
                // Si el único registro duplicado es el mismo que estamos actualizando, no es duplicado
                return count > 1;
            }
        }
        return count > 0;
    }

    @Override
    public SkillBlandaClas findBlandaClasById(Long id) {
        return blandaClasDao.findById(id).orElse(null);
    }

    @Override
    public void saveBlandaClas(SkillBlandaClas skillBlandaClas) {
        blandaClasDao.save(skillBlandaClas);
    }

    /*------ SkillBlanda ------*/
    @Override
    public SkillBlanda findBlandaById(Long id) {
        return blandaDao.findById(id).orElse(null);
    }

    @Override
    public void saveBlanda(SkillBlanda skillBlanda) {
        blandaDao.save(skillBlanda);
    }

    @Override
    public boolean existsBlandaActivoByDescripcionAndIdClas(String descripcion, Long idClas, Long idExcluido) {
        List<SkillBlanda> lista = blandaDao.findByDescripcionAndIdClasAndEstado(descripcion, idClas, 1);
        if (idExcluido != null) {
            return lista.stream().anyMatch(b -> !b.getId().equals(idExcluido));
        } else {
            return !lista.isEmpty();
        }
    }

    /*------ SkillDuraClas ------*/
    @Override
    public SkillDuraClas findDuraClasById(Long id) {
        return duraClasDao.findById(id).orElse(null);
    }

    @Override
    public void saveDuraClas(SkillDuraClas skillDuraClas) {
        duraClasDao.save(skillDuraClas);
    }

    @Override
    public boolean existsDuraClasActivoByDescripcion(String descripcion, Long idExcluir) {
        List<SkillDuraClas> lista = duraClasDao.findByDescripcionAndEstado(descripcion, 1);
        if (idExcluir != null) {
            return lista.stream().anyMatch(b -> !b.getId().equals(idExcluir));
        } else {
            return !lista.isEmpty();
        }
    }

    /*------ SkillDura ------*/
    @Override
    public SkillDura findDuraById(Long id) {
        return duraDao.findById(id).orElse(null);
    }

    @Override
    public void saveDura(SkillDura skillDura) {
        duraDao.save(skillDura);
    }

    @Override
    public boolean existsDuraActivoByDescripcionAndIdClas(String descripcion, Long idClas, Long idExcluido) {
        List<SkillDura> lista = duraDao.findByDescripcionAndIdClasAndEstado(descripcion, idClas, 1);
        if (idExcluido != null) {
            return lista.stream().anyMatch(b -> !b.getId().equals(idExcluido));
        } else {
            return !lista.isEmpty();
        }
    }
    /*------ SkillDuraFrameWork ------*/
    @Override
    public SkillDuraFrameWork findDuraFWById(Long id) {
        return duraFWDao.findById(id).orElse(null);
    }

    @Override
    public void saveDuraFW(SkillDuraFrameWork duraFrameWork) {
        duraFWDao.save(duraFrameWork);
    }

    @Override
    public boolean existsDuraFWActivoByDescripcion(String descripcion, Long idExcluir) {
        List<SkillDuraFrameWork> lista = duraFWDao.findByDescripcionAndEstado(descripcion, 1);
        if (idExcluir != null) {
            return lista.stream().anyMatch(b -> !b.getId().equals(idExcluir));
        } else {
            return !lista.isEmpty();
        }
    }
    /*------ SkillBlandaPers ------*/
    @Override
    public void saveBlandaPers(SkillBlandaPers blandaPers) {
        blandaPersDao.save(blandaPers);
    }

    @Override
    public void deleteBlandaPers(Long idHab) {
        blandaPersDao.deleteById(idHab);
    }

    @Override
    public List<Object> listaHabBlandas(Long idPer) {
        return blandaPersDao.listaHabBlandas(idPer);
    }

    /*------ SkillDuraPers ------*/
    @Override
    public void saveDuraPers(SkillDuraPers duraPers) {
        duraPersDao.save(duraPers);
    }

    @Override
    public void deleteDuraPers(Long idHab) {
        duraPersDao.deleteById(idHab);
    }

    @Override
    public List<Object> listaHabDuras(Long idPer) {
        return duraPersDao.listaHabDuras(idPer);
    }

}

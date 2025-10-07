package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.*;

import java.util.List;

public interface ISkillService {
    boolean existsDuraFWActivoByDescripcion(String descripcion, Long idExcluir);
    boolean existsDuraActivoByDescripcionAndIdClas(String descripcion, Long idClas, Long idExcluido);
    boolean existsDuraClasActivoByDescripcion(String descripcion, Long idExcluir);
    boolean existsBlandaActivoByDescripcionAndIdClas(String descripcion, Long idClas, Long idExcluido);
    /*------ SkillBlandaClas ------*/
    public SkillBlandaClas findBlandaClasById(Long id);
    public void saveBlandaClas(SkillBlandaClas skillBlandaClas);
    boolean existsDescripcionActivoBlandaClas(String descripcion, Long idExcluir);

    /*------ SkillBlanda ------*/
    public SkillBlanda findBlandaById(Long id);
    public void saveBlanda(SkillBlanda skillBlanda);

    /*------ SkillDuraClas ------*/
    public SkillDuraClas findDuraClasById(Long id);
    public void saveDuraClas(SkillDuraClas skillDuraClas);

    /*------ SkillDura ------*/
    public SkillDura findDuraById(Long id);
    public void saveDura(SkillDura skillDura);

    /*------ SkillDuraFrameWork ------*/
    public SkillDuraFrameWork findDuraFWById(Long id);
    public void saveDuraFW(SkillDuraFrameWork duraFrameWork);

    /*------ SkillBlandaPers ------*/
    public void saveBlandaPers(SkillBlandaPers blandaPers);
    public void deleteBlandaPers(Long idHab);
    public List<Object> listaHabBlandas(Long idPer);

    /*------ SkillDuraaPers ------*/
    public void saveDuraPers(SkillDuraPers duraPers);
    public void deleteDuraPers(Long idHab);
    public List<Object> listaHabDuras(Long idPer);
}

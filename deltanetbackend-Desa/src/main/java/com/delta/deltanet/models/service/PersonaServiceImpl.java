package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IDireccionDao;
import com.delta.deltanet.models.dao.IEmailDao;
import com.delta.deltanet.models.dao.IPersonaDao;
import com.delta.deltanet.models.dao.IRelacionDao;
import com.delta.deltanet.models.dao.ITelefonoDao;
import com.delta.deltanet.models.dao.ITipoRelacionDao;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.PersonaSearchDTO;
import com.delta.deltanet.models.entity.TipoRelacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaServiceImpl implements IPersonaService {

    @Autowired
    public IPersonaDao personaDao;

    @Override
    public List<Persona> listaPersonasNat(Long idTipoRel) {
        return personaDao.listaPersonasNat(idTipoRel);
    }

    @Override
    public List<Persona> listaPersonasNatStaff(Long idTipoRel) {
        return personaDao.listaPersonasNatStaff(idTipoRel);
    }

    @Override
    public Persona save(Persona persona) {
        return personaDao.save(persona);
    }

    @Override
    public List<Object> findByPersonaNat() {
        return personaDao.findByPersonaNat();
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, int estado) {
        return personaDao.findByPersonaNat(idTipoPer, idTipoDoc, nrodoc, descrip, estado);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer) {
        return personaDao.findByPersonaNat(idTipoPer);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc) {
        return personaDao.findByPersonaNat(idTipoPer, idTipoDoc);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, String nrodoc) {
        return personaDao.findByPersonaNat(idTipoPer, nrodoc);
    }

    @Override
    public List<Object> findByPersonaNat2(Long idTipoPer, String descrip) {
        return personaDao.findByPersonaNat2(idTipoPer, descrip);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, int estado) {
        return personaDao.findByPersonaNat(idTipoPer, estado);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc) {
        return personaDao.findByPersonaNat(idTipoPer, idTipoDoc, nrodoc);
    }

    @Override
    public List<Object> findByPersonaNat2(Long idTipoPer, Long idTipoDoc, String descrip) {
        return personaDao.findByPersonaNat2(idTipoPer, idTipoDoc, descrip);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, int estado) {
        return personaDao.findByPersonaNat(idTipoPer, idTipoDoc, estado);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip) {
        return personaDao.findByPersonaNat(idTipoPer, idTipoDoc, nrodoc, descrip);
    }

    @Override
    public List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, int estado) {
        return personaDao.findByPersonaNat(idTipoPer, idTipoDoc, nrodoc, estado);
    }

    @Override
    public List<Object> finByPersonaJur() {
        return personaDao.findByPersonaJur();
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, int estado, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, idTipoDoc, nrodoc, descrip, estado, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJurD(Long idTipoPer, Long idTipoDoc, String document) {
        return personaDao.findByPersonaJurD(idTipoPer, idTipoDoc, document);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, idTipoDoc, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, String nrodoc, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, nrodoc, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur2(Long idTipoPer, String descrip, String tipoCli) {
        return personaDao.findByPersonaJur2(idTipoPer, descrip, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, int estado, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, estado, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, idTipoDoc, nrodoc, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur2(Long idTipoPer, Long idTipoDoc, String descrip, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, idTipoDoc, descrip, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, int estado, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, idTipoDoc, estado, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, idTipoDoc, nrodoc, descrip, tipoCli);
    }

    @Override
    public List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, int estado, String tipoCli) {
        return personaDao.findByPersonaJur(idTipoPer, idTipoDoc, nrodoc, estado, tipoCli);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                        Long tipoRel) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Integer estado) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,estado);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                        Long tipoRel, Long tipoDoc) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,tipoDoc);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                        Long tipoRel, Long tipoDoc, String nroDoc) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,tipoDoc,
                nroDoc);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                        Long tipoRel, Long tipoDoc, String nroDoc,
                                        Integer estado) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,tipoDoc,
                nroDoc,estado);
    }

    @Override
    public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                        Long tipoRel, Long tipoDoc, String nroDoc,
                                        Integer estado, String nombre) {
        return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,tipoDoc,
                nroDoc,estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
    //                                     Long tipoRel, Long tipoDoc, String nroDoc,
    //                                     Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,tipoDoc,
    //             nroDoc,estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasN(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,tipoDoc,
    //             nroDoc,estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir) {
        return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir);
    }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer) {
        return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir, tipoPer);
    }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel) {
        return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir,tipoPer,tipoRel);
    }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Integer estado) {
        return personaDao.findByPersonasJ(tipoTel,tipoCor,tipoDir,tipoPer,tipoRel,estado);
    }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Long tipoDoc) {
        return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir,tipoPer,tipoRel,tipoDoc);
    }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Long tipoDoc, String nroDoc) {
        return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir,tipoPer,tipoRel,tipoDoc,nroDoc);
    }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado) {
        return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir,tipoPer,tipoRel,tipoDoc,nroDoc,estado);
    }

    @Override
    public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre) {
        return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir,tipoPer,tipoRel,tipoDoc,nroDoc,estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir,tipoPer,tipoRel,tipoDoc,nroDoc,estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasJ(tipoTel, tipoCor, tipoDir,tipoPer,tipoRel,tipoDoc,nroDoc,estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel) {
        return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel);
    }

    @Override
    public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, String nrodoc) {
        return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel, nrodoc);
    }

    @Override
    public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc) {
        return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc);
    }

    @Override
    public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, Integer estado) {
        return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc, estado);
    }

    @Override
    public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc) {
        return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc);
    }

    @Override
    public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado) {
        return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado);
    }

    @Override
    public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre) {
        return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasA(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel) {
        return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel);
    }

    @Override
    public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, String nrodoc) {
        return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel, nrodoc);
    }

    @Override
    public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc) {
        return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc);
    }

    @Override
    public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, Integer estado) {
        return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,estado);
    }

    @Override
    public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc) {
        return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc);
    }

    @Override
    public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado) {
        return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado);
    }

    @Override
    public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre) {
        return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasA1(tipoTel, tipoCor, tipoDir,tipoRel,tipoDoc,nroDoc,estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc) {
        return personaDao.findByPersonasB(tipoTel, tipoCor, tipoDir,tipoDoc);
    }

    @Override
    public List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc) {
        return personaDao.findByPersonasB(tipoTel, tipoCor, tipoDir,tipoDoc,nroDoc);
    }

    @Override
    public List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado) {
        return personaDao.findByPersonasB(tipoTel, tipoCor, tipoDir,tipoDoc,nroDoc,estado);
    }

    @Override
    public List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado, String nombre) {
        return personaDao.findByPersonasB(tipoTel, tipoCor, tipoDir,tipoDoc,nroDoc,estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasB(tipoTel, tipoCor, tipoDir,tipoDoc,nroDoc,estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasB(tipoTel, tipoCor, tipoDir,tipoDoc,nroDoc,estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc) {
        return personaDao.findByPersonasB1(tipoTel, tipoCor, tipoDir, tipoDoc);
    }

    @Override
    public List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc) {
        return personaDao.findByPersonasB1(tipoTel, tipoCor, tipoDir, tipoDoc, nroDoc);
    }

    @Override
    public List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado) {
        return personaDao.findByPersonasB1(tipoTel, tipoCor, tipoDir, tipoDoc, nroDoc, estado);
    }

    @Override
    public List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado, String nombre) {
        return personaDao.findByPersonasB1(tipoTel, tipoCor, tipoDir, tipoDoc, nroDoc, estado, nombre);
    }

    // @Override
    // public List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasB1(tipoTel, tipoCor, tipoDir, tipoDoc, nroDoc, estado, nombre, idCto, idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoDoc, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasB1(tipoTel, tipoCor, tipoDir, tipoDoc, nroDoc, estado, nombre, idCto);
    // }

    @Override
    public List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc) {
        return personaDao.findByPersonasC(tipoTel, tipoCor, tipoDir, nroDoc);
    }

    @Override
    public List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado) {
        return personaDao.findByPersonasC(tipoTel, tipoCor, tipoDir, nroDoc,estado);
    }

    @Override
    public List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado, String nombre) {
        return personaDao.findByPersonasC(tipoTel, tipoCor, tipoDir, nroDoc,estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasC(tipoTel, tipoCor, tipoDir, nroDoc,estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasC(tipoTel, tipoCor, tipoDir, nroDoc,estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc) {
        return personaDao.findByPersonasC1(tipoTel, tipoCor, tipoDir, nroDoc);
    }

    @Override
    public List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado) {
        return personaDao.findByPersonasC1(tipoTel, tipoCor, tipoDir, nroDoc, estado);
    }

    @Override
    public List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado, String nombre) {
        return personaDao.findByPersonasC1(tipoTel, tipoCor, tipoDir, nroDoc, estado, nombre);
    }

    // @Override
    // public List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasC1(tipoTel, tipoCor, tipoDir, nroDoc, estado, nombre, idCto, idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir, String nroDoc, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasC1(tipoTel, tipoCor, tipoDir, nroDoc, estado, nombre, idCto);
    // }

    @Override
    public List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado) {
        return personaDao.findByPersonasD(tipoTel, tipoCor, tipoDir, estado);
    }

    @Override
    public List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado, String nombre) {
        return personaDao.findByPersonasD(tipoTel, tipoCor, tipoDir, estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasD(tipoTel, tipoCor, tipoDir, estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasD(tipoTel, tipoCor, tipoDir, estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado) {
        return personaDao.findByPersonasD1(tipoTel, tipoCor, tipoDir, estado);
    }

    @Override
    public List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado, String nombre) {
        return personaDao.findByPersonasD1(tipoTel, tipoCor, tipoDir, estado,nombre);
    }

    // @Override
    // public List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasD1(tipoTel, tipoCor, tipoDir, estado,nombre,idCto,idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir, Integer estado, String nombre, Long idCto) {
    //     return personaDao.findByPersonasD1(tipoTel, tipoCor, tipoDir, estado,nombre,idCto);
    // }

    @Override
    public List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir, String nombre) {
        return personaDao.findByPersonasE(tipoTel, tipoCor, tipoDir, nombre);
    }

    // @Override
    // public List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasE(tipoTel, tipoCor, tipoDir, nombre, idCto, idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir, String nombre, Long idCto) {
    //     return personaDao.findByPersonasE(tipoTel, tipoCor, tipoDir, nombre, idCto);
    // }

    @Override
    public List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir, String nombre) {
        return personaDao.findByPersonasE1(tipoTel, tipoCor, tipoDir, nombre);
    }

    // @Override
    // public List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir, String nombre, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasE1(tipoTel, tipoCor, tipoDir, nombre, idCto, idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir, String nombre, Long idCto) {
    //     return personaDao.findByPersonasE1(tipoTel, tipoCor, tipoDir, nombre, idCto);
    // }

    // @Override
    // public List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasF(tipoTel, tipoCor, tipoDir, idCto, idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto, Long idMotivo, Integer estado) {
    //     return personaDao.findByPersonasF(tipoTel, tipoCor, tipoDir, idCto, idMotivo, estado);
    // }

    // @Override
    // public List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto) {
    //     return personaDao.findByPersonasF(tipoTel, tipoCor, tipoDir, idCto);
    // }

    // @Override
    // public List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto, Integer estado) {
    //     return personaDao.findByPersonasF(tipoTel, tipoCor, tipoDir, idCto, estado);
    // }

    // @Override
    // public List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto, Long idMotivo) {
    //     return personaDao.findByPersonasF1(tipoTel, tipoCor, tipoDir, idCto, idMotivo);
    // }

    // @Override
    // public List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto, Long idMotivo, Integer estado) {
    //     return personaDao.findByPersonasF1(tipoTel, tipoCor, tipoDir, idCto, idMotivo, estado);
    // }

    // @Override
    // public List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto) {
    //     return personaDao.findByPersonasF1(tipoTel, tipoCor, tipoDir, idCto);
    // }

    // @Override
    // public List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir, Long idCto, Integer estado) {
    //     return personaDao.findByPersonasF1(tipoTel, tipoCor, tipoDir, idCto, estado);
    // }

    @Override
    public Persona buscarId(Long idPer) {
        return personaDao.buscarId(idPer);
    }

    @Override
    public List<Object> findPersonaRelacion(Long idRo) {
        return personaDao.findPersonaRelacion(idRo);
    }
    
    @Override
    public List<Persona> findAll() {
        return personaDao.findAll();
    }

    @Autowired
    private IRelacionDao relacionDao;

    @Autowired
    private ITipoRelacionDao tipoRelacionDao;

    // @Override
    // public List<Object> listaRelaciones(Long idPer) {
    //     return relacionDao.listaRelacionesFilter(idPer);
    // }

    @Override
    public List<Object> listaRelaciones(Long idPer) {
        List<Object> relaciones = relacionDao.listaRelacionesFilter(idPer);
        for (int i = 0; i < relaciones.size(); i++) {
            Object[] datos = (Object[]) relaciones.get(i);
            Long idArea = datos[2] != null ? (Long) datos[2] : null;

            if (idArea != null) {
                TipoRelacion tipoRelacion = tipoRelacionDao.findById(idArea).orElse(null);
                if (tipoRelacion != null) {
                    datos = Arrays.copyOf(datos, datos.length + 1);
                    datos[datos.length - 1] = new Object[]{tipoRelacion.getIdTipoRel(), tipoRelacion.getDescrip(), "", tipoRelacion.getEstado()};
                    relaciones.set(i, datos);
                }
            }
        }
        return relaciones;
    }

    @Autowired
    private ITelefonoDao telefonoDao;

    @Override
    public List<Object> listaTelefonos(Long idPersona) {
        return telefonoDao.findAllByPerAndEstadoOrderbyTipo(idPersona);
    }

    @Autowired
    private IEmailDao emailDao;

    @Override
    public List<Object> listaEmails(Long id) {
        return emailDao.findAllPerWithEstado(id);
    }

    @Autowired
    private IDireccionDao direccionDao;

    @Override
    public List<Object> listaDirecciones(Long idPer) {
        return direccionDao.findAllPerWithEstado(idPer);
    }

	@Override
	public List<PersonaSearchDTO> searchByNombreOrDoc(String nombre, String documento) {
		List<Persona> personas = personaDao.findAll();
		List<PersonaSearchDTO> lstPersonas = new ArrayList<PersonaSearchDTO>();

		if(nombre == null && documento == null) {
			return personas.stream().map(p -> p.toPersonaSearchDto()).collect(Collectors.toList());
		}

		for(Persona p : personas) {
			String pNombre = "";
			String pDocumento = p.getDocumento();
			if(p.getPerNat() != null) {
				pNombre = p.getPerNat().getNombre() + " " + p.getPerNat().getApePaterno() + " " + p.getPerNat().getApeMaterno();
			}else {
				pNombre = p.getPerJur().getRazonSocial() + " " + p.getPerJur().getRazonComercial();
			}

            if(nombre != null) {
                if(!pNombre.toLowerCase().contains(nombre.toLowerCase())) {
                    continue;
                }
            }
			if(documento != null) {
				if(!pDocumento.contains(documento)) {
					continue;
				}
			}
			lstPersonas.add(p.toPersonaSearchDto());
		}

		return lstPersonas;
	}

}

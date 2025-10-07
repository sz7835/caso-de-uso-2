package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.PersonaSearchDTO;

import java.util.List;

@SuppressWarnings("ALL")
public interface IPersonaService {
    List<Persona> listaPersonasNat(Long idTipoRel);
    List<Persona> listaPersonasNatStaff(Long idTipoRel);
    Persona save(Persona persona);
    List<Object> findByPersonaNat();
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, int estado);
    List<Object> findByPersonaNat(Long idTipoPer);
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc);
    List<Object> findByPersonaNat(Long idTipoPer, String nrodoc);
    List<Object> findByPersonaNat2(Long idTipoPer, String descrip);
    List<Object> findByPersonaNat(Long idTipoPer, int estado);
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc);
    List<Object> findByPersonaNat2(Long idTipoPer, Long idTipoDoc, String descrip);
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, int estado);
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip);
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, int estado);

    List<Object> finByPersonaJur();
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, int estado, String tipoCli);
    List<Object> findByPersonaJur(Long idTipoPer, String tipoCli);
    List<Object> findByPersonaJurD(Long idTipoPer, Long idTipoDoc, String document);
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String tipoCli);
    List<Object> findByPersonaJur(Long idTipoPer, String nrodoc, String tipoCli);
    List<Object> findByPersonaJur2(Long idTipoPer, String descrip, String tipoCli);
    List<Object> findByPersonaJur(Long idTipoPer, int estado, String tipoCli);
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String tipoCli);
    List<Object> findByPersonaJur2(Long idTipoPer, Long idTipoDoc, String descrip, String tipoCli);
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, int estado, String tipoCli);
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, String tipoCli);
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, int estado, String tipoCli);

    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir);
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer);
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel);
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Integer estado);
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc);
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc);
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);
    // List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
    //                              Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //                              String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
    //                              Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //                              String nombre, Long idCto);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Integer estado);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);
    // List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
    //         Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //         String nombre, Long idCto);
    // List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir,Long tipoPer,
    //                              Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //                              String nombre, Long idCto, Long idMotivo);
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel);
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, String nrodoc);
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc);
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, Integer estado);
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, String nroDoc);
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);
    // List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //                              String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //                              String nombre, Long idCto);
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel);
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, String nrodoc);
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc);
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, Integer estado);
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, String nroDoc);
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                  String nombre);
    // List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //                               String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
    //                               String nombre, Long idCto);
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc);
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc, String nroDoc);
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc, String nroDoc, Integer estado);
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);
    // List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long tipoDoc, String nroDoc, Integer estado,
    //                              String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long tipoDoc, String nroDoc, Integer estado,
    //                              String nombre, Long idCto);
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc);
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc, String nroDoc);
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc, String nroDoc, Integer estado);
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc, String nroDoc, Integer estado,
                                  String nombre);
    // List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long tipoDoc, String nroDoc, Integer estado,
    //                               String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long tipoDoc, String nroDoc, Integer estado,
    //                               String nombre, Long idCto);
    List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nroDoc);
    List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nroDoc, Integer estado);
    List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nroDoc, Integer estado,
                                 String nombre);
    // List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              String nroDoc, Integer estado,
    //                              String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              String nroDoc, Integer estado,
    //                              String nombre, Long idCto);
    List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nroDoc);
    List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nroDoc, Integer estado);
    List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nroDoc, Integer estado,
                                  String nombre);
    // List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               String nroDoc, Integer estado,
    //                               String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               String nroDoc, Integer estado,
    //                               String nombre, Long idCto);
    List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Integer estado);
    List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Integer estado,
                                 String nombre);
    // List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Integer estado,
    //                              String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Integer estado,
    //                              String nombre, Long idCto);
    List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Integer estado);
    List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Integer estado,
                                  String nombre);
    // List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Integer estado,
    //                               String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Integer estado,
    //                               String nombre, Long idCto);
    List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nombre);
    // List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              String nombre, Long idCto);
    List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nombre);
    // List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               String nombre, Long idCto, Long idMotivo);
    // List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               String nombre, Long idCto);
    // List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long idCto, Long idMotivo);
    // List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long idCto, Long idMotivo, Integer estado);
    // List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long idCto);
    // List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
    //                              Long idCto, Integer estado);
    // List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long idCto, Long idMotivo);
    // List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long idCto, Long idMotivo, Integer estado);
    // List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long idCto);
    // List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
    //                               Long idCto, Integer estado);
    Persona buscarId(Long idPer);
    List<Persona> findAll();

    List<Object> listaRelaciones(Long idPersona);

    List<Object> listaTelefonos(Long idPersona);

    List<Object> listaEmails(Long idPersona);

    List<Object> listaDirecciones(Long idPersona);
    
    List<Object> findPersonaRelacion(Long idRo);
    
    List<PersonaSearchDTO> searchByNombreOrDoc(String nombre, String documento);
}
package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.EMail;

import java.util.List;

@SuppressWarnings("ALL")
public interface IEmailService {

    List<EMail> findAllEmail(Long id);

    EMail findById(Long idEMail);
    List<EMail> findByCorreo(String correo);
    EMail save (EMail email);
    List<Object> findAllAct();
    List<Object> findAllPer(Long IdPersona);

    List<Object> findByCorreosPer(Long tipo, Long idPer);
    List<Object> findByCorreosPer(Long tipo,String correo, Long idPer);
    List<Object> findByCorreosPer(Long tipo,String correo, Integer estado, Long idPer);
    List<Object> findByCorreosPer(Long tipo, Integer estado, Long idPer);
    List<Object> findByCorreosPer(String correo, Long idPer);
    List<Object> findByCorreosPer(String correo, Integer estado, Long idPer);
    List<Object> findByCorreosPer(Integer estado, Long idPer);

}

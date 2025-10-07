package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.EMail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEmailDao extends JpaRepository<EMail,Integer> {

    @Query("from EMail e where e.persona.id = ?1 and e.estado = 1")
    List<EMail> findAllEmail(Long id);

    @Query("from EMail where idEMail = ?1")
    EMail findById(Long id);

    @Query("SELECT e FROM EMail e WHERE e.correo = :correo")
    List<EMail> findByCorreo(@Param("correo") String correo);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "where e.estado = 1")
    List<Object> findAllAct();

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "where e.persona.id = ?1 " +
            "order by e.tipo asc")
    List<Object> findAllPer(Long idPersona);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "inner join e.persona p " +
            "where e.tipo = ?1 and e.persona.id = ?2")
    List<Object> findByCorreosPer(Long tipo, Long idPer);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "inner join e.persona p " +
            "where e.tipo = ?1 and e.correo like %?2% and e.persona.id = ?3")
    List<Object> findByCorreosPer(Long tipo, String correo, Long idPer);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "inner join e.persona p " +
            "where e.tipo = ?1 and e.estado = ?2 and e.persona.id = ?3")
    List<Object> findByCorreosPer(Long tipo, Integer estado, Long idPer);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "inner join e.persona p " +
            "where e.tipo = ?1 and e.correo like %?2% and e.estado = ?3 and e.persona.id = ?4")
    List<Object> findByCorreosPer(Long tipo, String correo, Integer estado, Long idPer);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "inner join e.persona p " +
            "where e.correo like %?1% and e.persona.id = ?2")
    List<Object> findByCorreosPer(String correo, Long idPer);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "inner join e.persona p " +
            "where e.correo like %?1% and e.estado = ?2 and e.persona.id = ?3")
    List<Object> findByCorreosPer(String correo, Integer estado, Long idPer);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
            "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
            "e.fechaUpdate from EMail e " +
            "inner join e.persona p " +
            "where e.estado = ?1 and e.persona.id = ?2 " +
            "order by e.tipo asc")
    List<Object> findByCorreosPer(Integer estado, Long idPer);

    @Query("select e.idEMail, e.persona.id, e.tipo, " +
        "e.correo, e.estado, e.usuCreado, e.fechaCreado, e.usuUpdate, " +
        "e.fechaUpdate from EMail e " +
        "where e.persona.id = ?1 and e.estado = 1 " +
        "order by e.tipo asc")
        List<Object> findAllPerWithEstado(Long idPersona);
}

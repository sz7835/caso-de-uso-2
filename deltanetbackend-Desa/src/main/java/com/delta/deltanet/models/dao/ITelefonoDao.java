package com.delta.deltanet.models.dao;
import com.delta.deltanet.models.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("all")
public interface ITelefonoDao extends JpaRepository<Telefono, Integer> {

    @Query("from Telefono t where t.persona.id = ?1 and t.estado = 1")
    List<Telefono> findAllFonos(Long id);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.persona.id = ?1")
    List<Object> findAllByPer(Long idPersona);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.numero = ?1")
    List<Object> findAllByNumero(String numero);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.tipo = ?1 and t.numero = ?2 and t.estado = ?3 and t.persona.id = ?4")
    List<Object> findAllTelefonosPer(Long tipo, String numero, Integer estado, Long idPersona);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.tipo = ?1 and t.persona.id = ?2")
    List<Object> findAllTelefonosPer(Long tipo, Long idPersona);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.tipo = ?1 and t.numero = ?2 and t.persona.id = ?3")
    List<Object> findAllTelefonosPer(Long tipo, String numero, Long idPersona);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.tipo = ?1 and t.estado = ?2 and t.persona.id = ?3")
    List<Object> findAllTelefonosPer(Long tipo, Integer estado, Long idPersona);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.numero = ?1 and t.persona.id = ?2")
    List<Object> findAllTelefonosPer(String numero, Long idPersona);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.numero = ?1 and t.estado = ?2 and t.persona.id = ?3")
    List<Object> findAllTelefonosPer(String numero, Integer estado, Long idPersona);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
            "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
            "t.usuUpdate, t.fechaUpdate from Telefono t " +
            "where t.estado = ?1 and t.persona.id = ?2")
    List<Object> findAllTelefonosPer(Integer estado, Long idPersona);

    @Query("from Telefono where idTelefono = ?1")
    Telefono findById(Long id);

    @Query("select t.idTelefono, t.persona.id, t.tipo, " +
        "t.numero, t.estado, t.usuCreado, t.fechaCreado, " +
        "t.usuUpdate, t.fechaUpdate from Telefono t " +
        "where t.persona.id = ?1 and t.estado = 1 " +
        "order by t.tipo asc")
        List<Object> findAllByPerAndEstadoOrderbyTipo(Long idPersona);
    
    boolean existsByNumero(String numero);
    boolean existsByNumeroAndPersona_IdNot(String numero, Long id);

}

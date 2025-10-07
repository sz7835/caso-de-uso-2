package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AutorizacionPerfilUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IAuthPerfilUserDao extends JpaRepository<AutorizacionPerfilUsuario,Long>
{
    public Optional<AutorizacionPerfilUsuario> findById(Long idUser);

    @Query("Select a.idPerfil, a.idUser from AutorizacionPerfilUsuario a where a.idUser = ?1")
    public List<Object> findPerfiles(Long idUser);

    @Query("Select a.idPerfil from AutorizacionPerfilUsuario a where a.idUser = ?1")
    public List<Integer> findListIdPerfil(Long idUser);

    @Query("from AutorizacionPerfilUsuario where idUser = ?1 and idPerfil = ?2")
    Optional<AutorizacionPerfilUsuario> buscaIdUsrIdPerfil(Long idUser, Integer idPerfil);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sec_autorizacion_perfil_usuario where ID_USUARIO = :id",nativeQuery = true)
    void delete(Integer id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO sec_autorizacion_perfil_usuario (ID_USUARIO,ID_PERFIL) " +
            "VALUES (:id, :idPerf);",nativeQuery = true)
    void insert(Long id, Integer idPerf);
}

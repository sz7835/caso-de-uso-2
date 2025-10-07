package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.AuthFunc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAuthFuncDao extends JpaRepository<AuthFunc,Integer> {
    @Query("select a.idFunc, max(a.nomFunc), max(a.dscFunc), max(a.ruta), max(a.icon), " +
            "max(a.flgMenu), max(a.flgControl), max(b.idPerfil), max(a.route)" +
            "from AuthFunc a " +
            "inner join a.authFncPerfiles b " +
            "where b.idPerfil in ?1 and a.idApp = ?2 and a.idFuncPadre = ?3 and a.estado = 1 " +
            "group by a.idFunc order by a.secOrden ")
    List<Object> ListaFuncionalidades(List<Integer> perfiles, int aplicacion, Long idFunc);

    @Query("select a.idFunc, max(a.nomFunc), max(a.dscFunc), max(a.ruta), max(a.icon), " +
            "max(a.flgMenu), max(a.flgControl), max(b.idPerfil), max(a.route)" +
            "from AuthFunc a " +
            "inner join a.authFncPerfiles b " +
            "where b.idPerfil in ?1 and a.idApp = ?2 and a.idFuncPadre = null and a.estado = 1 " +
            "group by a.idFunc order by a.secOrden ")
    List<Object> ListaFuncionalidades(List<Integer> perfiles, int aplicacion);

    @Query("from AuthFunc a where a.estado = 1 and a.idApp = ?1 and idFuncPadre = null order by a.secOrden asc")
    List<AuthFunc> listaFuncApp(Integer idApp);

    @Query("from AuthFunc a where a.estado = 1 and a.idApp = ?1 and idFuncPadre = ?2 order by a.secOrden asc")
    List<AuthFunc> listaFuncApp(Integer idApp, Long idPadre);

    @Query("from AuthFunc a where a.idFunc = ?1 and a.idApp = ?2")
    AuthFunc buscaFuncApp(Long idFunc, Integer idApp);

    @Query("from AuthFunc a where a.idFunc = ?1")
    AuthFunc buscaFuncApp(Long idFunc);

    @Query("from AuthFunc where idFuncPadre is null and idApp = ?2 and idFunc in (?1)")
    List<AuthFunc> listaFuncs(List<Long> lista, Integer idApp);

    @Query("from AuthFunc where idFuncPadre = ?1 and idFunc in (?2) and idApp = ?3")
    List<AuthFunc> listaFuncs(Long idPadre, List<Long> lista, Integer idApp);
}

package com.delta.deltanet.models.dao;

import com.delta.deltanet.models.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@SuppressWarnings("all")
public interface IPersonaDao extends JpaRepository<Persona,Long> {

    @Query("select p.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) from Persona p " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where p.visible = 1 order by 2")
    List<Object> findByPersonaNat();

    @Query("select p from Persona p " +
            "join p.area a " +
            "join p.puesto pue " +
            "join p.perNat pn " +
            "join p.atencion at " +
            "join p.tipoPer tp " +
            "join p.tipoDoc td " +
            "join p.relaciones r " +
            "where p.estado = 1 " +
            "and a.id = 6 " +
            "and pue.id = 7 " +
            "and tp.idTipoPer = 1 " +
            "and r.idTipoRel = ?1 " +
            "and r.estado = 1")
    List<Persona> listaPersonasNat(Long idTipoRel);

    @Query("select p from Persona p " +
        "join p.area a " +
        "join p.puesto pue " +
        "join p.perNat pn " +
        "join p.atencion at " +
        "join p.tipoPer tp " +
        "join p.tipoDoc td " +
        "join p.relaciones r " +
        "where p.estado = 1 " +
        "and tp.idTipoPer = 1 " +
        "and r.idTipoRel = ?1 " +
        "and r.estado = 1 " +
        "and pue.id != 7")
    List<Persona> listaPersonasNatStaff(Long idTipoRel);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4% and p.estado = ?5 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, int estado);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and p.documento = ?2 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer, String nrodoc);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and p.visible = 1 and " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?2%")
    List<Object> findByPersonaNat2(Long idTipoPer, String descrip);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and p.estado = ?2 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer, int estado);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.visible = 1 and " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?3%")
    List<Object> findByPersonaNat2(Long idTipoPer, Long idTipoDoc, String descrip);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.estado = ?3 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, int estado);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and p.visible = 1 and " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4%")
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and " +
            "p.estado = ?4 and p.visible = 1")
    List<Object> findByPersonaNat(Long idTipoPer, Long idTipoDoc, String nrodoc, int estado);

    @Query("select p.id, pj.razonSocial, pj.razonComercial, pj.fecIniOper " +
            "from Persona p " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where p.visible = 1")
    List<Object> findByPersonaJur();

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and " +
            "pj.razonSocial like %?4% and p.estado = ?5 and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, int estado, String tipocli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.visible = 1 and p.documento = ?3")
    List<Object> findByPersonaJurD(Long idTipoPer, Long idTipoDoc, String document);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.visible = 1")
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and p.documento = ?2 and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, String nrodoc, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and " +
            "pj.razonSocial like %?2% and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur2(Long idTipoPer, String descrip, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and p.estado = ?2 and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, int estado, String tipocli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and " +
            "pj.razonSocial like %?3% and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur2(Long idTipoPer, Long idTipoDoc, String descrip, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.estado = ?3 and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, int estado, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and " +
            "pj.razonSocial like %?4% and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, String descrip, String tipoCli);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, at.id, " +
            "pj.razonSocial, p.estado, pj.razonComercial, pj.fecIniOper from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "where tp.idTipoPer = ?1 and td.idTipDoc = ?2 and p.documento = ?3 and " +
            "p.estado = ?4 and p.visible = 1 and (pj.tipo = 'C' or pj.tipo = 'P')")
    List<Object> findByPersonaJur(Long idTipoPer, Long idTipoDoc, String nrodoc, int estado, String tipoCli);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "join p.tipoPer tp " +
            "join p.tipoDoc td " +
            "join p.perNat pn " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 "+
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "join p.tipoPer tp " +
            "join p.tipoDoc td " +
            "join p.perNat pn " +
            "where tp.idTipoPer = ?4 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 and p.estado = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 and td.idTipDoc = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "and r.tipoRel.idTipoRel = ?5 and td.idTipDoc = ?6 and p.documento = ?7 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "and td.idTipDoc = ?6 and p.documento = ?7 " +
            "and p.estado = ?8 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "and td.idTipDoc = ?6 and p.documento = ?7 " +
            "and p.estado = ?8 " +
            "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?9% " +
            "group by p.id order by p.id")
    List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
//             "left outer join p.contactos c on c.contacto.id = ?10 and c.motivo.id = ?11 " +
//             "where tp.idTipoPer = ?4 " +
//             "and td.idTipDoc = ?6 and p.documento = ?7 " +
//             "and p.estado = ?8 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?9% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
//                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                  String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
//             "left outer join p.contactos c on c.contacto.id = ?10 " +
//             "where tp.idTipoPer = ?4 " +
//             "and td.idTipDoc = ?6 and p.documento = ?7 " +
//             "and p.estado = ?8 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?9% " +
//             "and c.contacto = ?10 and c.motivo = ?11 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasN(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
//                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                  String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where tp.idTipoPer = ?4 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 and p.estado = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "and td.idTipDoc = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "and td.idTipDoc = ?6 and p.documento = ?7 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "and td.idTipDoc = ?6 and p.documento = ?7 " +
            "and p.estado = ?8 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
            "where tp.idTipoPer = ?4 " +
            "and td.idTipDoc = ?6 and p.documento = ?7 " +
            "and p.estado = ?8 " +
            "and pj.razonSocial like %?9% " +
            "group by p.id order by p.id")
    List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
//             "left outer join p.contactos c on c.contacto.id = ?10 and c.motivo.id = ?11 " +
//             "where tp.idTipoPer = ?4 " +
//             "and td.idTipDoc = ?6 and p.documento = ?7 " +
//             "and p.estado = ?8 " +
//             "and pj.razonSocial like %?9% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
//                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                  String nombre, Long idCto, Long idMotivo);
//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?5 " +
//             "left outer join p.contactos c on c.contacto.id = ?10 " +
//             "where tp.idTipoPer = ?4 " +
//             "and td.idTipDoc = ?6 and p.documento = ?7 " +
//             "and p.estado = ?8 " +
//             "and pj.razonSocial like %?9% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasJ(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoPer,
//                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                  String nombre, Long idCto);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), " +
            "p.creDate, pn.fecNacim, t.numero, e.correo, d.direccion, " +
            "p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left join p.telefonos t " +
            "left join p.correos e " +
            "left join p.direcciones d " +
            "inner join p.relaciones r " +
            "where t.tipo = ?1 and e.tipo = ?2 and d.tipo = ?3  " +
            "and r.tipoRel.idTipoRel = ?4 ")
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel);

    @Query("select p.id, tp.nombre, td.nombre, p.documento, " +
            "concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), " +
            "p.creDate, pn.fecNacim, t.numero, e.correo, d.direccion, " +
            "p.estado from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left join p.telefonos t " +
            "left join p.correos e " +
            "left join p.direcciones d " +
            "inner join p.relaciones r " +
            "where t.tipo = ?1 and e.tipo = ?2 and d.tipo = ?3  " +
            "and r.tipoRel.idTipoRel = ?4 and p.documento = ?5 ")
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, String nrodoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.estado = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 "+
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.documento = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 "+
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.documento = ?6 " +
            "and p.estado = ?7 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 "+
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.documento = ?6 " +
            "and p.estado = ?7 " +
            "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?8% " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 "+
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
//             "left outer join p.contactos c on c.contacto.id = ?9 and c.motivo.id = ?10 " +
//             "where td.idTipDoc = ?5 and p.documento = ?6 " +
//             "and p.estado = ?7 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?8% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                  String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 "+
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
//             "left outer join p.contactos c on c.contacto.id = ?9 " +
//             "where td.idTipDoc = ?5 and p.documento = ?6 " +
//             "and p.estado = ?7 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?8% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasA(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                  String nombre, Long idCto);
    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where p.documento = ?5 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir, Long tipoRel, String nrodoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 group by p.id order by p.id")
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.estado = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.documento = ?6 " +
            "group by p.id order by p.id")
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.documento = ?6 " +
            "and p.estado = ?7 group by p.id order by p.id")
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
            "where td.idTipDoc = ?5 and p.documento = ?6 " +
            "and p.estado = ?7 " +
            "and pj.razonSocial like %?8% group by p.id order by p.id")
    List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
                                  String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
//             "left outer join p.contactos c on c.contacto.id = ?9 and c.motivo.id = ?10 " +
//             "where td.idTipDoc = ?5 and p.documento = ?6 " +
//             "and p.estado = ?7 " +
//             "and pj.razonSocial like %?8% group by p.id order by p.id")
//     List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                   String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?4 " +
//             "left outer join p.contactos c on c.contacto.id = ?9 " +
//             "where td.idTipDoc = ?5 and p.documento = ?6 " +
//             "and p.estado = ?7 " +
//             "and pj.razonSocial like %?8% group by p.id order by p.id")
//     List<Object> findByPersonasA1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long tipoRel, Long tipoDoc, String nroDoc, Integer estado,
//                                   String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 group by p.id order by p.id")
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 and p.documento = ?5 group by p.id order by p.id")
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc, String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 and p.documento = ?5 " +
            "and p.estado = ?6 group by p.id order by p.id")
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc, String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 and p.documento = ?5 " +
            "and p.estado = ?6 " +
            "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?7% " +
            "group by p.id order by p.id")
    List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Long tipoDoc, String nroDoc, Integer estado,
                                 String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?8 and c.motivo.id = ?9 " +
//             "where td.idTipDoc = ?4 and p.documento = ?5 " +
//             "and p.estado = ?6 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?7% " +
//             "group by p.id order by p.id")
// //     List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
// //                                  Long tipoDoc, String nroDoc, Integer estado,
// //                                  String nombre, Long idCto, Long idMotivo);
//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?8 " +
//             "where td.idTipDoc = ?4 and p.documento = ?5 " +
//             "and p.estado = ?6 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?7% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasB(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Long tipoDoc, String nroDoc, Integer estado,
//                                  String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 group by p.id order by p.id")
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 and p.documento = ?5 group by p.id order by p.id")
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc, String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 and p.documento = ?5 " +
            "and p.estado = ?6 group by p.id order by p.id")
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc, String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where td.idTipDoc = ?4 and p.documento = ?5 " +
            "and p.estado = ?6 " +
            "and pj.razonSocial like %?7% group by p.id order by p.id")
    List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Long tipoDoc, String nroDoc, Integer estado,
                                  String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?8 and c.motivo.id = ?9 " +
//             "where td.idTipDoc = ?4 and p.documento = ?5 " +
//             "and p.estado = ?6 " +
//             "and pj.razonSocial like %?7% group by p.id order by p.id")
//     List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long tipoDoc, String nroDoc, Integer estado,
//                                   String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?8 " +
//             "where td.idTipDoc = ?4 and p.documento = ?5 " +
//             "and p.estado = ?6 " +
//             "and pj.razonSocial like %?7% group by p.id order by p.id")
//     List<Object> findByPersonasB1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long tipoDoc, String nroDoc, Integer estado,
//                                   String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.documento = ?4 group by p.id order by p.id")
    List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.documento = ?4 " +
            "and p.estado = ?5 group by p.id order by p.id")
    List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.documento = ?4 " +
            "and p.estado = ?5 " +
            "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?6% " +
            "group by p.id order by p.id")
    List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nroDoc, Integer estado,
                                 String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?7 and c.motivo.id = ?8 " +
//             "where p.documento = ?4 " +
//             "and p.estado = ?5 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?6% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  String nroDoc, Integer estado,
//                                  String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?7 " +
//             "where p.documento = ?4 " +
//             "and p.estado = ?5 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?6% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasC(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  String nroDoc, Integer estado,
//                                  String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.documento = ?4 group by p.id order by p.id")
    List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nroDoc);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.documento = ?4 " +
            "and p.estado = ?5 group by p.id order by p.id")
    List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nroDoc, Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.documento = ?4 " +
            "and p.estado = ?5 " +
            "and pj.razonSocial like %?6% group by p.id order by p.id")
    List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nroDoc, Integer estado,
                                  String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "where p.documento = ?4 " +
//             "and p.estado = ?5 " +
//             "and pj.razonSocial like %?6% group by p.id order by p.id")
//     List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   String nroDoc, Integer estado,
//                                   String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?7 " +
//             "where p.documento = ?4 " +
//             "and p.estado = ?5 " +
//             "and pj.razonSocial like %?6% group by p.id order by p.id")
//     List<Object> findByPersonasC1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   String nroDoc, Integer estado,
//                                   String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.estado = ?4 group by p.id order by p.id")
    List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.estado = ?4 " +
            "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?5% " +
            "group by p.id order by p.id")
    List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
                                 Integer estado,
                                 String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?6 and c.motivo.id = ?7 " +
//             "where p.estado = ?4 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?5% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Integer estado,
//                                  String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?6 " +
//             "where p.estado = ?4 " +
//             "and concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?5% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasD(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Integer estado,
//                                  String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.estado = ?4 group by p.id order by p.id")
    List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Integer estado);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where p.estado = ?4 " +
            "and pj.razonSocial like %?5% group by p.id order by p.id")
    List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  Integer estado,
                                  String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?6 and c.motivo.id = ?7 " +
//             "where p.estado = ?4 " +
//             "and pj.razonSocial like %?5% group by p.id order by p.id")
//     List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Integer estado,
//                                   String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?6 " +
//             "where p.estado = ?4 " +
//             "and pj.razonSocial like %?5% group by p.id order by p.id")
//     List<Object> findByPersonasD1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Integer estado,
//                                   String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
            "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perNat pn inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4% " +
            "group by p.id order by p.id")
    List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir,
                                 String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?5 and c.motivo.id = ?6 " +
//             "where concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?5 " +
//             "where concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno) like %?4% " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasE(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  String nombre, Long idCto);

    @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
            "max(pj.razonSocial), " +
            "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
            "max(p.estado), max(pj.razonComercial) from Persona p " +
            "inner join p.tipoPer tp " +
            "inner join p.tipoDoc td " +
            "inner join p.perJur pj inner join p.atencion at " +
            "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
            "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
            "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
            "left outer join p.relaciones r on r.estado = 1 " +
            "where pj.razonSocial like %?4% group by p.id order by p.id")
    List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir,
                                  String nombre);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?5 and c.motivo.id = ?6 " +
//             "where pj.razonSocial like %?4% group by p.id order by p.id")
//     List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   String nombre, Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?5 " +
//             "where pj.razonSocial like %?4% group by p.id order by p.id")
//     List<Object> findByPersonasE1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   String nombre, Long idCto);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 and c.motivo.id = ?5 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 and c.motivo.id = ?5 " +
//             "where p.estado = ?6 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Long idCto, Long idMotivo, Integer estado);
//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Long idCto);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno)), " +
//             "max(p.creDate), max(pn.fecNacim), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perNat pn inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 " +
//             "where p.estado = ?5 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF(Long tipoTel, Long tipoCor, Long tipoDir,
//                                  Long idCto, Integer estado);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 and c.motivo.id = ?5 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long idCto, Long idMotivo);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 and c.motivo.id = ?5 " +
//             "where p.estado = ?6 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long idCto, Long idMotivo, Integer estado);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long idCto);

//     @Query("select p.id, max(tp.nombre), max(td.nombre), max(p.documento), " +
//             "max(pj.razonSocial), " +
//             "max(p.creDate), max(pj.fecIniOper), max(t.numero), max(e.correo), max(d.direccion), " +
//             "max(p.estado), max(pj.razonComercial) from Persona p " +
//             "inner join p.tipoPer tp " +
//             "inner join p.tipoDoc td " +
//             "inner join p.perJur pj inner join p.atencion at " +
//             "left outer join p.telefonos t on t.estado = 1 and t.tipo = ?1 " +
//             "left outer join p.correos e on e.estado = 1 and e.tipo = ?2 " +
//             "left outer join p.direcciones d on d.estado = 1 and d.tipo = ?3 " +
//             "left outer join p.relaciones r on r.estado = 1 " +
//             "left outer join p.contactos c on c.contacto.id = ?4 " +
//             "where p.estado = ?5 " +
//             "group by p.id order by p.id")
//     List<Object> findByPersonasF1(Long tipoTel, Long tipoCor, Long tipoDir,
//                                   Long idCto, Integer estado);

    @Query("from Persona where id = ?1")
    public Persona buscarId(Long idPer);

    @Query("select p.id, concat(pn.nombre,' ',pn.apePaterno,' ',pn.apeMaterno), p.estado from Persona p " +
            "inner join p.relaciones r on r.estado = 1 and r.tipoRel.idTipoRel = ?1 " +
            "inner join p.perNat pn "+
            "where p.estado = 1 and p.visible = 1" )
    List<Object> findPersonaRelacion(Long idRo);

}

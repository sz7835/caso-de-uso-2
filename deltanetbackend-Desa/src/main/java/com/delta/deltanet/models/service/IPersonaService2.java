package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class IPersonaService2 {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IAreaService areaService;

    @Autowired
    private IRelacionService relacionService;

    @Autowired
    private IPersonaService personaService;

    public List<Object> listadoClientes() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);// .distinct(true); // Asegurar resultados únicos
        Root<Persona> personaRoot = cq.from(Persona.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(personaRoot.get("visible"), 1));
        predicates.add(cb.equal(personaRoot.get("tipoPer").get("idTipoPer"), 2));

        Join<Persona, Relacion> relacionJoin = personaRoot.join("relaciones", JoinType.INNER);
        Join<Relacion, Contrato> relacion2Join = relacionJoin.join("contratos", JoinType.INNER);
        Predicate relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);
        Predicate relacion2Predicate = cb.equal(relacion2Join.get("estado"), 1);
        relacionJoin.on(relacionPredicate);
        relacion2Join.on(relacion2Predicate);
        predicates.add(cb.equal(relacionJoin.get("tipoRel"), 1));
        //predicates.add(cb.equal(relacionJoin.get("tipo"), 1));

        Join<Persona, PersonaJuridica> perJurJoin = personaRoot.join("perJur", JoinType.INNER);

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(personaRoot.get("perJur").get("razonSocial")));
        cq.groupBy(personaRoot.get("id")) ;
        List<Persona> personas = entityManager.createQuery(cq).getResultList();

        return personas.stream().map(persona -> {
            lstPerClientes clientes = new lstPerClientes();
            clientes.setId(persona.getId());
            clientes.setIdCli(persona.getPerJur().getIdPerJur());
            clientes.setRazonSocial(persona.getPerJur().getRazonComercial());
            return clientes;
        }).collect(Collectors.toList());
    }

    public List<Object> listadoClientesPorContrato() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contrato> cq = cb.createQuery(Contrato.class);
        Root<Contrato> contratoRoot = cq.from(Contrato.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(contratoRoot.get("estado"), 1L));
        predicates.add(cb.equal(contratoRoot.get("idTipoContrato"), 2L));
        cq.where(predicates.toArray(new Predicate[0]));

        List<Contrato> contratos = entityManager.createQuery(cq).getResultList();

        Set<Long> idsProcesados = new HashSet<>();
        List<Object> resultado = new ArrayList<>();
        for (Contrato contrato : contratos) {
            Long idCliente = contrato.getIdCliente();
            if (idCliente == null || idsProcesados.contains(idCliente)) continue;
            Persona persona = entityManager.find(Persona.class, idCliente);
            if (persona == null) continue;
            if (persona.getPerJur() == null) continue;
            lstPerClientes cliente = new lstPerClientes();
            cliente.setId(persona.getId());
            cliente.setIdCli(persona.getPerJur().getIdPerJur());
            cliente.setRazonSocial(persona.getPerJur().getRazonComercial());
            resultado.add(cliente);
            idsProcesados.add(idCliente);
        }
        return resultado;
    }

    public List<Object> listadoOutsourcing(Long idCliente, Long tipdoc, String nrodoc, String nom, String apepat,
            String apemat) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutenticacionUsuario> cq = cb.createQuery(AutenticacionUsuario.class);// .distinct(true); //
                                                                                            // Asegurar resultados
                                                                                            // únicos
        Root<AutenticacionUsuario> usuarioRoot = cq.from(AutenticacionUsuario.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(usuarioRoot.get("codEstUsuario"), 1));

        Join<AutenticacionUsuario, Persona> personaJoin = usuarioRoot.join("persona", JoinType.INNER);
        Join<Persona, PersonaNatural> perNatJoin = personaJoin.join("perNat", JoinType.INNER);

        Join<Persona, Relacion> relacionJoin = personaJoin.join("relaciones", JoinType.INNER);
        Predicate relacionPredicate = cb.equal(relacionJoin.get("tipoRel"), 4);
        relacionJoin.on(relacionPredicate);

        Join<Persona, PersonaCliente> personaClienteJoin = personaJoin.join("clients", JoinType.INNER);
        

        Join<Persona, Atencion> atencionJoin = personaJoin.join("atencion", JoinType.LEFT);
        Join<Persona, Telefono> telefonoJoin = personaJoin.join("telefonos", JoinType.LEFT);
        Join<Persona, EMail> emailJoin = personaJoin.join("correos", JoinType.LEFT);
        Join<Persona, Direccion> direccionJoin = personaJoin.join("direcciones", JoinType.LEFT);

        predicates.add(cb.equal(personaClienteJoin.get("estado"), 1));
        predicates.add(cb.equal(relacionJoin.get("estado"), 1));
        predicates.add(cb.equal(personaJoin.get("area"), 6));
        predicates.add(cb.equal(personaJoin.get("puesto"), 7));
        if (tipdoc != null && tipdoc != 0) {
            predicates.add(cb.equal(personaJoin.get("tipoDoc"), tipdoc));
        }
        if (nrodoc != null) {
            predicates.add(cb.equal(personaJoin.get("documento"), nrodoc));
        }
        if (nom != null) {
            predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nom + "%"));
        }
        if (apepat != null) {
            predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apepat + "%"));
        }
        if (apemat != null) {
            predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apemat + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(usuarioRoot.get("id"));

        List<AutenticacionUsuario> usuarios = entityManager.createQuery(cq).getResultList();

        return usuarios.stream().map(user -> {
            lstOutsourcing recurso = new lstOutsourcing();
            recurso.setId(user.getId());
            recurso.setRazonSocial(user.getPersona().getClients().stream().filter(client-> client.getEstado().equals(1L)).collect(Collectors.toList()).get(0).getClient().getPerJur().getRazonSocial());
            recurso.setTipDoc(user.getPersona().getTipoDoc().getNombre());
            recurso.setNroDoc(user.getPersona().getDocumento());
            recurso.setNombre(user.getPersona().getPerNat().getNombre());
            recurso.setApePat(user.getPersona().getPerNat().getApePaterno());
            recurso.setApeMat(user.getPersona().getPerNat().getApeMaterno());
            recurso.setFecNac(user.getPersona().getPerNat().getFecNacim());
            recurso.setAtencion(user.getPersona().getAtencion().getAbrev());
            List<String> emails = new ArrayList<>();
            for (EMail mail : user.getPersona().getCorreos()) {
                if (mail.getEstado() == 1) {
                    emails.add(mail.getCorreo());
                }
            }
            recurso.setCorreos(emails);

            List<String> fonos = new ArrayList<>();
            for (Telefono fono : user.getPersona().getTelefonos()) {
                if (fono.getEstado() == 1) {
                    fonos.add(fono.getNumero());
                }
            }
            recurso.setNroTlfs(fonos);

            List<String> direcs = new ArrayList<>();
            for (Direccion direccion : user.getPersona().getDirecciones()) {
                if (direccion.getEstado() == 1) {
                    direcs.add(direccion.getDireccion());
                }
            }
            recurso.setDirecciones(direcs);

            List<String> relacs = new ArrayList<>();
            for (Relacion rel : user.getPersona().getRelaciones()) {
                if (rel.getEstado() == 1) {
                    relacs.add(rel.getTipoRel().getDescrip());
                }
            }
            recurso.setTipoRel(relacs);
            return recurso;
        }).collect(Collectors.toList());

    }

    public Page<Object> listadoOutsourcing(Long idCliente, Long tipdoc, String nrodoc, String nom, String apepat,
            String apemat, Integer column, String dir, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AutenticacionUsuario> cq = cb.createQuery(AutenticacionUsuario.class);// .distinct(true); //
                                                                                            // Asegurar resultados
                                                                                            // únicos
        Root<AutenticacionUsuario> usuarioRoot = cq.from(AutenticacionUsuario.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(usuarioRoot.get("codEstUsuario"), 1));

        Join<AutenticacionUsuario, Persona> personaJoin = usuarioRoot.join("persona", JoinType.INNER);
        Join<Persona, PersonaNatural> perNatJoin = personaJoin.join("perNat", JoinType.INNER);

        Join<Persona, Relacion> relacionJoin = personaJoin.join("relaciones", JoinType.INNER);
        Predicate relacionPredicate = cb.equal(relacionJoin.get("tipoRel"), 4);
        relacionJoin.on(relacionPredicate);
        Join<Persona, PersonaCliente> personaClienteJoin = personaJoin.join("clients", JoinType.INNER);

        if (idCliente != null && idCliente != 0) {
            predicates.add(cb.equal(personaClienteJoin.get("idCliente"), idCliente));
        }

        Join<Persona, Atencion> atencionJoin = personaJoin.join("atencion", JoinType.LEFT);
        Join<Persona, Telefono> telefonoJoin = personaJoin.join("telefonos", JoinType.LEFT);
        Join<Persona, EMail> emailJoin = personaJoin.join("correos", JoinType.LEFT);
        Join<Persona, Direccion> direccionJoin = personaJoin.join("direcciones", JoinType.LEFT);

        predicates.add(cb.equal(personaClienteJoin.get("estado"), 1));
        predicates.add(cb.equal(relacionJoin.get("estado"), 1));
        predicates.add(cb.equal(personaJoin.get("area"), 6));
        predicates.add(cb.equal(personaJoin.get("puesto"), 7));
        if (tipdoc != null && tipdoc != 0) {
            predicates.add(cb.equal(personaJoin.get("tipoDoc"), tipdoc));
        }
        if (nrodoc != null) {
            predicates.add(cb.like(personaJoin.get("documento"), "%" + nrodoc + "%"));
        }
        if (nom != null) {
            predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nom + "%"));
        }
        if (apepat != null) {
            predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apepat + "%"));
        }
        if (apemat != null) {
            predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apemat + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(usuarioRoot.get("id"));

        if (column == null || dir == null) {
            cq.orderBy(cb.asc(personaJoin.get("perNat").get("apePaterno")),
                    cb.asc(personaJoin.get("perNat").get("apeMaterno")));
        } else {
            if (dir.equals("asc")) {
                if (column == 0)
                    cq.orderBy(cb.asc(personaJoin.get("tipoDoc").get("nombre")));
                if (column == 1)
                    cq.orderBy(cb.asc(personaJoin.get("documento")));
                if (column == 2)
                    cq.orderBy(cb.asc(personaJoin.get("perNat").get("apePaterno")),
                            cb.asc(personaJoin.get("perNat").get("apeMaterno")));
                if (column == 3)
                    cq.orderBy(cb.asc(personaJoin.get("perNat").get("nombre")));
                if (column == 4)
                    cq.orderBy(cb.asc(personaJoin.get("atencion").get("descrip")));
                if (column == 5)
                    cq.orderBy(cb.asc(personaJoin.get("perNat").get("fecNacim")));
            }
            if (dir.equals("desc")) {
                if (column == 0)
                    cq.orderBy(cb.desc(personaJoin.get("tipoDoc").get("nombre")));
                if (column == 1)
                    cq.orderBy(cb.desc(personaJoin.get("documento")));
                if (column == 2)
                    cq.orderBy(cb.desc(personaJoin.get("perNat").get("apePaterno")),
                            cb.asc(personaJoin.get("perNat").get("apeMaterno")));
                if (column == 3)
                    cq.orderBy(cb.desc(personaJoin.get("perNat").get("nombre")));
                if (column == 4)
                    cq.orderBy(cb.desc(personaJoin.get("atencion").get("descrip")));
                if (column == 5)
                    cq.orderBy(cb.desc(personaJoin.get("perNat").get("fecNacim")));
            }
        }

        List<AutenticacionUsuario> usuarios;

        usuarios = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();
        Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());

        List<Object> dataUsuarios = usuarios.stream().map(user -> {
            lstOutsourcing recurso = new lstOutsourcing();
            recurso.setId(user.getPersona().getId());
            recurso.setRazonSocial(user.getPersona().getClients().stream().filter(client-> client.getEstado().equals(1L)).collect(Collectors.toList()).get(0).getClient().getPerJur().getRazonSocial());
            recurso.setTipDoc(user.getPersona().getTipoDoc().getNombre());
            recurso.setNroDoc(user.getPersona().getDocumento());
            recurso.setNombre(user.getPersona().getPerNat().getNombre());
            recurso.setApePat(user.getPersona().getPerNat().getApePaterno());
            recurso.setApeMat(user.getPersona().getPerNat().getApeMaterno());
            recurso.setFecNac(user.getPersona().getPerNat().getFecNacim());
            recurso.setAtencion(user.getPersona().getAtencion().getAbrev());
            List<String> emails = new ArrayList<>();
            for (EMail mail : user.getPersona().getCorreos()) {
                if (mail.getEstado() == 1) {
                    emails.add(mail.getCorreo());
                }
            }
            recurso.setCorreos(emails);

            List<String> fonos = new ArrayList<>();
            for (Telefono fono : user.getPersona().getTelefonos()) {
                if (fono.getEstado() == 1) {
                    fonos.add(fono.getNumero());
                }
            }
            recurso.setNroTlfs(fonos);

            List<String> direcs = new ArrayList<>();
            for (Direccion direccion : user.getPersona().getDirecciones()) {
                if (direccion.getEstado() == 1) {
                    direcs.add(direccion.getDireccion());
                }
            }
            recurso.setDirecciones(direcs);

            List<String> relacs = new ArrayList<>();
            for (Relacion rel : user.getPersona().getRelaciones()) {
                if (rel.getEstado() == 1L) {
                    relacs.add(rel.getTipoRel().getDescrip());
                }
            }

            recurso.setTipoRel(relacs);
            return recurso;
        }).collect(Collectors.toList());
        Page<Object> result1 = new PageImpl<>(dataUsuarios, pageable, total);
        return result1;
    }

    public Page<Object> filtradoAvanzado(Long idTipoPersona, Long tipoRelacion, Long tipoDocumento, String nroDoc,
            String nombre, String apePat, String apeMat, Long idCto,
            Integer motivo, Integer tipoTelefono, Integer tipoEmail, Integer tipoDireccion,
            String dpto, String prov, String dist, Integer column, String dir, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Persona> cq = cb.createQuery(Persona.class); // Asegurar resultados únicos
        Root<Persona> personaRoot = cq.from(Persona.class);
        List<Predicate> predicates = new ArrayList<>();

        // condición para filtrar solo las personas con "visible" igual a 1
        predicates.add(cb.equal(personaRoot.get("visible"), 1));
        predicates.add(cb.equal(personaRoot.get("tipoPer").get("idTipoPer"), idTipoPersona));

        Long tipoRelLong = tipoRelacion != null ? Long.valueOf(tipoRelacion) : null;

        // Preparar para unir con la tabla de relaciones
        Join<Persona, Relacion> relacionJoin = personaRoot.join("relaciones", JoinType.LEFT);

        // Definir la condición de unión con la tabla de relaciones
        Predicate relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);

        // Modificar el join para incluir la condición de unión
        relacionJoin.on(relacionPredicate);

        // Filtrar por tipoRelacion si no es 0; si es 0, se ignora este filtro y se trae
        // todos los resultados
        if (tipoRelLong != null) {
            if (tipoRelLong != -1) {
                if (tipoRelLong != 0) {
                    predicates.add(cb.equal(relacionJoin.get("tipoRel"), tipoRelLong));
                }
            } else {
                predicates.add(cb.isNull(relacionJoin.get("idRel")));
            }
        }

        if (tipoDocumento != null && tipoDocumento != 0) {
            predicates.add(cb.equal(personaRoot.get("tipoDoc").get("idTipDoc"), tipoDocumento));
        }

        // Filtrar por documento si el parámetro nroDoc no está vacío
        if (nroDoc != null && !nroDoc.isEmpty()) {
            predicates.add(cb.like(personaRoot.get("documento"), "%" + nroDoc + "%"));
        }

        if (idTipoPersona == 1) { // Persona Natural
            Join<Persona, PersonaNatural> perNatJoin = personaRoot.join("perNat", JoinType.LEFT);
            if (nombre != null && !nombre.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombre + "%"));
            }
            if (apePat != null && !apePat.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apePat + "%"));
            }
            if (apeMat != null && !apeMat.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apeMat + "%"));
            }
        } else if (idTipoPersona == 2) { // Persona Jurídica
            Join<Persona, PersonaJuridica> perJurJoin = personaRoot.join("perJur", JoinType.LEFT);
            if (nombre != null && !nombre.isEmpty()) {
                Predicate razonSocialPredicate = cb.like(perJurJoin.get("razonSocial"), "%" + nombre + "%");
                Predicate razonComercialPredicate = cb.like(perJurJoin.get("razonComercial"), "%" + nombre + "%");
                predicates.add(cb.or(razonSocialPredicate, razonComercialPredicate));
            }
        }

        if (idCto != null && idCto != 0) {
            predicates.add(cb.equal(relacionJoin.get("personaD").get("id"), idCto)); // Nodo destino
        }
        if (motivo != null && motivo != 0) {
            predicates.add(cb.equal(relacionJoin.get("idTipoRel"), motivo)); // Motivo en grafo_enlace
        }

        Long tipoTelLong = tipoTelefono != null ? Long.valueOf(tipoTelefono) : null;
        Long tipoEmailLong = tipoEmail != null ? Long.valueOf(tipoEmail) : null;
        Long tipoDirLong = tipoDireccion != null ? Long.valueOf(tipoDireccion) : null;

        Join<Persona, Atencion> atencionJoin = personaRoot.join("atencion", JoinType.LEFT);
        Join<Persona, Telefono> telefonoJoin = personaRoot.join("telefonos", JoinType.LEFT);
        Join<Persona, EMail> emailJoin = personaRoot.join("correos", JoinType.LEFT);
        Join<Persona, Direccion> direccionJoin = personaRoot.join("direcciones", JoinType.LEFT);

        if (tipoTelLong != null && tipoTelefono != 0) {
            predicates.add(cb.equal(telefonoJoin.get("tipo"), tipoTelLong));
        }

        // Filtrar por tipoEmail si no es 0; si es 0, se ignora este filtro y se trae
        // todos los resultados
        if (tipoEmailLong != null && tipoEmail != 0) {
            predicates.add(cb.equal(emailJoin.get("tipo"), tipoEmailLong));
        }

        // Filtrar por tipo de dirección, incluyendo todos los resultados si
        // tipoDireccion es 0
        if (tipoDirLong != null && tipoDireccion != 0) {
            predicates.add(cb.equal(direccionJoin.get("tipo"), tipoDirLong));
        }

        if (dist != null && !"999999".equals(dist)) {
            predicates.add(cb.like(direccionJoin.get("distrito").get("id").as(String.class), dist));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(personaRoot.get("id"));

        if (idTipoPersona == 1) {
            if (column == null || dir == null) {
                cq.orderBy(cb.asc(personaRoot.get("perNat").get("apePaterno")),
                        cb.asc(personaRoot.get("perNat").get("apeMaterno")));
            } else {
                if (dir.equals("asc")) {
                    if (column == 1)
                        cq.orderBy(cb.asc(personaRoot.get("tipoDoc").get("nombre")));
                    if (column == 2)
                        cq.orderBy(cb.asc(personaRoot.get("documento")));
                    if (column == 3)
                        cq.orderBy(cb.asc(personaRoot.get("perNat").get("apePaterno")),
                                cb.asc(personaRoot.get("perNat").get("apeMaterno")));
                    if (column == 4)
                        cq.orderBy(cb.asc(personaRoot.get("perNat").get("nombre")));
                    if (column == 5)
                        cq.orderBy(cb.asc(personaRoot.get("atencion").get("descrip")));
                    if (column == 6)
                        cq.orderBy(cb.asc(personaRoot.get("perNat").get("fecNacim")));
                }
                if (dir.equals("desc")) {
                    if (column == 1)
                        cq.orderBy(cb.desc(personaRoot.get("tipoDoc").get("nombre")));
                    if (column == 2)
                        cq.orderBy(cb.desc(personaRoot.get("documento")));
                    if (column == 3)
                        cq.orderBy(cb.desc(personaRoot.get("perNat").get("apePaterno")),
                                cb.asc(personaRoot.get("perNat").get("apeMaterno")));
                    if (column == 4)
                        cq.orderBy(cb.desc(personaRoot.get("perNat").get("nombre")));
                    if (column == 5)
                        cq.orderBy(cb.desc(personaRoot.get("atencion").get("descrip")));
                    if (column == 6)
                        cq.orderBy(cb.desc(personaRoot.get("perNat").get("fecNacim")));
                }
            }
        }
        if (idTipoPersona == 2) {
            if (column == null || dir == null) {
                cq.orderBy(cb.asc(personaRoot.get("perJur").get("razonSocial")));
            } else {
                if (dir.equals("asc")) {
                    if (column == 0)
                        cq.orderBy(cb.asc(personaRoot.get("documento")));
                    if (column == 1)
                        cq.orderBy(cb.asc(personaRoot.get("perJur").get("razonSocial")));
                    if (column == 2)
                        cq.orderBy(cb.asc(personaRoot.get("perJur").get("fecIniOper")));
                }
                if (dir.equals("desc")) {
                    if (column == 0)
                        cq.orderBy(cb.desc(personaRoot.get("documento")));
                    if (column == 1)
                        cq.orderBy(cb.desc(personaRoot.get("perJur").get("razonSocial")));
                    if (column == 2)
                        cq.orderBy(cb.desc(personaRoot.get("perJur").get("fecIniOper")));
                }
            }
        }

        List<Persona> personas;

        personas = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Persona> personaRootCount = countQuery.from(Persona.class);
        predicates = new ArrayList<>();

        predicates.add(cb.equal(personaRootCount.get("visible"), 1));
        predicates.add(cb.equal(personaRootCount.get("tipoPer").get("idTipoPer"), idTipoPersona));

        relacionJoin = personaRootCount.join("relaciones", JoinType.LEFT);
        relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);
        relacionJoin.on(relacionPredicate);

        if (tipoRelLong != null) {
            if (tipoRelLong != -1) {
                if (tipoRelLong != 0) {
                    predicates.add(cb.equal(relacionJoin.get("tipoRel"), tipoRelLong));
                }
            } else {
                predicates.add(cb.isNull(relacionJoin.get("idRel")));
            }
        }

        if (tipoDocumento != null && tipoDocumento != 0)
            predicates.add(cb.equal(personaRootCount.get("tipoDoc").get("idTipDoc"), tipoDocumento));
        if (nroDoc != null && !nroDoc.isEmpty())
            predicates.add(cb.like(personaRootCount.get("documento"), "%" + nroDoc + "%"));

        if (idTipoPersona == 1) { // Persona Natural
            Join<Persona, PersonaNatural> perNatJoin = personaRootCount.join("perNat", JoinType.LEFT);
            if (nombre != null && !nombre.isEmpty())
                predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombre + "%"));
            if (apePat != null && !apePat.isEmpty())
                predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apePat + "%"));
            if (apeMat != null && !apeMat.isEmpty())
                predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apeMat + "%"));
        } else if (idTipoPersona == 2) { // Persona Jurídica
            Join<Persona, PersonaJuridica> perJurJoin = personaRootCount.join("perJur", JoinType.LEFT);
            if (nombre != null && !nombre.isEmpty()) {
                Predicate razonSocialPredicate = cb.like(perJurJoin.get("razonSocial"), "%" + nombre + "%");
                Predicate razonComercialPredicate = cb.like(perJurJoin.get("razonComercial"), "%" + nombre + "%");
                predicates.add(cb.or(razonSocialPredicate, razonComercialPredicate));
            }
        }

        Join<Persona, Relacion> relacionJoin2 = personaRootCount.join("relaciones", JoinType.LEFT);

        if (idCto != null && idCto != 0) {
            predicates.add(cb.equal(relacionJoin2.get("personaD").get("id"), idCto)); // Nodo destino
        }
        if (motivo != null && motivo != 0) {
            predicates.add(cb.equal(relacionJoin2.get("idTipoRel"), motivo)); // Motivo en grafo_enlace
        }

        atencionJoin = personaRootCount.join("atencion", JoinType.LEFT);
        telefonoJoin = personaRootCount.join("telefonos", JoinType.LEFT);
        emailJoin = personaRootCount.join("correos", JoinType.LEFT);
        direccionJoin = personaRootCount.join("direcciones", JoinType.LEFT);

        if (tipoTelLong != null && tipoTelefono != 0)
            predicates.add(cb.equal(telefonoJoin.get("tipo"), tipoTelLong));
        if (tipoEmailLong != null && tipoEmail != 0)
            predicates.add(cb.equal(emailJoin.get("tipo"), tipoEmailLong));
        if (tipoDirLong != null && tipoDireccion != 0)
            predicates.add(cb.equal(direccionJoin.get("tipo"), tipoDirLong));
        if (dist != null && !"999999".equals(dist))
            predicates.add(cb.like(direccionJoin.get("distrito").get("id").as(String.class), dist));

        countQuery.select(cb.count(personaRootCount)).where(cb.and(predicates.toArray(new Predicate[0])))
                .groupBy(personaRootCount.get("id"));
        List<Long> lista = entityManager.createQuery(countQuery).getResultList();
        Long count = (long) lista.size();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<Object> dataPersonas = personas.stream().map(persona -> {
            if (persona.getPerNat() != null) {
                lstPersona2 personaDto = new lstPersona2();
                personaDto.setId(persona.getId());
                personaDto.setAtencion(persona.getAtencion().getAbrev());
                personaDto.setTipPer(persona.getTipoPer().getNombre());
                personaDto.setTipDoc(persona.getTipoDoc().getNombre());
                personaDto.setNroDoc(persona.getDocumento());

                if (persona.getPerNat() != null) {
                    personaDto.setNombre(persona.getPerNat().getNombre());
                    personaDto.setApePat(persona.getPerNat().getApePaterno());
                    personaDto.setApeMat(persona.getPerNat().getApeMaterno());
                    personaDto.setFecNac(dateFormat.format(persona.getPerNat().getFecNacim()));
                }

                // Verificar la condición del tipo de relación antes de filtrar y ordenar
                List<String> relationsFiltrados;
                if (tipoRelacion != null && tipoRelacion != 0) {
                    // Filtrar por tipoRelacion específico y estado 1, y luego ordenar
                    relationsFiltrados = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getTipoRel().getIdTipoRel().equals(tipoRelacion)
                                    && relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getTipoRel().getDescrip())
                            .collect(Collectors.toList());
                } else {
                    // Si tipoRelacion es 9 o null, filtrar solo por estado 1 y ordenar
                    relationsFiltrados = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getTipoRel().getDescrip())
                            .collect(Collectors.toList());
                }

                // Asignar las relaciones filtradas y ordenadas a personaDto
                personaDto.setTipoRel(relationsFiltrados.isEmpty() ? null : relationsFiltrados);

                List<Long> relationsAreas;
                if (tipoRelacion != null && tipoRelacion != 0) {
                    relationsAreas = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getTipoRel().getIdTipoRel().equals(tipoRelacion)
                                    && relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getIdArea())
                            .collect(Collectors.toList());
                } else {
                    relationsAreas = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getIdArea())
                            .collect(Collectors.toList());
                }
                if (!relationsAreas.isEmpty()) {
                    Long idOficina = relationsAreas.get(0);
                    if (idOficina != null) {
                        Area oficina = areaService.findById(idOficina);
                        if (oficina != null)
                            personaDto.setDesArea(oficina.getNombre());
                        else
                            personaDto.setDesArea("---");
                    } else
                        personaDto.setDesArea("---");
                } else
                    personaDto.setDesArea("---");

                // teléfonos
                List<String> telefonosFiltrados;
                if (tipoTelefono != null && tipoTelefono != 0) {
                    telefonosFiltrados = persona.getTelefonos().stream()
                            .filter(telefono -> telefono.getTipo().equals(Long.valueOf(tipoTelefono))
                                    && telefono.getEstado() == 1) // Filtrar por tipo específico y estado
                            .sorted(Comparator.comparing(Telefono::getTipo)) // Ordenar por tipo
                            .map(Telefono::getNumero)
                            .collect(Collectors.toList());
                } else {
                    // Si tipoTelefono es 0, incluir todos los teléfonos filtrando solo por estado 1
                    // y ordenar por tipo
                    telefonosFiltrados = persona.getTelefonos().stream()
                            .filter(telefono -> telefono.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(Telefono::getTipo)) // Ordenar por tipo
                            .map(Telefono::getNumero)
                            .collect(Collectors.toList());
                }
                personaDto.setNroTlfs(!telefonosFiltrados.isEmpty() ? telefonosFiltrados : null);

                // correos
                List<String> emailsFiltrados;
                if (tipoEmail != null && tipoEmail != 0) {
                    emailsFiltrados = persona.getCorreos().stream()
                            .filter(email -> email.getTipo().equals(Long.valueOf(tipoEmail)) && email.getEstado() == 1) // Filtrar
                                                                                                                        // por
                                                                                                                        // tipo
                                                                                                                        // específico
                                                                                                                        // y
                                                                                                                        // estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(EMail::getCorreo)
                            .collect(Collectors.toList());
                } else {
                    // Si tipoEmail es 0, incluir todos los correos filtrando solo por estado 1 y
                    // ordenar por tipo
                    emailsFiltrados = persona.getCorreos().stream()
                            .filter(email -> email.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(EMail::getCorreo)
                            .collect(Collectors.toList());
                }
                personaDto.setCorreos(!emailsFiltrados.isEmpty() ? emailsFiltrados : null);

                // direcciones
                List<Direccion> direccionesFiltradasPorTipoDireccion;

                // Paso 1: Filtrar por tipoDireccion (si es diferente de 0) y estado, luego
                // ordenar por tipo
                if (tipoDireccion != null && tipoDireccion != 0) {
                    direccionesFiltradasPorTipoDireccion = persona.getDirecciones().stream()
                            .filter(direccion -> direccion.getTipo().equals(tipoDirLong) && direccion.getEstado() == 1) // Filtrar
                                                                                                                        // por
                                                                                                                        // tipo
                                                                                                                        // específico
                                                                                                                        // y
                                                                                                                        // estado
                            .sorted(Comparator.comparing(Direccion::getTipo)) // Ordenar por tipo
                            .collect(Collectors.toList());
                } else {
                    // Si tipoDireccion es 0, incluir todas las direcciones pero aún filtrar por
                    // estado = 1 y ordenar por tipo
                    direccionesFiltradasPorTipoDireccion = persona.getDirecciones().stream()
                            .filter(direccion -> direccion.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(Direccion::getTipo)) // Ordenar por tipo
                            .collect(Collectors.toList());
                }

                // Paso 2: Filtrar por tipoUbigeo
                List<String> direccionesFinales;
                if ("999999".equals(dist) || dist == null) {
                    // Incluir todas las direcciones si tipoUbigeo es '999999', las direcciones ya
                    // están filtradas y ordenadas por el paso 1

                    direccionesFinales = direccionesFiltradasPorTipoDireccion.stream()
                            .map(Direccion::getDireccion)
                            .collect(Collectors.toList());
                    personaDto.setUbigeo("---");
                } else {
                    // Filtrar direcciones basándose en el tipoUbigeo específico, manteniendo el
                    // orden previo
                    // String regexPattern = dist.replace("9999", ".*").replace("99", ".*");
                    direccionesFinales = direccionesFiltradasPorTipoDireccion.stream()
                            .filter(direccion -> direccion.getDistrito().getId().toString().matches(dist))
                            .map(Direccion::getDireccion)
                            .collect(Collectors.toList());
                    personaDto.setUbigeo((dist));
                }

                // Asignar las direcciones finales al DTO
                personaDto.setDirecciones(direccionesFinales.isEmpty() ? null : direccionesFinales);

                personaDto.setEstado(persona.getEstado());

                return personaDto;
            } else {
                lstPersonaJuridica perJurDto = new lstPersonaJuridica();
                perJurDto.setId(persona.getId());
                if (persona.getTipoPer().getNombre() == null)
                    perJurDto.setTipPer("---");
                else
                    perJurDto.setTipPer(persona.getTipoPer().getNombre());
                if (persona.getTipoDoc().getNombre() == null)
                    perJurDto.setTipDoc("---");
                else
                    perJurDto.setTipDoc(persona.getTipoDoc().getNombre());
                if (persona.getDocumento() == null)
                    perJurDto.setNroDoc("---");
                else
                    perJurDto.setNroDoc(persona.getDocumento());
                if (persona.getAtencion().getAbrev() == null)
                    perJurDto.setTratamiento("---");
                else
                    perJurDto.setTratamiento(persona.getAtencion().getAbrev());
                if (persona.getPerJur().getRazonSocial() == null)
                    perJurDto.setRazSocial("---");
                else
                    perJurDto.setRazSocial(persona.getPerJur().getRazonSocial());
                if (persona.getPerJur().getRazonComercial() == null)
                    perJurDto.setRazComercial("---");
                else
                    perJurDto.setRazComercial(persona.getPerJur().getRazonComercial());
                if (persona.getPerJur().getFecIniOper() == null)
                    perJurDto.setFechaInicio("---");
                else
                    perJurDto.setFechaInicio(dateFormat.format(persona.getPerJur().getFecIniOper()));
                if (persona.getIdTitular() == null)
                    perJurDto.setIdTitular("---");
                else
                    perJurDto.setIdTitular(persona.getIdTitular().toString());
                if (persona != null && persona.getIdTitular() != null) {
                    Persona titular = personaService.buscarId(persona.getIdTitular().longValue());
                    if (titular != null && titular.getPerNat() != null) {
                        String nombreTitular = titular.getPerNat().getApePaterno() + " " +
                                titular.getPerNat().getApeMaterno() + ", " +
                                titular.getPerNat().getNombre();
                        perJurDto.setNombreTitular(nombreTitular);
                    } else {
                        perJurDto.setNombreTitular("---");
                    }
                } else {
                    perJurDto.setNombreTitular("---");
                }

                List<lstEmail1> correosFilter;
                if (tipoEmail != null && tipoEmail != 0) {
                    correosFilter = persona.getCorreos().stream()
                            .filter(email -> email.getTipo().equals(Long.valueOf(tipoEmail)) && email.getEstado() == 1) // Filtrar
                                                                                                                        // por
                                                                                                                        // tipo
                                                                                                                        // específico
                                                                                                                        // y
                                                                                                                        // estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(email -> createEmailDto(email))
                            .collect(Collectors.toList());
                } else {
                    // Si tipoEmail es 0, incluir todos los correos filtrando solo por estado 1 y
                    // ordenar por tipo
                    correosFilter = persona.getCorreos().stream()
                            .filter(email -> email.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(email -> createEmailDto(email))
                            .collect(Collectors.toList());
                }
                perJurDto.setCorreos(!correosFilter.isEmpty() ? correosFilter : null);

                List<lstEmail1> correos = persona.getCorreos().stream()
                        .filter(email -> email.getEstado() == 1) // Filtrar por estado
                        .map(email -> createEmailDto(email))
                        .collect(Collectors.toList());
                perJurDto.setCorreos(correos);

                List<lstTelefono1> telefonos = persona.getTelefonos().stream()
                        .filter(telefono -> telefono.getEstado() == 1) // Filtrar por estado
                        .map(telefono -> createTelefonoDto(telefono))
                        .collect(Collectors.toList());
                perJurDto.setTelefonos(telefonos);

                List<Direccion> direcciones = persona.getDirecciones().stream()
                        .filter(direccion -> direccion.getEstado() == 1) // Filtrar por estado
                        .collect(Collectors.toList());
                perJurDto.setDirecciones(direcciones);

                List<Relacion> relaciones = persona.getRelaciones().stream()
                    .filter(relacion ->
                        relacion.getEstado() == 1 &&
                        relacion.getTipoRel() != null &&
                        (relacion.getTipoRel().getIdTipoRel() == 1 || relacion.getTipoRel().getIdTipoRel() == 2)
                    )
                    .collect(Collectors.toList());

                for(Relacion rel: relaciones) {
                	if(rel.getPersonaD() != null) {
                		if(rel.getPersonaD().getArea() != null) {
	                		rel.getPersonaD().getArea().setPuestos(null);
	                		rel.getPersonaD().getArea().getGerencia().setOrgAreas(null);
                		}
                    	rel.getPersonaD().setPuesto(null);
                	}
                	if(rel.getArea() != null) {
                		rel.getArea().getGerencia().setOrgAreas(null);
                	}
                }
                perJurDto.setRelaciones(relaciones);

                if (Objects.equals(persona.getEstado(), null))
                    perJurDto.setEstado("---");
                else
                    perJurDto.setEstado(String.valueOf(persona.getEstado()));
                return perJurDto;
            }
        }).collect(Collectors.toList());

        Page<Object> result1 = new PageImpl<>(dataPersonas, pageable, count);
        return result1;
    }

    public List<Object> filtradoAvanzado(Long idTipoPersona, Long tipoRelacion, Long tipoDocumento, String nroDoc,
            String nombre, String apePat, String apeMat, Long idCto,
            Integer motivo, Integer tipoTelefono, Integer tipoEmail, Integer tipoDireccion,
            String dpto, String prov, String dist) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Persona> cq = cb.createQuery(Persona.class);// .distinct(true); // Asegurar resultados únicos
        Root<Persona> personaRoot = cq.from(Persona.class);
        List<Predicate> predicates = new ArrayList<>();

        // condición para filtrar solo las personas con "visible" igual a 1
        predicates.add(cb.equal(personaRoot.get("visible"), 1));
        predicates.add(cb.equal(personaRoot.get("tipoPer").get("idTipoPer"), idTipoPersona));

        Long tipoRelLong = tipoRelacion != null ? Long.valueOf(tipoRelacion) : null;

        // Preparar para unir con la tabla de relaciones
        Join<Persona, Relacion> relacionJoin = personaRoot.join("relaciones", JoinType.LEFT);

        // Definir la condición de unión con la tabla de relaciones
        Predicate relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);

        // Modificar el join para incluir la condición de unión
        relacionJoin.on(relacionPredicate);

        // Filtrar por tipoRelacion si no es 0; si es 0, se ignora este filtro y se trae
        // todos los resultados
        if (tipoRelLong != null) {
            if (tipoRelLong != -1) {
                if (tipoRelLong != 0) {
                    predicates.add(cb.equal(relacionJoin.get("tipoRel"), tipoRelLong));
                }
            } else {
                predicates.add(cb.isNull(relacionJoin.get("idRel")));
            }
        }

        if (tipoDocumento != null && tipoDocumento != 0) {
            predicates.add(cb.equal(personaRoot.get("tipoDoc").get("idTipDoc"), tipoDocumento));
        }

        // Filtrar por documento si el parámetro nroDoc no está vacío
        if (nroDoc != null && !nroDoc.isEmpty()) {
            predicates.add(cb.like(personaRoot.get("documento"), "%" + nroDoc + "%"));
        }

        if (idTipoPersona == 1) { // Persona Natural
            Join<Persona, PersonaNatural> perNatJoin = personaRoot.join("perNat", JoinType.LEFT);
            if (nombre != null && !nombre.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombre + "%"));
            }
            if (apePat != null && !apePat.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apePat + "%"));
            }
            if (apeMat != null && !apeMat.isEmpty()) {
                predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apeMat + "%"));
            }
        } else if (idTipoPersona == 2) { // Persona Jurídica
            Join<Persona, PersonaJuridica> perJurJoin = personaRoot.join("perJur", JoinType.LEFT);
            if (nombre != null && !nombre.isEmpty()) {
                Predicate razonSocialPredicate = cb.like(perJurJoin.get("razonSocial"), "%" + nombre + "%");
                Predicate razonComercialPredicate = cb.like(perJurJoin.get("razonComercial"), "%" + nombre + "%");
                predicates.add(cb.or(razonSocialPredicate, razonComercialPredicate));
            }
        }

        if (idCto != null && idCto != 0) {
            predicates.add(cb.equal(relacionJoin.get("personaD").get("id"), idCto)); // Nodo destino
        }
        if (motivo != null && motivo != 0) {
            predicates.add(cb.equal(relacionJoin.get("idTipoRel"), motivo)); // Motivo en grafo_enlace
        }

        Long tipoTelLong = tipoTelefono != null ? Long.valueOf(tipoTelefono) : null;
        Long tipoEmailLong = tipoEmail != null ? Long.valueOf(tipoEmail) : null;
        Long tipoDirLong = tipoDireccion != null ? Long.valueOf(tipoDireccion) : null;

        Join<Persona, Atencion> atencionJoin = personaRoot.join("atencion", JoinType.LEFT);
        Join<Persona, Telefono> telefonoJoin = personaRoot.join("telefonos", JoinType.LEFT);
        Join<Persona, EMail> emailJoin = personaRoot.join("correos", JoinType.LEFT);
        Join<Persona, Direccion> direccionJoin = personaRoot.join("direcciones", JoinType.LEFT);

        if (tipoTelLong != null && tipoTelefono != 0) {
            predicates.add(cb.equal(telefonoJoin.get("tipo"), tipoTelLong));
        }

        // Filtrar por tipoEmail si no es 0; si es 0, se ignora este filtro y se trae
        // todos los resultados
        if (tipoEmailLong != null && tipoEmail != 0) {
            predicates.add(cb.equal(emailJoin.get("tipo"), tipoEmailLong));
        }

        // Filtrar por tipo de dirección, incluyendo todos los resultados si
        // tipoDireccion es 0
        if (tipoDirLong != null && tipoDireccion != 0) {
            predicates.add(cb.equal(direccionJoin.get("tipo"), tipoDirLong));
        }

        if (dist != null && !"999999".equals(dist)) {
            predicates.add(cb.like(direccionJoin.get("distrito").get("id").as(String.class), dist));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(personaRoot.get("id"));

        if (idTipoPersona == 1) {
            cq.orderBy(cb.asc(personaRoot.get("perNat").get("apePaterno")),
                    cb.asc(personaRoot.get("perNat").get("apeMaterno")));
        }
        if (idTipoPersona == 2) {
            cq.orderBy(cb.asc(personaRoot.get("perJur").get("razonSocial")));
        }

        List<Persona> personas = entityManager.createQuery(cq).getResultList();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return personas.stream().map(persona -> {
            if (persona.getPerNat() != null) {
                lstPersona2 personaDto = new lstPersona2();
                personaDto.setId(persona.getId());
                personaDto.setAtencion(persona.getAtencion().getAbrev());
                personaDto.setTipPer(persona.getTipoPer().getNombre());
                personaDto.setTipDoc(persona.getTipoDoc().getNombre());
                personaDto.setNroDoc(persona.getDocumento());

                if (persona.getPerNat() != null) {
                    personaDto.setNombre(persona.getPerNat().getNombre());
                    personaDto.setApePat(persona.getPerNat().getApePaterno());
                    personaDto.setApeMat(persona.getPerNat().getApeMaterno());
                    personaDto.setFecNac(dateFormat.format(persona.getPerNat().getFecNacim()));
                }

                // Verificar la condición del tipo de relación antes de filtrar y ordenar
                List<String> relationsFiltrados;
                if (tipoRelacion != null && tipoRelacion != 0) {
                    // Filtrar por tipoRelacion específico y estado 1, y luego ordenar
                    relationsFiltrados = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getTipoRel().getIdTipoRel().equals(tipoRelacion)
                                    && relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getTipoRel().getDescrip())
                            .collect(Collectors.toList());
                } else {
                    // Si tipoRelacion es 9 o null, filtrar solo por estado 1 y ordenar
                    relationsFiltrados = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getTipoRel().getDescrip())
                            .collect(Collectors.toList());
                }
                // Asignar las relaciones filtradas y ordenadas a personaDto
                personaDto.setTipoRel(relationsFiltrados.isEmpty() ? null : relationsFiltrados);

                List<Long> relationsAreas;
                if (tipoRelacion != null && tipoRelacion != 0) {
                    relationsAreas = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getTipoRel().getIdTipoRel().equals(tipoRelacion)
                                    && relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getIdArea())
                            .collect(Collectors.toList());
                } else {
                    relationsAreas = persona.getRelaciones().stream()
                            .filter(relacion -> relacion.getEstado() == 1)
                            .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                            .map(relacion -> relacion.getIdArea())
                            .collect(Collectors.toList());
                }
                if (!relationsAreas.isEmpty()) {
                    Long idOficina = relationsAreas.get(0);
                    if (idOficina != null) {
                        Area oficina = areaService.findById(idOficina);
                        if (oficina != null)
                            personaDto.setDesArea(oficina.getNombre());
                        else
                            personaDto.setDesArea("---");
                    } else
                        personaDto.setDesArea("---");
                } else
                    personaDto.setDesArea("---");

                // teléfonos
                List<String> telefonosFiltrados;
                if (tipoTelefono != null && tipoTelefono != 0) {
                    telefonosFiltrados = persona.getTelefonos().stream()
                            .filter(telefono -> telefono.getTipo().equals(Long.valueOf(tipoTelefono))
                                    && telefono.getEstado() == 1) // Filtrar por tipo específico y estado
                            .sorted(Comparator.comparing(Telefono::getTipo)) // Ordenar por tipo
                            .map(Telefono::getNumero)
                            .collect(Collectors.toList());
                } else {
                    // Si tipoTelefono es 0, incluir todos los teléfonos filtrando solo por estado 1
                    // y ordenar por tipo
                    telefonosFiltrados = persona.getTelefonos().stream()
                            .filter(telefono -> telefono.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(Telefono::getTipo)) // Ordenar por tipo
                            .map(Telefono::getNumero)
                            .collect(Collectors.toList());
                }
                personaDto.setNroTlfs(!telefonosFiltrados.isEmpty() ? telefonosFiltrados : null);

                // correos
                List<String> emailsFiltrados;
                if (tipoEmail != null && tipoEmail != 0) {
                    emailsFiltrados = persona.getCorreos().stream()
                            .filter(email -> email.getTipo().equals(Long.valueOf(tipoEmail)) && email.getEstado() == 1) // Filtrar
                                                                                                                        // por
                                                                                                                        // tipo
                                                                                                                        // específico
                                                                                                                        // y
                                                                                                                        // estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(EMail::getCorreo)
                            .collect(Collectors.toList());
                } else {
                    // Si tipoEmail es 0, incluir todos los correos filtrando solo por estado 1 y
                    // ordenar por tipo
                    emailsFiltrados = persona.getCorreos().stream()
                            .filter(email -> email.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(EMail::getCorreo)
                            .collect(Collectors.toList());
                }
                personaDto.setCorreos(!emailsFiltrados.isEmpty() ? emailsFiltrados : null);

                // direcciones
                List<Direccion> direccionesFiltradasPorTipoDireccion;

                // Paso 1: Filtrar por tipoDireccion (si es diferente de 0) y estado, luego
                // ordenar por tipo
                if (tipoDireccion != null && tipoDireccion != 0) {
                    direccionesFiltradasPorTipoDireccion = persona.getDirecciones().stream()
                            .filter(direccion -> direccion.getTipo().equals(tipoDirLong) && direccion.getEstado() == 1) // Filtrar
                                                                                                                        // por
                                                                                                                        // tipo
                                                                                                                        // específico
                                                                                                                        // y
                                                                                                                        // estado
                            .sorted(Comparator.comparing(Direccion::getTipo)) // Ordenar por tipo
                            .collect(Collectors.toList());
                } else {
                    // Si tipoDireccion es 0, incluir todas las direcciones pero aún filtrar por
                    // estado = 1 y ordenar por tipo
                    direccionesFiltradasPorTipoDireccion = persona.getDirecciones().stream()
                            .filter(direccion -> direccion.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(Direccion::getTipo)) // Ordenar por tipo
                            .collect(Collectors.toList());
                }

                // Paso 2: Filtrar por tipoUbigeo
                List<String> direccionesFinales;
                if ("999999".equals(dist) || dist == null) {
                    // Incluir todas las direcciones si tipoUbigeo es '999999', las direcciones ya
                    // están filtradas y ordenadas por el paso 1

                    direccionesFinales = direccionesFiltradasPorTipoDireccion.stream()
                            .map(Direccion::getDireccion)
                            .collect(Collectors.toList());
                    personaDto.setUbigeo("---");
                } else {
                    // Filtrar direcciones basándose en el tipoUbigeo específico, manteniendo el
                    // orden previo
                    // String regexPattern = dist.replace("9999", ".*").replace("99", ".*");
                    direccionesFinales = direccionesFiltradasPorTipoDireccion.stream()
                            .filter(direccion -> direccion.getDistrito().getId().toString().matches(dist))
                            .map(Direccion::getDireccion)
                            .collect(Collectors.toList());
                    personaDto.setUbigeo((dist));
                }

                // Asignar las direcciones finales al DTO
                personaDto.setDirecciones(direccionesFinales.isEmpty() ? null : direccionesFinales);

                personaDto.setEstado(persona.getEstado());

                return personaDto;
            } else {
                lstPersonaJuridica perJurDto = new lstPersonaJuridica();
                perJurDto.setId(persona.getId());
                if (persona.getTipoPer().getNombre() == null)
                    perJurDto.setTipPer("---");
                else
                    perJurDto.setTipPer(persona.getTipoPer().getNombre());
                if (persona.getTipoDoc().getNombre() == null)
                    perJurDto.setTipDoc("---");
                else
                    perJurDto.setTipDoc(persona.getTipoDoc().getNombre());
                if (persona.getDocumento() == null)
                    perJurDto.setNroDoc("---");
                else
                    perJurDto.setNroDoc(persona.getDocumento());
                if (persona.getAtencion().getAbrev() == null)
                    perJurDto.setTratamiento("---");
                else
                    perJurDto.setTratamiento(persona.getAtencion().getAbrev());
                if (persona.getPerJur().getRazonSocial() == null)
                    perJurDto.setRazSocial("---");
                else
                    perJurDto.setRazSocial(persona.getPerJur().getRazonSocial());
                if (persona.getPerJur().getRazonComercial() == null)
                    perJurDto.setRazComercial("---");
                else
                    perJurDto.setRazComercial(persona.getPerJur().getRazonComercial());
                if (persona.getPerJur().getFecIniOper() == null)
                    perJurDto.setFechaInicio("---");
                else
                    perJurDto.setFechaInicio(dateFormat.format(persona.getPerJur().getFecIniOper()));
                if (persona.getIdTitular() == null)
                    perJurDto.setIdTitular("---");
                else
                    perJurDto.setIdTitular(persona.getIdTitular().toString());
                if (persona != null && persona.getIdTitular() != null) {
                    Persona titular = personaService.buscarId(persona.getIdTitular().longValue());
                    if (titular != null && titular.getPerNat() != null) {
                        String nombreTitular = titular.getPerNat().getApePaterno() + " " +
                                titular.getPerNat().getApeMaterno() + ", " +
                                titular.getPerNat().getNombre();
                        perJurDto.setNombreTitular(nombreTitular);
                    } else {
                        perJurDto.setNombreTitular("---");
                    }
                } else {
                    perJurDto.setNombreTitular("---");
                }

                List<lstEmail1> correosFilter;
                if (tipoEmail != null && tipoEmail != 0) {
                    correosFilter = persona.getCorreos().stream()
                            .filter(email -> email.getTipo().equals(Long.valueOf(tipoEmail)) && email.getEstado() == 1) // Filtrar
                                                                                                                        // por
                                                                                                                        // tipo
                                                                                                                        // específico
                                                                                                                        // y
                                                                                                                        // estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(email -> createEmailDto(email))
                            .collect(Collectors.toList());
                } else {
                    // Si tipoEmail es 0, incluir todos los correos filtrando solo por estado 1 y
                    // ordenar por tipo
                    correosFilter = persona.getCorreos().stream()
                            .filter(email -> email.getEstado() == 1) // Filtrar por estado
                            .sorted(Comparator.comparing(EMail::getTipo)) // Ordenar por tipo
                            .map(email -> createEmailDto(email))
                            .collect(Collectors.toList());
                }
                perJurDto.setCorreos(!correosFilter.isEmpty() ? correosFilter : null);

                List<lstEmail1> correos = persona.getCorreos().stream()
                        .filter(email -> email.getEstado() == 1) // Filtrar por estado
                        .map(email -> createEmailDto(email))
                        .collect(Collectors.toList());
                perJurDto.setCorreos(correos);

                List<lstTelefono1> telefonos = persona.getTelefonos().stream()
                        .filter(telefono -> telefono.getEstado() == 1) // Filtrar por estado
                        .map(telefono -> createTelefonoDto(telefono))
                        .collect(Collectors.toList());
                perJurDto.setTelefonos(telefonos);

                List<Direccion> direcciones = persona.getDirecciones().stream()
                        .filter(direccion -> direccion.getEstado() == 1) // Filtrar por estado
                        .collect(Collectors.toList());
                perJurDto.setDirecciones(direcciones);

                List<Relacion> relaciones = persona.getRelaciones().stream()
                        .filter(relacion -> (relacion.getEstado() == 1)) // Filtrar por estado
                        .collect(Collectors.toList());

                for(Relacion rel: relaciones) {
                	if(rel.getPersonaD() != null) {
	                	if(rel.getPersonaD().getArea() != null) {
	                		rel.getPersonaD().getArea().setPuestos(null);
	                		rel.getPersonaD().getArea().getGerencia().setOrgAreas(null);
	                	}
	                	rel.getPersonaD().setPuesto(null);
                	}
                	if(rel.getArea() != null) {
                		rel.getArea().getGerencia().setOrgAreas(null);
                	}
                }
                perJurDto.setRelaciones(relaciones);

                if (Objects.equals(persona.getEstado(), null))
                    perJurDto.setEstado("---");
                else
                    perJurDto.setEstado(String.valueOf(persona.getEstado()));
                return perJurDto;
            }
        }).collect(Collectors.toList());
    }

    private lstEmail1 createEmailDto(EMail email) {
        lstEmail1 emailDto = new lstEmail1();
        emailDto.setIdEMail(email.getIdEMail());
        emailDto.setIdPersona(email.getPersona().getId());
        emailDto.setTipo(email.getTipo());
        emailDto.setCorreo(email.getCorreo());
        emailDto.setEstado(email.getEstado());
        emailDto.setUsuCreado(email.getUsuCreado());
        emailDto.setFechaCreado(email.getFechaCreado());
        emailDto.setUsuUpdate(email.getUsuUpdate());
        emailDto.setFechaUpdate(email.getFechaUpdate());
        return emailDto;
    }

    private lstTelefono1 createTelefonoDto(Telefono telefono) {
        lstTelefono1 telefonoDto = new lstTelefono1();
        telefonoDto.setIdTelefono(telefono.getIdTelefono());
        telefonoDto.setIdPersona(telefono.getPersona().getId());
        telefonoDto.setTipo(telefono.getTipo());
        telefonoDto.setNumero(telefono.getNumero());
        telefonoDto.setEstado(telefono.getEstado());
        telefonoDto.setUsuCreado(telefono.getUsuCreado());
        telefonoDto.setFechaCreado(telefono.getFechaCreado());
        telefonoDto.setUsuUpdate(telefono.getUsuUpdate());
        telefonoDto.setFechaUpdate(telefono.getFechaUpdate());
        return telefonoDto;
    }

    public List<lstPersona5> filtradoNatural(Long idTipoPersona, Long tipoRelacion, Long tipoDocumento, String nroDoc,
                String nombre, String apePat, String apeMat, Long idCto,
                Integer motivo) {

        Integer tipoTelefono = 2, tipoEmail = 1; // personal y delta
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Persona> cq = cb.createQuery(Persona.class); // Asegurar resultados únicos
        Root<Persona> personaRoot = cq.from(Persona.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(personaRoot.get("visible"), 1));
        predicates.add(cb.equal(personaRoot.get("tipoPer").get("idTipoPer"), idTipoPersona));

        Long tipoRelLong = tipoRelacion != null ? Long.valueOf(tipoRelacion) : null;

        // Join con la tabla de relaciones (grafo_enlace)
        Join<Persona, Relacion> relacionJoin = personaRoot.join("relaciones", JoinType.LEFT);
        Predicate relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);
        relacionJoin.on(relacionPredicate);

        if (tipoRelLong != null && tipoRelacion != 0) {
            predicates.add(cb.equal(relacionJoin.get("tipoRel"), tipoRelLong));
        }

        if (tipoDocumento != null && tipoDocumento != 0) {
            predicates.add(cb.equal(personaRoot.get("tipoDoc").get("idTipDoc"), tipoDocumento));
        }

        if (nroDoc != null && !nroDoc.isEmpty()) {
            predicates.add(cb.like(personaRoot.get("documento"), "%" + nroDoc + "%"));
        }

        Join<Persona, PersonaNatural> perNatJoin = personaRoot.join("perNat", JoinType.LEFT);
        if (nombre != null && !nombre.isEmpty()) {
            predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombre + "%"));
        }
        if (apePat != null && !apePat.isEmpty()) {
            predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apePat + "%"));
        }
        if (apeMat != null && !apeMat.isEmpty()) {
            predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apeMat + "%"));
        }

        if (idCto != null) {
            predicates.add(cb.equal(relacionJoin.get("personaD").get("id"), idCto)); // Nodo destino
        }
        if (motivo != null && motivo != 0) {
            predicates.add(cb.equal(relacionJoin.get("idTipoRel"), motivo)); // Motivo en grafo_enlace
        }

        Long tipoFonoLong = Long.valueOf(tipoTelefono);
        Long tipoEmailLong = Long.valueOf(tipoEmail);
        Join<Persona, Atencion> atencionJoin = personaRoot.join("atencion", JoinType.LEFT);

        Join<Persona, Telefono> telefonoJoin = personaRoot.join("telefonos", JoinType.LEFT);
        Predicate fonoPredicate = cb.equal(telefonoJoin.get("tipo"), tipoFonoLong);
        telefonoJoin.on(fonoPredicate);

        Join<Persona, EMail> emailJoin = personaRoot.join("correos", JoinType.LEFT);
        Predicate correoPredicate = cb.equal(emailJoin.get("tipo"), tipoEmailLong);
        emailJoin.on(correoPredicate);

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(personaRoot.get("perNat").get("nombre")));

        List<Persona> personas = entityManager.createQuery(cq).getResultList();

        return personas.stream().map(persona -> {
            lstPersona5 personaDto = new lstPersona5();
            personaDto.setId(persona.getId());
            personaDto.setAtencion(persona.getAtencion().getAbrev());

            if (persona.getPerNat() != null) {
                String apellidos = String.format("%s %s",
                        persona.getPerNat().getApePaterno(),
                        persona.getPerNat().getApeMaterno());
                personaDto.setNombres(persona.getPerNat().getNombre());
                personaDto.setApellidos(apellidos);
            }

            personaDto.setMotivo("---");
            if (!persona.getRelaciones().isEmpty()) {
                personaDto.setMotivo(persona.getRelaciones().get(0).getTipoRel().getDescrip());
            }

            Relacion rel = null;
            List<String> relationsFiltrados;
            String relSelec = "---";
            if (tipoRelacion != null && tipoRelacion != 0) {
                rel = relacionService.buscaId(tipoRelacion);
                if (rel != null) {
                    relSelec = rel.getTipoRel().getDescrip();
                }
                relationsFiltrados = persona.getRelaciones().stream()
                        .filter(relacion -> relacion.getTipoRel().getIdTipoRel().equals(tipoRelacion)
                                && relacion.getEstado() == 1)
                        .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                        .map(relacion -> relacion.getTipoRel().getDescrip())
                        .collect(Collectors.toList());
            } else {
                relationsFiltrados = persona.getRelaciones().stream()
                        .filter(relacion -> relacion.getEstado() == 1)
                        .sorted(Comparator.comparing(relacion -> relacion.getTipoRel().getIdTipoRel()))
                        .map(relacion -> relacion.getTipoRel().getDescrip())
                        .collect(Collectors.toList());
            }

            personaDto.setTipoRel(relationsFiltrados.isEmpty() ? null : relationsFiltrados);
            personaDto.setEmpresa("---");
            personaDto.setPropietario("---");
            if (!relationsFiltrados.isEmpty()) {
                boolean existe = relationsFiltrados.stream()
                        .anyMatch(item -> item.equals("Staff") || item.equals("Outsourcing"));
                if (existe) {
                    personaDto.setEmpresa("Informática Delta");
                    personaDto.setPropietario("Informática Delta");
                }
                personaDto.setRelSel(relSelec);
            }

            List<String> telefonosFiltrados = persona.getTelefonos().stream()
                    .filter(telefono -> telefono.getTipo().equals(Long.valueOf(tipoTelefono))
                            && telefono.getEstado() == 1)
                    .sorted(Comparator.comparing(Telefono::getTipo))
                    .map(Telefono::getNumero)
                    .collect(Collectors.toList());
            if (!telefonosFiltrados.isEmpty()) {
                personaDto.setCelular(telefonosFiltrados.get(0));
            }

            List<String> emailsFiltrados = persona.getCorreos().stream()
                    .filter(email -> email.getTipo().equals(Long.valueOf(tipoEmail)) && email.getEstado() == 1)
                    .sorted(Comparator.comparing(EMail::getTipo))
                    .map(EMail::getCorreo)
                    .collect(Collectors.toList());
            if (!emailsFiltrados.isEmpty()) {
                personaDto.setCorreo(emailsFiltrados.get(0));
            }

            return personaDto;
        }).collect(Collectors.toList());
    }

    public Page<lstPersona5> filtradoNatural(Long idTipoPersona, Long tipoRelacion, Long tipoDocumento, String nroDoc,
                String nombre, String apePat, String apeMat, Long idCto,
                Integer motivo, Integer column, String dir, Pageable pageable) {

        Integer tipoTelefono = 2, tipoEmail = 1; // personal y delta
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Persona> cq = cb.createQuery(Persona.class); // Asegurar resultados únicos
        Root<Persona> personaRoot = cq.from(Persona.class);
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(personaRoot.get("visible"), 1));
        predicates.add(cb.equal(personaRoot.get("tipoPer").get("idTipoPer"), idTipoPersona));

        Long tipoRelLong = tipoRelacion != null ? Long.valueOf(tipoRelacion) : null;

        // Join con la tabla de relaciones (grafo_enlace)
        Join<Persona, Relacion> relacionJoin = personaRoot.join("relaciones", JoinType.LEFT);
        Predicate relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);
        relacionJoin.on(relacionPredicate);

        if (tipoRelLong != null && tipoRelacion != 0) {
        predicates.add(cb.equal(relacionJoin.get("tipoRel"), tipoRelLong));
        }

        if (tipoDocumento != null && tipoDocumento != 0) {
        predicates.add(cb.equal(personaRoot.get("tipoDoc").get("idTipDoc"), tipoDocumento));
        }

        if (nroDoc != null && !nroDoc.isEmpty()) {
        predicates.add(cb.like(personaRoot.get("documento"), "%" + nroDoc + "%"));
        }

        Join<Persona, PersonaNatural> perNatJoin = personaRoot.join("perNat", JoinType.LEFT);
        if (nombre != null && !nombre.isEmpty()) {
        predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombre + "%"));
        }
        if (apePat != null && !apePat.isEmpty()) {
        predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apePat + "%"));
        }
        if (apeMat != null && !apeMat.isEmpty()) {
        predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apeMat + "%"));
        }

        if (idCto != null) {
        predicates.add(cb.equal(relacionJoin.get("personaD").get("id"), idCto)); // Nodo destino
        }
        if (motivo != null && motivo != 0) {
        predicates.add(cb.equal(relacionJoin.get("idTipoRel"), motivo)); // Motivo en grafo_enlace
        }

        Long tipoFonoLong = Long.valueOf(tipoTelefono);
        Long tipoEmailLong = Long.valueOf(tipoEmail);
        Join<Persona, Atencion> atencionJoin = personaRoot.join("atencion", JoinType.LEFT);

        Join<Persona, Telefono> telefonoJoin = personaRoot.join("telefonos", JoinType.LEFT);
        Predicate fonoPredicate = cb.equal(telefonoJoin.get("tipo"), tipoFonoLong);
        telefonoJoin.on(fonoPredicate);

        Join<Persona, EMail> emailJoin = personaRoot.join("correos", JoinType.LEFT);
        Predicate correoPredicate = cb.equal(emailJoin.get("tipo"), tipoEmailLong);
        emailJoin.on(correoPredicate);

        cq.where(predicates.toArray(new Predicate[0]));
        cq.groupBy(personaRoot.get("id"));

        if (idTipoPersona == 1) {
        if (column == null || dir == null) {
            cq.orderBy(cb.asc(personaRoot.get("perNat").get("nombre")));
        } else {
            if (dir.equals("asc")) {
                if (column == 1)
                    cq.orderBy(cb.asc(personaRoot.get("perNat").get("nombre")));
                if (column == 2)
                    cq.orderBy(cb.asc(personaRoot.get("perNat").get("apePaterno")),
                            cb.asc(personaRoot.get("perNat").get("apeMaterno")));
                if (column == 7)
                    cq.orderBy(cb.asc(personaRoot.get("atencion").get("descrip")));
            }
            if (dir.equals("desc")) {
                if (column == 1)
                    cq.orderBy(cb.desc(personaRoot.get("perNat").get("nombre")));
                if (column == 2)
                    cq.orderBy(cb.desc(personaRoot.get("perNat").get("apePaterno")),
                            cb.asc(personaRoot.get("perNat").get("apeMaterno")));
                if (column == 7)
                    cq.orderBy(cb.desc(personaRoot.get("atencion").get("descrip")));
            }
        }
        }

        List<Persona> personas = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize()).getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Persona> personaRootCount = countQuery.from(Persona.class);

        predicates = new ArrayList<>();
        predicates.add(cb.equal(personaRootCount.get("visible"), 1));
        predicates.add(cb.equal(personaRootCount.get("tipoPer").get("idTipoPer"), idTipoPersona));

        relacionJoin = personaRootCount.join("relaciones", JoinType.LEFT);
        relacionPredicate = cb.equal(relacionJoin.get("estado"), 1);
        relacionJoin.on(relacionPredicate);

        if (tipoRelLong != null && tipoRelacion != 0)
        predicates.add(cb.equal(relacionJoin.get("tipoRel"), tipoRelLong));
        if (tipoDocumento != null && tipoDocumento != 0)
        predicates.add(cb.equal(personaRootCount.get("tipoDoc").get("idTipDoc"), tipoDocumento));
        if (nroDoc != null && !nroDoc.isEmpty())
        predicates.add(cb.like(personaRootCount.get("documento"), "%" + nroDoc + "%"));

        perNatJoin = personaRootCount.join("perNat", JoinType.LEFT);
        if (nombre != null && !nombre.isEmpty())
        predicates.add(cb.like(perNatJoin.get("nombre"), "%" + nombre + "%"));
        if (apePat != null && !apePat.isEmpty())
        predicates.add(cb.like(perNatJoin.get("apePaterno"), "%" + apePat + "%"));
        if (apeMat != null && !apeMat.isEmpty())
        predicates.add(cb.like(perNatJoin.get("apeMaterno"), "%" + apeMat + "%"));

        atencionJoin = personaRootCount.join("atencion", JoinType.LEFT);
        telefonoJoin = personaRootCount.join("telefonos", JoinType.LEFT);
        fonoPredicate = cb.equal(telefonoJoin.get("tipo"), tipoFonoLong);
        telefonoJoin.on(fonoPredicate);
        emailJoin = personaRootCount.join("correos", JoinType.LEFT);
        correoPredicate = cb.equal(emailJoin.get("tipo"), tipoEmailLong);
        emailJoin.on(correoPredicate);

        countQuery.select(cb.count(personaRootCount)).where(cb.and(predicates.toArray(new Predicate[0])))
            .groupBy(personaRootCount.get("id"));
        List<Long> lista = entityManager.createQuery(countQuery).getResultList();
        Long count = (long) lista.size();

        List<lstPersona5> dataPersonas = personas.stream().map(persona -> {
        lstPersona5 personaDto = new lstPersona5();
        personaDto.setId(persona.getId());
        personaDto.setAtencion(persona.getAtencion().getAbrev());

        if (persona.getPerNat() != null) {
            String apellidos = String.format("%s %s",
                    persona.getPerNat().getApePaterno(),
                    persona.getPerNat().getApeMaterno());
            personaDto.setNombres(persona.getPerNat().getNombre());
            personaDto.setApellidos(apellidos);
        }

        personaDto.setMotivo("---");
        if (!persona.getRelaciones().isEmpty()) {
            personaDto.setMotivo(persona.getRelaciones().get(0).getTipoRel().getDescrip());
        }

        List<String> telefonosFiltrados = persona.getTelefonos().stream()
                .filter(telefono -> telefono.getTipo().equals(Long.valueOf(tipoTelefono))
                        && telefono.getEstado() == 1)
                .sorted(Comparator.comparing(Telefono::getTipo))
                .map(Telefono::getNumero)
                .collect(Collectors.toList());
        if (!telefonosFiltrados.isEmpty()) {
            personaDto.setCelular(telefonosFiltrados.get(0));
        }

        List<String> emailsFiltrados = persona.getCorreos().stream()
                .filter(email -> email.getTipo().equals(Long.valueOf(tipoEmail)) && email.getEstado() == 1)
                .sorted(Comparator.comparing(EMail::getTipo))
                .map(EMail::getCorreo)
                .collect(Collectors.toList());
        if (!emailsFiltrados.isEmpty()) {
            personaDto.setCorreo(emailsFiltrados.get(0));
        }

        return personaDto;
        }).collect(Collectors.toList());

        return new PageImpl<>(dataPersonas, pageable, count);
    }

    public List<lstPersona6> filtradoJuridico(Long tipoRelacion, Long tratamiento, String nroDoc,
                String nombre, Long estado, Integer tipoTelefono,
                Integer tipoEmail, Integer tipoDireccion,
                String dpto, String prov, String dist,
                Integer motivo) {

        Long tipoDirLong = tipoDireccion != null ? Long.valueOf(tipoDireccion) : 0;
        Long tipoEmailLong = tipoEmail != null ? Long.valueOf(tipoEmail) : 0;
        Long tipoTelLong = tipoTelefono != null ? Long.valueOf(tipoTelefono) : 0;
        Long tipoRelLong = tipoRelacion != null ? tipoRelacion : 0;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Relacion> cq = cb.createQuery(Relacion.class);
        Root<Relacion> relacionRoot = cq.from(Relacion.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(relacionRoot.get("idTipoRel"), motivo)); // Motivo en grafo_enlace
        predicates.add(cb.equal(relacionRoot.get("estado"), 1)); // Solo relaciones activas

        Join<Relacion, Persona> personaJoin = relacionRoot.join("persona", JoinType.INNER); // Nodo origen
        predicates.add(cb.equal(personaJoin.get("visible"), 1)); // Personas visibles

        Join<Relacion, Persona> personaDestinoJoin = relacionRoot.join("personaD", JoinType.LEFT); // Nodo destino
        Join<Persona, PersonaJuridica> perJurJoin = personaDestinoJoin.join("perJur", JoinType.LEFT);

        Join<Persona, EMail> emailJoin = personaDestinoJoin.join("correos", JoinType.LEFT);
        Predicate emailPredicate = cb.equal(emailJoin.get("estado"), 1);
        emailJoin.on(emailPredicate);

        Join<Persona, Telefono> telefonoJoin = personaDestinoJoin.join("telefonos", JoinType.LEFT);
        Predicate telefonoPredicate = cb.equal(telefonoJoin.get("estado"), 1);
        telefonoJoin.on(telefonoPredicate);

        Join<PersonaJuridica, Direccion> direccionJoin = perJurJoin.join("direcciones", JoinType.LEFT);

        if (tipoRelLong > 0) {
        predicates.add(cb.equal(relacionRoot.get("tipoRel"), tipoRelLong));
        }

        if (tipoDirLong != 0) {
        predicates.add(cb.equal(direccionJoin.get("tipo"), tipoDirLong));
        }

        if (dist != null && !"999999".equals(dist)) {
        predicates.add(cb.like(direccionJoin.get("distrito").get("id").as(String.class), dist));
        }

        if (nombre != null && !nombre.isEmpty()) {
        predicates.add(cb.like(perJurJoin.get("razonSocial"), "%" + nombre + "%"));
        }

        if (nroDoc != null && !nroDoc.isEmpty()) {
        predicates.add(cb.like(personaJoin.get("documento"), "%" + nroDoc + "%"));
        }

        if (tipoEmailLong != 0) {
        predicates.add(cb.equal(emailJoin.get("tipo"), tipoEmailLong));
        }

        if (tipoTelLong != 0) {
        predicates.add(cb.equal(telefonoJoin.get("tipo"), tipoTelLong));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        List<Relacion> relaciones = entityManager.createQuery(cq).getResultList();
        List<lstPersona6> response = new ArrayList<>();

        for (Relacion relacion : relaciones) {
            lstPersona6 personaDto = new lstPersona6();
            Persona personaDestino = relacion.getPersonaD();

            if (personaDestino != null) {
                personaDto.setId(personaDestino.getId());
                if (personaDestino.getPerNat() != null) {
                    personaDto.setNombres(personaDestino.getPerNat().getNombre());
                    personaDto.setApellidos(personaDestino.getPerNat().getApePaterno() + " " +
                            personaDestino.getPerNat().getApeMaterno());
                }
                if (personaDestino.getPerJur() != null) {
                    personaDto.setEmpresa(personaDestino.getPerJur().getRazonSocial());
                }
                personaDto.setRuc(personaDestino.getDocumento());
            }

            List<String> emails = personaDestino.getCorreos().stream()
                    .filter(email -> email.getEstado() == 1)
                    .map(EMail::getCorreo)
                    .collect(Collectors.toList());
            personaDto.setCorreo(emails);

            List<String> telefonos = personaDestino.getTelefonos().stream()
                    .filter(telefono -> telefono.getEstado() == 1)
                    .map(Telefono::getNumero)
                    .collect(Collectors.toList());
            personaDto.setCelular(telefonos);

            List<String> lstRel = new ArrayList<>();
            Integer flgCli = 0;
            Integer flgPrv = 0;

            for (Relacion ir : personaDestino.getRelaciones()) {
                if (ir.getEstado() == 1) {
                    if (ir.getTipoRel().getIdTipoRel() == 1)
                        flgCli = 1;
                    if (ir.getTipoRel().getIdTipoRel() == 2)
                        flgPrv = 1;
                }
            }

            if (tipoRelLong == 0) {
                if (flgCli == 0 && flgPrv == 0)
                    lstRel.add("No Cliente");
                if (flgCli == 1)
                    lstRel.add("Cliente");
                if (flgPrv == 1)
                    lstRel.add("Proveedor");
            } else if (tipoRelLong == -1) {
                if (flgCli == 0 && flgPrv == 0)
                    lstRel.add("No Cliente");
            } else if (tipoRelLong == 1 && flgCli == 1) {
                lstRel.add("Cliente");
            } else if (tipoRelLong == 2 && flgPrv == 1) {
                lstRel.add("Proveedor");
            }

            personaDto.setRelacion(lstRel);
            response.add(personaDto);
        }

        return response;
    }

    public Page<lstPersona6> filtradoJuridico(Long tipoRelacion, Long tratamiento, String nroDoc,
                String nombre, Long estado, Integer tipoTelefono,
                Integer tipoEmail, Integer tipoDireccion,
                String dpto, String prov, String dist,
                Integer motivo, Integer column, String dir, String[] campos,
                Pageable pageable) {

        Long tipoDirLong = tipoDireccion != null ? Long.valueOf(tipoDireccion) : 0;
        Long tipoEmailLong = tipoEmail != null ? Long.valueOf(tipoEmail) : 0;
        Long tipoTelLong = tipoTelefono != null ? Long.valueOf(tipoTelefono) : 0;
        Long tipoRelLong = tipoRelacion != null ? tipoRelacion : 0;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> idQuery = cb.createQuery(Long.class);
        Root<Relacion> relacionRoot = idQuery.from(Relacion.class);
        Join<Relacion, Persona> personaJoin = relacionRoot.join("persona", JoinType.INNER); // Nodo origen
        Join<Relacion, Persona> personaDestinoJoin = relacionRoot.join("personaD", JoinType.LEFT); // Nodo destino
        Join<Persona, PersonaJuridica> perJurJoin = personaDestinoJoin.join("perJur", JoinType.LEFT);
        Join<PersonaJuridica, Direccion> direccionJoin = perJurJoin.join("direcciones", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(relacionRoot.get("idTipoRel"), motivo));
        predicates.add(cb.equal(relacionRoot.get("estado"), 1));
        predicates.add(cb.equal(personaJoin.get("visible"), 1));
        if (tipoRelLong > 0) {
            predicates.add(cb.equal(relacionRoot.get("tipoRel"), tipoRelLong));
        }
        if (tipoDirLong != 0) {
            predicates.add(cb.equal(direccionJoin.get("tipo"), tipoDirLong));
        }
        if (dist != null && !"999999".equals(dist)) {
            predicates.add(cb.like(direccionJoin.get("distrito").get("id").as(String.class), dist));
        }
        if (nombre != null && !nombre.isEmpty()) {
            predicates.add(cb.like(perJurJoin.get("razonSocial"), "%" + nombre + "%"));
        }
        if (nroDoc != null && !nroDoc.isEmpty()) {
            predicates.add(cb.like(personaJoin.get("documento"), "%" + nroDoc + "%"));
        }
        if (tipoEmailLong != 0) {
            Join<Persona, EMail> emailJoin = personaDestinoJoin.join("correos", JoinType.LEFT);
            Predicate emailPredicate = cb.equal(emailJoin.get("estado"), 1);
            emailJoin.on(emailPredicate);
            predicates.add(cb.equal(emailJoin.get("tipo"), tipoEmailLong));
        }
        if (tipoTelLong != 0) {
            Join<Persona, Telefono> telefonoJoin = personaDestinoJoin.join("telefonos", JoinType.LEFT);
            Predicate telefonoPredicate = cb.equal(telefonoJoin.get("estado"), 1);
            telefonoJoin.on(telefonoPredicate);
            predicates.add(cb.equal(telefonoJoin.get("tipo"), tipoTelLong));
        }

        idQuery.select(personaDestinoJoin.get("id")).where(predicates.toArray(new Predicate[0])).groupBy(personaDestinoJoin.get("id"));

        // Ordenamiento
        List<Order> orders = new ArrayList<>();
        for (String s : campos) {
            if (s.equals("nombres")) {
                orders.add(cb.asc(personaDestinoJoin.get("perNat").get("nombre")));
            }
            if (s.equals("apellidos")) {
                orders.add(cb.asc(personaDestinoJoin.get("perNat").get("apePaterno")));
                orders.add(cb.asc(personaDestinoJoin.get("perNat").get("apeMaterno")));
            }
            if (s.equals("empresa")) {
                orders.add(cb.asc(perJurJoin.get("razonSocial")));
            }
        }
        if (!orders.isEmpty()) {
            idQuery.orderBy(orders);
        }

        // Conteo total de ids únicos
        List<Long> allIds = entityManager.createQuery(idQuery).getResultList();
        Long totRegistros = (long) allIds.size();

        // Paginación de ids
        List<Long> pagedIds = entityManager.createQuery(idQuery)
            .setFirstResult((int) pageable.getOffset())
            .setMaxResults(pageable.getPageSize())
            .getResultList();
        if (pagedIds.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, totRegistros);
        }

        CriteriaQuery<Persona> personaQuery = cb.createQuery(Persona.class);
        Root<Persona> personaRoot = personaQuery.from(Persona.class);
        personaQuery.select(personaRoot).where(personaRoot.get("id").in(pagedIds));
        List<Persona> personas = entityManager.createQuery(personaQuery).getResultList();

        List<lstPersona6> response = new ArrayList<>();
        for (Persona personaDestino : personas) {
            lstPersona6 personaDto = new lstPersona6();
            personaDto.setId(personaDestino.getId());
            if (personaDestino.getPerNat() != null) {
                personaDto.setNombres(personaDestino.getPerNat().getNombre());
                personaDto.setApellidos(personaDestino.getPerNat().getApePaterno() + " " +
                        personaDestino.getPerNat().getApeMaterno());
            }
            if (personaDestino.getPerJur() != null) {
                personaDto.setEmpresa(personaDestino.getPerJur().getRazonSocial());
            }
            personaDto.setRuc(personaDestino.getDocumento());

            List<String> emails = personaDestino.getCorreos().stream()
                    .filter(email -> email.getEstado() == 1)
                    .map(EMail::getCorreo)
                    .collect(Collectors.toList());
            personaDto.setCorreo(emails);

            List<String> telefonos = personaDestino.getTelefonos().stream()
                    .filter(telefono -> telefono.getEstado() == 1)
                    .map(Telefono::getNumero)
                    .collect(Collectors.toList());
            personaDto.setCelular(telefonos);

            List<String> relacionesDescripcion = personaDestino.getRelaciones().stream()
                .filter(rel -> rel.getEstado() == 1)
                .filter(rel -> {
                    Long origen = rel.getTipoRel().getOrigen();
                    Long destino = rel.getTipoRel().getDestino();
                    return (origen != null && destino != null && origen == 3 && destino == 3)
                        || (origen != null && origen == 2);
                })
                .map(rel -> rel.getTipoRel().getDescrip())
                .collect(Collectors.toList());
            personaDto.setRelacion(relacionesDescripcion);

            response.add(personaDto);
        }

        return new PageImpl<>(response, pageable, totRegistros);
    }
}

class lstOutsourcing {
    private Long id;
    private String razonSocial;
    private String tipDoc;
    private String nroDoc;
    private String nombre;
    private String apePat;
    private String apeMat;
    private String atencion;
    private Date fecNac;
    private List<String> correos;
    private List<String> nroTlfs;
    private List<String> tipoRel;
    private List<String> direcciones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTipDoc() {
        return tipDoc;
    }

    public void setTipDoc(String tipDoc) {
        this.tipDoc = tipDoc;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApePat() {
        return apePat;
    }

    public void setApePat(String apePat) {
        this.apePat = apePat;
    }

    public String getApeMat() {
        return apeMat;
    }

    public void setApeMat(String apeMat) {
        this.apeMat = apeMat;
    }

    public String getAtencion() {
        return atencion;
    }

    public void setAtencion(String atencion) {
        this.atencion = atencion;
    }

    public Date getFecNac() {
        return fecNac;
    }

    public void setFecNac(Date fecNac) {
        this.fecNac = fecNac;
    }

    public List<String> getCorreos() {
        return correos;
    }

    public void setCorreos(List<String> correos) {
        this.correos = correos;
    }

    public List<String> getNroTlfs() {
        return nroTlfs;
    }

    public void setNroTlfs(List<String> nroTlfs) {
        this.nroTlfs = nroTlfs;
    }

    public List<String> getTipoRel() {
        return tipoRel;
    }

    public void setTipoRel(List<String> tipoRel) {
        this.tipoRel = tipoRel;
    }

    public List<String> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones(List<String> direcciones) {
        this.direcciones = direcciones;
    }
}

class lstPerClientes {
    private Long id;
    private Long idCli;
    private String razonSocial;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCli() {
        return idCli;
    }

    public void setIdCli(Long idClie) {
        this.idCli = idClie;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }
}
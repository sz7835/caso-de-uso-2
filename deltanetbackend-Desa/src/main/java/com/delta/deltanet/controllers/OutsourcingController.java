package com.delta.deltanet.controllers;

import com.delta.deltanet.models.entity.*;
import com.delta.deltanet.models.service.IHisActividadService;
import com.delta.deltanet.models.service.IPersonaService2;
import com.delta.deltanet.models.service.PersonaJuridicaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/outsourcing")
class OutsourcingController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private IPersonaService2 personaService;

    @Autowired
    private IHisActividadService hisActividadService;

    @Autowired
    private PersonaJuridicaServiceImpl perJurService;

    @Scheduled(cron = "0 10 00 1 * ?") //El primer dia del mes a as 00:10   CR-BSG-05
    @GetMapping("/consolidado")
    public ResponseEntity<?> Consolidado(){
        Map<String, Object> response = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formPer = new SimpleDateFormat("yyyyMM");

        Calendar calendar = Calendar.getInstance();
        /*------------------------------------------------------
        try {
            String dateString = "2024-07-15";
            calendar.setTime(formatter.parse(dateString));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }------------------------------------------------------*/
        calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH)-1);
        calendar.set(Calendar.DAY_OF_MONTH,1);

        String startDate = formatter.format(calendar.getTime());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String endDate = formatter.format(calendar.getTime());
        String periodo = formPer.format(calendar.getTime());

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OutRegistroHoras> cq = cb.createQuery(OutRegistroHoras.class);
        Root<OutRegistroHoras> regHorasRoot = cq.from(OutRegistroHoras.class);

        List<Predicate> predicates = new ArrayList<>();
        Join<OutRegistroHoras,Persona> perPersonJoin = regHorasRoot.join("persona",JoinType.INNER);
        Join<Persona,PersonaNatural> perColabDataJoin = perPersonJoin.join("perNat",JoinType.INNER);

        predicates.add(cb.equal(regHorasRoot.get("estado"), 1));
        try {
            predicates.add(cb.between(regHorasRoot.get("dia"), formatter.parse(startDate), formatter.parse(endDate)));
        } catch (ParseException e) {
            response.put("message","Error en el filtro de rango de fechas");
            return new ResponseEntity<>(response,HttpStatus.FAILED_DEPENDENCY);
        }
        cq.where(predicates.toArray(new Predicate[0]));
        //cq.orderBy(cb.asc(perCliDataJoin.get("razonSocial")),cb.asc(perColabDataJoin.get("apePaterno")));

        List<OutRegistroHoras> registros = entityManager.createQuery(cq).getResultList();
        for(OutRegistroHoras reg : registros){
            /* Eliminar registros del periodo de proceso*/
            HisActividades historico = new HisActividades();
            Long idCli = null;
            String rzSoc = null;
            Long idColab = reg.getPersona().getPerNat().getIdPerNat();
            Long idColab2 = reg.getPersona().getId();
            String nomColab = reg.getPersona().getPerNat().getNombre() + " " + reg.getPersona().getPerNat().getApePaterno() + " " + reg.getPersona().getPerNat().getApeMaterno();

            calendar.setTime(reg.getDia());
            int diaSem = calendar.get(Calendar.DAY_OF_WEEK);
            String nomDia = "";
            switch (diaSem){
                case 1: nomDia = "D";break;
                case 2: nomDia = "L";break;
                case 3: nomDia = "M";break;
                case 4: nomDia = "X";break;
                case 5: nomDia = "J";break;
                case 6: nomDia = "V";break;
                case 7: nomDia = "S";break;
            }
            /*lunes es 2*/

            List<Relacion> relaciones = entityManager.createQuery(
                "SELECT ge FROM Relacion ge WHERE ge.idPersona = :idColab AND ge.idTipoRel = 4 AND ge.idPersona != 1827",
                Relacion.class)
                .setParameter("idColab", idColab2)
                .getResultList();

            for (Relacion relacion : relaciones) {
                // Buscar contratos asociados a la relación
                List<Contrato> contratos = entityManager.createQuery(
                    "SELECT c FROM Contrato c WHERE c.idRelacion = :idRelacion AND c.idTipoContrato = 2",
                    Contrato.class)
                    .setParameter("idRelacion", relacion.getIdRel())
                    .getResultList();
                for (Contrato contrato : contratos) {
                    // Obtener el cliente del contrato
                    Long idClienteContrato = contrato.getIdCliente();
                    if (idClienteContrato != null) {
                        Persona personaCliente = entityManager.find(Persona.class, idClienteContrato);
                        if (personaCliente != null && personaCliente.getPerJur() != null) {
                            idCli = personaCliente.getPerJur().getIdPerJur();
                            rzSoc = personaCliente.getPerJur().getRazonSocial();
                            break;
                        }
                    }
                }
                if (idCli != null) break;
            }
            historico.setIdCliente(idCli);
            historico.setRazonSocial(rzSoc);
            historico.setIdPersona(idColab);
            historico.setNomColab(nomColab);
            historico.setPeriodo(periodo);
            historico.setIdProyecto(reg.getIdProyecto());
            historico.setProyecto(reg.getProyect().getProyectoDescripcion());
            historico.setIdRegHoras(reg.getId());
            historico.setFecRegHora(reg.getDia());
            historico.setDiaSemana(nomDia);
            historico.setNroHoras(reg.getHoras());
            historico.setDscActividad(reg.getActividad());

            hisActividadService.save(historico);

        }
        response.put("message","proceso termino satisfactoriamente");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/periodos")
    public ResponseEntity<?> listaPeriodos(){
        Map<String, Object> response = new HashMap<>();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<HisActividades> historicoRoot = cq.from(HisActividades.class);
        cq.select(historicoRoot.get("periodo"));
        cq.groupBy(historicoRoot.get("periodo"));
        cq.orderBy(cb.desc(historicoRoot.get("periodo")));

        List<String> registros = entityManager.createQuery(cq).getResultList();
        List<Object> lista = new ArrayList<>();
        Integer pos = 0;
        for(String it : registros){
            lstPeriodos rpta = new lstPeriodos();
            rpta.setId(pos);
            rpta.setDescrip(it);
            lista.add(rpta);
            pos++;
        }
        response.put("rpta",lista);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/listaHistoricos")
    public ResponseEntity<?> listaHistorico(@RequestParam(name = "cliente") Long idCli,
                                            @RequestParam(name = "periodo") String periodo,
                                            @RequestParam(value = "paginado", required = false, defaultValue = "0") Integer swPag,
                                            @RequestParam(value = "column", required = false) Integer column,
                                            @RequestParam(value = "dir", required = false) String dir,
                                            Pageable pageable){
        Map<String, Object> response = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Optional<PersonaJuridica> datEmpresa = perJurService.findById(idCli);
        String nomCliente = datEmpresa.get().getRazonComercial();
        String anio = periodo.substring(0,4);
        String mes = nombreMes(periodo.substring(4,6));
        String titulo = "Informatica Delta - Reporte Actividades - " + nomCliente + " - Consolidado - " + mes + " " + anio;
        String nomFile = "Informatica_Delta-Reporte_Mensual-" + nomCliente.replaceAll(" ","_") + "-" + anio + "_" + periodo.substring(4,6) + "_" + mes + "-v1.0";


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<HisActividades> cq = cb.createQuery(HisActividades.class);
        Root<HisActividades> historicoRoot = cq.from(HisActividades.class);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(historicoRoot.get("idCliente"), idCli));
        predicates.add(cb.equal(historicoRoot.get("periodo"),periodo));
        cq.where(predicates.toArray(new Predicate[0]));

        cq.orderBy(cb.desc(historicoRoot.get("nomColab")));

        List<HisActividades> registros;

        List<lstHistoricos> data = new ArrayList<>();
        if(swPag==0) {
            registros = entityManager.createQuery(cq).getResultList();
            for(HisActividades ha : registros){
                lstHistoricos reg = new lstHistoricos();
                reg.setId(ha.getId());
                reg.setIdCliente(ha.getIdCliente());
                reg.setRazonSocial(ha.getRazonSocial());
                reg.setIdPersona(ha.getIdPersona());
                reg.setNomColab(ha.getNomColab());
                reg.setPeriodo(ha.getPeriodo());
                //reg.setIdCronograma(ha.getIdCronograma());
                reg.setIdProyecto(ha.getIdProyecto());
                reg.setProyecto(ha.getProyecto());
                reg.setIdRegHoras(ha.getIdRegHoras());
                reg.setFecRegHora(dateFormat.format(ha.getFecRegHora()));
                reg.setDiaSemana(ha.getDiaSemana());
                reg.setNroHoras(ha.getNroHoras());
                reg.setDscActividad(ha.getDscActividad());
                data.add(reg);
            }

            if (data.isEmpty()){
                response.put("mensaje","No se han encontrado datos..");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response.put("datos", data);
            response.put("titulo",titulo);
            response.put("nomfile",nomFile);
        } else {
            registros = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
            Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());

            List<Object> dataReg = registros.stream().map(ha -> {
                lstHistoricos reg = new lstHistoricos();
                reg.setId(ha.getId());
                reg.setIdCliente(ha.getIdCliente());
                reg.setRazonSocial(ha.getRazonSocial());
                reg.setIdPersona(ha.getIdPersona());
                reg.setNomColab(ha.getNomColab());
                reg.setPeriodo(ha.getPeriodo());
                //reg.setIdCronograma(ha.getIdCronograma());
                reg.setIdProyecto(ha.getIdProyecto());
                reg.setProyecto(ha.getProyecto());
                reg.setIdRegHoras(ha.getIdRegHoras());
                reg.setFecRegHora(dateFormat.format(ha.getFecRegHora()));
                reg.setDiaSemana(ha.getDiaSemana());
                reg.setNroHoras(ha.getNroHoras());
                reg.setDscActividad(ha.getDscActividad());
                return reg;
            }).collect(Collectors.toList());

            Page<Object> result1 = new PageImpl<>(dataReg,pageable,total);

            response.put("datos",result1.getContent());
            response.put("totRegs",result1.getTotalElements());
            response.put("totPags", result1.getTotalPages());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/listaClientes")
    public ResponseEntity<?> listadoClientes() {
        List<Object> clientes = personaService.listadoClientes();
        return ResponseEntity.ok(clientes);
    }

    String nombreMes(String mes){
        String cad = "";
        switch (mes){
            case "01": cad="Enero";break;
            case "02": cad="Febrero";break;
            case "03": cad="Marzo";break;
            case "04": cad="Abril";break;
            case "05": cad="Mayo";break;
            case "06": cad="Junio";break;
            case "07": cad="Julio";break;
            case "08": cad="Agosto";break;
            case "09": cad="Setiembre";break;
            case "10": cad="Octubre";break;
            case "11": cad="Noviembre";break;
            case "12": cad="Diciembre";break;
        }
        return  cad;
    }
}

class lstHistoricos{
    private Long id;
    private Long idCliente;
    private String razonSocial;
    private Long idPersona;
    private String nomColab;
    private String periodo;
    private Integer idProyecto;
    private Integer idCronograma;
    private String proyecto;
    private Integer idRegHoras;
    private String fecRegHora;
    private String diaSemana;
    private Integer nroHoras;
    private String dscActividad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getNomColab() {
        return nomColab;
    }

    public void setNomColab(String nomColab) {
        this.nomColab = nomColab;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Integer getIdCronograma() {
        return idCronograma;
    }

    public void setIdCronograma(Integer idCronograma) {
        this.idCronograma = idCronograma;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public Integer getIdRegHoras() {
        return idRegHoras;
    }

    public void setIdRegHoras(Integer idRegHoras) {
        this.idRegHoras = idRegHoras;
    }

    public String getFecRegHora() {
        return fecRegHora;
    }

    public void setFecRegHora(String fecRegHora) {
        this.fecRegHora = fecRegHora;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getNroHoras() {
        return nroHoras;
    }

    public void setNroHoras(Integer nroHoras) {
        this.nroHoras = nroHoras;
    }

    public String getDscActividad() {
        return dscActividad;
    }

    public void setDscActividad(String dscActividad) {
        this.dscActividad = dscActividad;
    }
}

class lstPeriodos{
    private Integer id;
    private String descrip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }
}
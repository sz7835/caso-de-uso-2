package com.delta.deltanet.models.service;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.ISrvActividadesDao;
import com.delta.deltanet.models.entity.AutenticacionUsuario;
import com.delta.deltanet.models.entity.PersonaJuridica;
import com.delta.deltanet.models.entity.ServicioActividades;
import com.delta.deltanet.models.entity.ServicioForma;
import com.delta.deltanet.models.entity.ServicioTipo;
import com.delta.deltanet.models.entity.SrvEstado;

@Service
public class SrvActivServiceImpl implements ISrvActivService {

	@Autowired
	private ISrvActividadesDao actividadServicio;

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<ServicioActividades> listaActividades() {
		return actividadServicio.listaActividades();
	}

	@Override
	public void delete(ServicioActividades item) {
		actividadServicio.delete(item);
	}

	@Override
	public ServicioActividades save(ServicioActividades reg) {
		return actividadServicio.save(reg);
	}
	
	@Override
	public List<ServicioActividades> saveAll(List<ServicioActividades> regs) {
		return actividadServicio.saveAll(regs);
	}

	@Override
	public ServicioActividades buscaById(Long id) {
		return actividadServicio.findById(id).orElse(null);
	}

	@Override
	public List<String> listaPeriodos() {
		return actividadServicio.listaPeriodos();
	}

	@Override
	public List<lstActi> lstFiltrado(Long idCli, Long idTipo, Long idForma, Long estado, Long idResolutor,
			Date fecIni, Date fecFin, Integer facturable, String mmFac, String yyFac, List<Long> lstTickets) {
		List<lstActi> lstResumen = new ArrayList<>();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServicioActividades> cq = cb.createQuery(ServicioActividades.class);
		Root<ServicioActividades> actiRoot = cq.from(ServicioActividades.class);
		List<Predicate> predicates = new ArrayList<>();

		if (idCli != null && idCli != 0) {
			Join<ServicioActividades, PersonaJuridica> clienteJoin = actiRoot.join("cliente", JoinType.INNER);
			predicates.add(cb.equal(clienteJoin.get("idPerJur"), idCli));
		}
		//  else {
		// 	return lstResumen;
		// }
		
		if (lstTickets != null && !lstTickets.isEmpty()) {
		    predicates.add(actiRoot.get("id").in(lstTickets));
		}
		
		if (idTipo != null && idTipo != 0) {
			Join<ServicioActividades, ServicioTipo > tipoJoin = actiRoot.join("tipo", JoinType.INNER);
			predicates.add(cb.equal(tipoJoin.get("id"), idTipo));
		}
		if (idForma!= null && idForma != 0) {
			Join<ServicioActividades, ServicioForma> formaJoin = actiRoot.join("forma", JoinType.INNER);
			predicates.add(cb.equal(formaJoin.get("id"), idForma));
		}
		if (estado!= null && estado != 0) {
			Join<ServicioActividades, SrvEstado> estadoJoin = actiRoot.join("estado", JoinType.INNER);
			predicates.add(cb.equal(estadoJoin.get("id"), estado));
		}
		if (idResolutor != null && idResolutor != 0) {
			Join<ServicioActividades, AutenticacionUsuario> usuarioJoin = actiRoot.join("resolutor", JoinType.INNER);
			predicates.add(cb.equal(usuarioJoin.get("id"), idResolutor));
		}
		if (fecIni != null) {
			predicates.add(cb.greaterThanOrEqualTo(actiRoot.get("fechaIni"), fecIni));
		}
		if (fecFin != null) predicates.add(cb.lessThanOrEqualTo(actiRoot.get("fechaFin"), fecFin));
		if (facturable != null) predicates.add(cb.equal(actiRoot.get("facturable"), facturable));
		if (mmFac != null) predicates.add(cb.equal(actiRoot.get("mesFac"), mmFac));
		if (yyFac != null) predicates.add(cb.equal(actiRoot.get("yearFac"), yyFac));

		cq.where(predicates.toArray(new Predicate[0]));
		List<ServicioActividades> actividades = entityManager.createQuery(cq).getResultList();
		lstActi lista;
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);

		for(ServicioActividades reg : actividades) {
			String resolutorName = "";
			
			if(reg.getResolutor().getPersona().getPerNat() != null) {
				resolutorName = reg.getResolutor().getPersona().getPerNat().getNombre() + 
						" " + 
						reg.getResolutor().getPersona().getPerNat().getApePaterno() +
						" " +
						reg.getResolutor().getPersona().getPerNat().getApeMaterno();
			} else {
				resolutorName = reg.getResolutor().getPersona().getPerJur().getRazonSocial();
			}
			
			lista = new lstActi();
			lista.setId(reg.getId());
			lista.setTipo(reg.getTipo().getNombre());
			lista.setForma(reg.getForma().getNombre());
			lista.setIdEstado(reg.getEstado().getId());
			lista.setDesEstado(reg.getEstado().getNombre());
			lista.setResolutor(resolutorName);
			lista.setDescrip(reg.getDescrip());
			lista.setNumero(reg.getNumero());
			lista.setNroHoras(reg.getHoras());
			lista.setFecIni(formatter.format(reg.getFechaIni()));
			lista.setFecFin(formatter.format(reg.getFechaFin()));
			lista.setCliente(reg.getCliente().getRazonSocial());
			// Nuevos campos para mes/año ejecución y facturación
			lista.setMesEjecucion(reg.getMesEje());
			lista.setAnioEjecucion(reg.getYearEje());
			lista.setMesFacturacion(reg.getMesFac());
			lista.setAnioFacturacion(reg.getYearFac());
			lista.setPeriodoEje(reg.getYearEje() + "-" + reg.getMesEje());
			// Validación para periodoFac: solo mostrar si ambos valores existen y no son vacíos
			String yearFac = reg.getYearFac();
			String mesFac = reg.getMesFac();
			if (yearFac != null && !yearFac.trim().isEmpty() && mesFac != null && !mesFac.trim().isEmpty()) {
				lista.setPeriodoFac(yearFac + "-" + mesFac);
			} else {
				lista.setPeriodoFac("");
			}
			lista.setFacturable(reg.getFacturable());
			lista.setPeriodoEje(reg.getMesEje() + "-" + reg.getYearEje());
			lista.setPeriodoFac(reg.getYearFac() + "-" + reg.getMesFac());
			lista.setObservacion(reg.getObs());

			lstResumen.add(lista);
		}
		return lstResumen;
	}

	@Override
	public Page<lstActi> lstFiltrado(Long idCli, Long idTipo, Long idForma, Long estado, Long idResolutor,
			Date fecIni, Date fecFin, Integer facturable, String mmFac, String yyFac, Pageable pageable) {
		List<lstActi> lstResumen = new ArrayList<>();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServicioActividades> cq = cb.createQuery(ServicioActividades.class);
		Root<ServicioActividades> actiRoot = cq.from(ServicioActividades.class);
		List<Predicate> predicates = new ArrayList<>();

		if (idCli != null && idCli != 0) {
			Join<ServicioActividades, PersonaJuridica> clienteJoin = actiRoot.join("cliente", JoinType.INNER);
			predicates.add(cb.equal(clienteJoin.get("idPerJur"), idCli));
		}
		// else {
		// 	Page<lstActi> resp = new PageImpl<>(lstResumen, pageable, 0);
		// 	return resp;
		// }
		if (idTipo != null && idTipo != 0) {
			Join<ServicioActividades, ServicioTipo > tipoJoin = actiRoot.join("tipo", JoinType.INNER);
			predicates.add(cb.equal(tipoJoin.get("id"), idTipo));
		}
		if (idForma!= null && idForma != 0) {
			Join<ServicioActividades, ServicioForma> formaJoin = actiRoot.join("forma", JoinType.INNER);
			predicates.add(cb.equal(formaJoin.get("id"), idForma));
		}
		if (estado!= null && estado != 0) {
			Join<ServicioActividades, SrvEstado> estadoJoin = actiRoot.join("estado", JoinType.INNER);
			predicates.add(cb.equal(estadoJoin.get("id"), estado));
		}
		if (idResolutor != null && idResolutor != 0) {
			Join<ServicioActividades, AutenticacionUsuario> usuarioJoin = actiRoot.join("resolutor", JoinType.INNER);
			predicates.add(cb.equal(usuarioJoin.get("id"), idResolutor));
		}
		if (fecIni != null) {
			predicates.add(cb.greaterThanOrEqualTo(actiRoot.get("fechaIni"), fecIni));
		}
		if (fecFin != null) predicates.add(cb.lessThanOrEqualTo(actiRoot.get("fechaFin"), fecFin));
		if (facturable != null) predicates.add(cb.equal(actiRoot.get("facturable"), facturable));
		if (mmFac != null) predicates.add(cb.equal(actiRoot.get("mesFac"), mmFac));
		if (yyFac != null) predicates.add(cb.equal(actiRoot.get("yearFac"), yyFac));
		cq.where(predicates.toArray(new Predicate[0]));

		List<ServicioActividades> actividades = entityManager.createQuery(cq).setFirstResult((int) pageable.getOffset())
				.setMaxResults(pageable.getPageSize()).getResultList();
		Long total = Long.valueOf(entityManager.createQuery(cq).getResultList().size());
		lstActi lista;

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
		for(ServicioActividades reg : actividades) {
			String resolutorName = "";
			
			if(reg.getResolutor().getPersona().getPerNat() != null) {
				resolutorName = reg.getResolutor().getPersona().getPerNat().getNombre() + 
						" " + 
						reg.getResolutor().getPersona().getPerNat().getApePaterno() +
						" " +
						reg.getResolutor().getPersona().getPerNat().getApeMaterno();
			} else {
				resolutorName = reg.getResolutor().getPersona().getPerJur().getRazonSocial();
			}
			
			lista = new lstActi();
			lista.setId(reg.getId());
			lista.setTipo(reg.getTipo().getNombre());
			lista.setForma(reg.getForma().getNombre());
			lista.setIdEstado(reg.getEstado().getId());
			lista.setNumero(reg.getNumero());
			lista.setDesEstado(reg.getEstado().getNombre());
			lista.setResolutor(resolutorName);
			lista.setDescrip(reg.getDescrip());
			lista.setNroHoras(reg.getHoras());
			lista.setFecIni(formatter.format(reg.getFechaIni()));
			lista.setFecFin(formatter.format(reg.getFechaFin()));
			lista.setCliente(reg.getCliente().getRazonSocial());
			lista.setMesEjecucion(reg.getMesEje());
			lista.setAnioEjecucion(reg.getYearEje());
			// convertir mesFacturacion a nombre del mes
			String mesFact = reg.getMesFac();
			String nombreMesFac = "";
			if (mesFact != null && !mesFact.trim().isEmpty()) {
				try {
					int mesInt = Integer.parseInt(mesFact);
					nombreMesFac = Month.of(mesInt).getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
					// Primera letra mayúscula, resto minúscula
					nombreMesFac = nombreMesFac.substring(0, 1).toUpperCase() + nombreMesFac.substring(1).toLowerCase();
				} catch (Exception e) {
					nombreMesFac = mesFact; // fallback si hay error
				}
			}
			lista.setMesFacturacion(nombreMesFac);
			lista.setAnioFacturacion(reg.getYearFac());
			lista.setFacturable(reg.getFacturable());
			lista.setPeriodoEje(reg.getYearEje() + "-" + reg.getMesEje());
			String yearFac = reg.getYearFac();
			String mesFac = reg.getMesFac();
			if (yearFac != null && !yearFac.trim().isEmpty() && mesFac != null && !mesFac.trim().isEmpty()) {
				lista.setPeriodoFac(yearFac + "-" + mesFac);
			} else {
				lista.setPeriodoFac("");
			}
			lista.setObservacion(reg.getObs());

			lstResumen.add(lista);
		}
		Page<lstActi> resp = new PageImpl<>(lstResumen, pageable, total);
		return resp;
	}
	
	@Override
	public List<ServicioActividades> lstFiltrado(Long idCli, List<Long> lstTickets, Integer facturable) {
		List<ServicioActividades> actividades = new ArrayList<>();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ServicioActividades> cq = cb.createQuery(ServicioActividades.class);
		Root<ServicioActividades> actiRoot = cq.from(ServicioActividades.class);
		
		List<Predicate> predicates = new ArrayList<>();
		
		if (idCli != null && idCli != 0) {
			Join<ServicioActividades, PersonaJuridica> clienteJoin = actiRoot.join("cliente", JoinType.INNER);
			predicates.add(cb.equal(clienteJoin.get("idPerJur"), idCli));
		} else {
			return actividades;
		}
		
		if (lstTickets != null && !lstTickets.isEmpty()) {
		    predicates.add(actiRoot.get("id").in(lstTickets));
		}
		
		if (facturable != null) predicates.add(cb.equal(actiRoot.get("facturable"), facturable));
		
		cq.where(predicates.toArray(new Predicate[0]));
		
		actividades = entityManager.createQuery(cq).getResultList();

		return actividades;
	}

	
	public class lstActi {
		public Long id;
		public String tipo;
		public String forma;
		public Long idEstado;
		public Long numero;
		public String desEstado;
		public String resolutor;
		public String descrip;
		public double nroHoras;
		public String fecIni;
		public String fecFin;
		public Integer facturable;
		public String periodoEje;
		public String periodoFac;
		public String observacion;
		private String cliente;
		private String mesEjecucion;
		private String anioEjecucion;
		private String mesFacturacion;
		private String anioFacturacion;

		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getForma() {
			return forma;
		}
		public void setForma(String forma) {
			this.forma = forma;
		}
		public Long getIdEstado() {
			return idEstado;
		}
		public void setIdEstado(Long idEstado) {
			this.idEstado = idEstado;
		}
		public Long getNumero() {
			return numero;
		}
		public void setNumero(Long numero) {
			this.numero = numero;
		}
		public String getDesEstado() {
			return desEstado;
		}
		public void setDesEstado(String desEstado) {
			this.desEstado = desEstado;
		}
		public String getResolutor() {
			return resolutor;
		}
		public void setResolutor(String resolutor) {
			this.resolutor = resolutor;
		}
		public String getDescrip() {
			return descrip;
		}
		public void setDescrip(String descrip) {
			this.descrip = descrip;
		}
		public double getNroHoras() {
			return nroHoras;
		}
		public void setNroHoras(double nroHoras) {
			this.nroHoras = nroHoras;
		}
		public String getFecIni() {
			return fecIni;
		}
		public void setFecIni(String fecIni) {
			this.fecIni = fecIni;
		}
		public String getFecFin() {
			return fecFin;
		}
		public void setFecFin(String fecFin) {
			this.fecFin = fecFin;
		}
		public Integer getFacturable() {
			return facturable;
		}
		public void setFacturable(Integer facturable) {
			this.facturable = facturable;
		}
		public String getPeriodoEje() {
			return periodoEje;
		}
		public void setPeriodoEje(String periodoEje) {
			this.periodoEje = periodoEje;
		}
		public String getPeriodoFac() {
			return periodoFac;
		}
		public void setPeriodoFac(String periodoFac) {
			this.periodoFac = periodoFac;
		}
		public String getObservacion() {
			return observacion;
		}
		public void setObservacion(String observacion) {
			this.observacion = observacion;
		}
		public String getCliente() {
			return cliente;
		}
		public void setCliente(String cliente) {
			this.cliente = cliente;
		}
		public String getMesEjecucion() {
			return mesEjecucion;
		}
		public void setMesEjecucion(String mesEjecucion) {
			this.mesEjecucion = mesEjecucion;
		}
		public String getAnioEjecucion() {
			return anioEjecucion;
		}
		public void setAnioEjecucion(String anioEjecucion) {
			this.anioEjecucion = anioEjecucion;
		}
		public String getMesFacturacion() {
			return mesFacturacion;
		}
		public void setMesFacturacion(String mesFacturacion) {
			this.mesFacturacion = mesFacturacion;
		}
		public String getAnioFacturacion() {
			return anioFacturacion;
		}
		public void setAnioFacturacion(String anioFacturacion) {
			this.anioFacturacion = anioFacturacion;
		}
	}

}

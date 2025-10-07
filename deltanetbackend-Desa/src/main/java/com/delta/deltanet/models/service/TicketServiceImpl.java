package com.delta.deltanet.models.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.IAutenticacionUsuarioDao;
import com.delta.deltanet.models.dao.ITicketDao;
import com.delta.deltanet.models.entity.AutenticacionUsuario;
import com.delta.deltanet.models.entity.Ticket;

@Service
public class TicketServiceImpl implements ITicketService {
	
	@Autowired
	private ITicketDao ticketDao;
	@Autowired
	private IAutenticacionUsuarioDao autenticacionUsuarioDao;
	@PersistenceContext
    private EntityManager entityManager;

	@Override
	public List<Ticket> findAll() {
		return ticketDao.findAll();
	}

	@Override
	public Ticket findById(Long id) {
		return ticketDao.findById(id).orElse(null);
	}

	@Override
	public Ticket save(Ticket Ticket) {
		return ticketDao.save(Ticket);
	}

	@Override
	public void delete(Long id) {
		ticketDao.deleteById(id);
	}

	@Override
	public List<Ticket> findAllNoResueltosByUsuarioCreador(Long id) {
		return ticketDao.findAllNoResueltosByUsuarioCreador(id);
	}

	@Override
	public List<Ticket> findAllByAutenticacionUsuario(Long id) {
		return ticketDao.findAllByAutenticacionUsuario(id);
	}

	@Override
	public List<Ticket> findAllByAutenticacionUsuarioNull() {
		return ticketDao.findAllByAutenticacionUsuarioNull();
	}

	@Override
	public List<Ticket> findAllResueltos(String usuario, Long usuarioServicioId, Long areaId) {
		return ticketDao.findAllResueltos(usuario, usuarioServicioId, areaId);
	}

	@Override
	public List<Ticket> findAllResueltosAdmin(Long areaId) {
		return ticketDao.findAllResueltosAdmin(areaId);
	}

	@Override
	public List<Ticket> findAllModificados(String usuario, Long usuarioServicioId) {
		return ticketDao.findAllModificados(usuario, usuarioServicioId);
	}

	@Override
	public List<Ticket> findAllModificados() {
		return ticketDao.findAllModificados();
	}

	@Override
	public List<Ticket> findAllByUsuarioCreador(String usuario) {
		return ticketDao.findAllByUsuarioCreador(usuario);
	}

	@Override
	public List<Ticket> findAllResueltosByUsuarioCreador(String usuario) {
		return ticketDao.findAllResueltosByUsuarioCreador(usuario);
	}


	@Override
	public List<Ticket> findAllFiltro(Long idAreaOrigen, Long idAreaDestino, String tipoUsuario, Long idPrioridad,
			Long idCategoria, Long idCatalogoServicio, String usuarioCrea) {
		
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
        Root<Ticket> ticket = query.from(Ticket.class);
        
        AutenticacionUsuario usuario = autenticacionUsuarioDao.findByUsuario(usuarioCrea);
        
        boolean allNull = true;
        
        List<Predicate> andPredicates = new ArrayList<>();
        
        if(idAreaOrigen!=null) {
        	andPredicates.add(cb.equal(ticket.get("areaOrigen").get("id"), idAreaOrigen));
        	allNull = false;
        }
        if(idAreaDestino!=null) {
        	andPredicates.add(cb.equal(ticket.get("areaDestino").get("id"), idAreaDestino));
        	allNull = false;
        }
        if(tipoUsuario!=null) {
        	andPredicates.add(cb.equal(ticket.get("tipoUsuarioCreador"), tipoUsuario));
        	allNull = false;
        }
        if(idPrioridad!=null) {
        	andPredicates.add(cb.equal(ticket.get("prioridad").get("id"), idPrioridad));
        	allNull = false;
        }
        if(idCategoria!=null) {
        	andPredicates.add(cb.equal(ticket.get("categoria").get("id"), idCategoria));
        	allNull = false;
        }
        if(idCatalogoServicio!=null) {
        	andPredicates.add(cb.equal(ticket.get("catalogoServicio").get("id"), idCatalogoServicio));
        	allNull = false;
        }
        
        Predicate predicateForUser = cb.equal(ticket.get("usuarioCreador"), usuarioCrea);
        
        if(allNull) {
        	query.select(ticket).where(predicateForUser);
        }else {
    		query.select(ticket).where(cb.or(predicateForUser, cb.and(andPredicates.toArray(new Predicate[andPredicates.size()]))));
        }
        
        return entityManager.createQuery(query).getResultList();
        
	}
	
	@Override
	public Map<String, Object> findAllFiltroPaginado(Long idAreaOrigen, Long idAreaDestino, Long usuarioServicio, Long idPrioridad,
			Long idCategoria, Long idCatalogoServicio, Long idTicket, String usuarioCrea, String startDate, String endDate) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Ticket> query = cb.createQuery(Ticket.class);
        Root<Ticket> ticket = query.from(Ticket.class);
        AutenticacionUsuario usuario = null;
        Map<String, Object> objects = new HashMap<>();
        boolean allNull = true;

        List<Predicate> andPredicates = new ArrayList<>();
        if(usuarioCrea != null && usuarioCrea != "") {
        	usuario = autenticacionUsuarioDao.findByUsuario(usuarioCrea);
        	andPredicates.add(cb.equal(ticket.get("usuarioCreador"), usuarioCrea));
        	allNull = false;
        }

				if(startDate != null && endDate != null) {
						SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat timestampFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						try {
								Date start = dateFormatter.parse(startDate);
								Date end = dateFormatter.parse(endDate);

								// Set time to 00:00:00 for startDate
								Calendar calStart = Calendar.getInstance();
								calStart.setTime(start);
								calStart.set(Calendar.HOUR_OF_DAY, 0);
								calStart.set(Calendar.MINUTE, 0);
								calStart.set(Calendar.SECOND, 0);

								// Set time to 23:59:59 for endDate
								Calendar calEnd = Calendar.getInstance();
								calEnd.setTime(end);
								calEnd.set(Calendar.HOUR_OF_DAY, 23);
								calEnd.set(Calendar.MINUTE, 59);
								calEnd.set(Calendar.SECOND, 59);

								String startTimestamp = timestampFormatter.format(calStart.getTime());
								String endTimestamp = timestampFormatter.format(calEnd.getTime());

								andPredicates.add(cb.between(ticket.get("fechaCreado"),
																						cb.literal(Timestamp.valueOf(startTimestamp)),
																						cb.literal(Timestamp.valueOf(endTimestamp))));
								allNull = false;
						} catch (ParseException e) {
								e.printStackTrace();
						}
				}
        if(idAreaOrigen!=null) {
        	andPredicates.add(cb.equal(ticket.get("areaOrigen").get("id"), idAreaOrigen));
        	allNull = false;
        }
        if(idAreaDestino!=null) {
        	andPredicates.add(cb.equal(ticket.get("areaDestino").get("id"), idAreaDestino));
        	allNull = false;
        }
        if(usuarioServicio!=null) {
        	andPredicates.add(cb.equal(ticket.get("autenticacionUsuario").get("id"), usuarioServicio));
        	allNull = false;
        }
        if(idPrioridad!=null) {
        	andPredicates.add(cb.equal(ticket.get("prioridad").get("id"), idPrioridad));
        	allNull = false;
        }
        if(idCategoria!=null) {
        	andPredicates.add(cb.equal(ticket.get("categoria").get("id"), idCategoria));
        	allNull = false;
        }
        if(idCatalogoServicio!=null) {
        	andPredicates.add(cb.equal(ticket.get("catalogoServicio").get("id"), idCatalogoServicio));
        	allNull = false;
        }
        if(idTicket!=null) {
        	andPredicates.add(cb.equal(ticket.get("id"), idTicket));
        	allNull = false;
        }
        
        if(allNull) {
        	query.select(ticket).orderBy(cb.desc(ticket.get("id")));
        }else {
    		query.select(ticket).where(cb.and(cb.and(andPredicates.toArray(new Predicate[andPredicates.size()])))).orderBy(cb.desc(ticket.get("id")));
        }

        Long total = Long.valueOf(entityManager.createQuery(query).getResultList().size());
        List<Ticket> tickets = entityManager.createQuery(query).getResultList();
        for(Ticket tk: tickets) {
			if(tk.getAreaDestino().getGerencia() != null) {
				tk.getAreaDestino().getGerencia().setOrgAreas(null);
				tk.getAreaDestino().setPuestos(null);
			}
			if(tk.getAreaOrigen().getGerencia() != null) {
				tk.getAreaOrigen().getGerencia().setOrgAreas(null);
				tk.getAreaOrigen().setPuestos(null);
			}

			if(tk.getAutenticacionUsuario().getPersona().getArea() != null) {
				tk.getAutenticacionUsuario().getPersona().getArea().getGerencia().setOrgAreas(null);
				tk.getAutenticacionUsuario().getPersona().getArea().setPuestos(null);
			}
			if(tk.getAutenticacionUsuario().getPersona().getPuesto() != null) {
				tk.getAutenticacionUsuario().getPersona().getPuesto().setArea(null);
			}
		}
        objects.put("tickets", tickets);
        objects.put("total", total);
        
        return objects;
        
	}

	@Override
	public List<Ticket> findAllporCerrar(Date fecha) {
		return ticketDao.findAllporCerrar(fecha);
	}

	@Override
	public List<Ticket> findAllporCerrar(Date fecIni, Date fecFin) {
		return ticketDao.findAllporCerrar(fecIni, fecFin);
	}

	@Override
	public List<Ticket> findAllporCerrar2(Date fecFin) {
		return ticketDao.findAllporCerrar2(fecFin);
	}

}

package com.delta.deltanet.models.service;

import com.delta.deltanet.models.dao.IGerenciaDao;
import com.delta.deltanet.models.dao.IOrgAreasDao;
import com.delta.deltanet.models.dao.IRelacionDao;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.PersonaJuridica;
import com.delta.deltanet.models.entity.PersonaNatural;
import com.delta.deltanet.models.entity.Relacion;
import com.delta.deltanet.models.entity.RelacionDTO;
import com.delta.deltanet.models.entity.RelacionTrabajadorDatosDTO;
import com.delta.deltanet.models.entity.TipoRelacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Objects;

@Service
public class RelacionServiceImpl implements IRelacionService {

    @Autowired
    private IRelacionDao relacionDao;

    @Autowired
    private IOrgAreasDao orgAreasDao;

    @Autowired
    private IGerenciaDao gerenciaDao;

    @Override
    public List<Object> listaRelaciones(Long idPer) {
        return relacionDao.listaRelaciones(idPer);
    }

    @Override
    public Long nroContratos(Long idRel) {
        return relacionDao.nroContratos(idRel);
    }

    @Override
    public Relacion save(Relacion relacion) {
        return relacionDao.save(relacion);
    }

    @Override
    public Relacion buscaId(Long idRel) {
        return relacionDao.buscaId(idRel);
    }

    @Override
    public List<Object> busFiltrada(Long idPer) {
        return relacionDao.busFiltrada(idPer);
    }

    @Override
    public List<Object> busFiltrada(Long idPer, Long idTipoRel) {
        return relacionDao.busFiltrada(idPer, idTipoRel);
    }

    @Override
    public List<Object> busFiltrada(Long idPer, Long idTipoRel, Date fecIni, Date fecFin) {
        return relacionDao.busFiltrada(idPer, idTipoRel, fecIni, fecFin);
    }

    @Override
    public List<Object> busFiltrada(Long idPer, Long idTipoRel, Date fecIni, Date fecFin, Long estado) {
        return relacionDao.busFiltrada(idPer, idTipoRel, fecIni, fecFin, estado);
    }

    @Override
    public List<Object> busFiltrada(Long idPer, Long idTipoRel, Long estado) {
        return relacionDao.busFiltrada(idPer, idTipoRel, estado);
    }

    @Override
    public List<Object> busFiltrada(Long idPer, Date fecIni, Date fecFin) {
        return relacionDao.busFiltrada(idPer, fecIni, fecFin);
    }

    @Override
    public List<Object> busFiltrada(Long idPer, Date fecIni, Date fecFin, Long estado) {
        return relacionDao.busFiltrada(idPer, fecIni, fecFin, estado);
    }

    @Override
    public List<Object> busFiltrada4(Long idPer, Long estado) {
        return relacionDao.busFiltrada4(idPer, estado);
    }

    @Override
    public List<Object> busFiltrada2(Long idPer, Long estado) {
        return relacionDao.busFiltrada2(idPer, estado);
    }

    @Override
    public List<Object> busFiltrada3(Long idPer, Long idArea, Long estado) {
        return relacionDao.busFiltrada3(idPer, idArea, estado);
    }

    @Override
    public List<Object> listaPersonal() {
        return relacionDao.listaPersonal();
    }

    @Override
    public List<Object> listaOutsourcing() {
        return relacionDao.listaOutsourcing();
    }

    @Override
    public Long idArea(Long idRel) {
        return relacionDao.findIdAreaByIdRel(idRel);
    }

    @Override
    public Optional<Long> findGerenciaIdByAreaId(Long idArea) {
        return orgAreasDao.findGerenciaIdByAreaId(idArea);
    }

    @Override
    public String findAreaNameById(Long idArea) {
        return orgAreasDao.findAreaNameById(idArea);
    }

    @Override
    public String findGerenciaNameById(Long idGerencia) {
        return gerenciaDao.findGerenciaNameById(idGerencia);
    }

    @Override
    public List<Relacion> buscaPorArea(Long idArea) {
        return relacionDao.buscaPorArea(idArea);
    }

    @Override
    public Relacion buscaReverse(Long NodoOrigen, Long NodoDestino, Long idTipo) {
        return relacionDao.buscaReverse(NodoOrigen, NodoDestino, idTipo);
    }

    @Override
    public List<RelacionDTO> findRelacionesForContrato() {
        List<Object[]> resultados = relacionDao.findRelacionesForContrato();

        return resultados.stream()
                .map(this::convertToRelacionDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RelacionDTO> findRelacionesForContratoOutsourcing() {
    	List<Object[]> resultados = relacionDao.listaPersonasOutsourcing();
        
        return resultados.stream()
                .map(this::convertToRelacionDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<RelacionDTO> findRelacionesClientesForContratoOutsourcing() {
    	List<Object[]> resultados = relacionDao.listaClientesOutsourcing();
        
        return resultados.stream()
                .map(this::convertToRelacionDTO)
                .collect(Collectors.toList());
    }

    private RelacionDTO convertToRelacionDTO(Object[] result) {
        RelacionDTO dto = new RelacionDTO();

        Long idRel = (Long) result[0];
        dto.setIdRel(idRel);
        TipoRelacion tipoRel = (TipoRelacion) result[2];
        Persona persona = (Persona) result[1];
        dto.setIdPersona(persona.getId());

        if (persona != null && persona.getTipoPer() != null) {
            if (persona.getTipoPer().getIdTipoPer() == 1L) {
                // Persona Natural
                PersonaNatural perNat = persona.getPerNat();
                if (perNat != null) {
                    String nombreCompleto = Stream.of(
                            perNat.getNombre(),
                            perNat.getApePaterno(),
                            perNat.getApeMaterno())
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining(" "));
                    dto.setNombrePersona(nombreCompleto + " (" +
                            (tipoRel != null ? tipoRel.getDescrip() : "") + ")");
                }
            } else if (persona.getTipoPer().getIdTipoPer() == 2L) {
                // Persona Jurídica
                PersonaJuridica perJur = persona.getPerJur();
                if (perJur != null) {
                    dto.setNombrePersona(perJur.getRazonSocial() + " (" +
                            (tipoRel != null ? tipoRel.getDescrip() : "") + ")");
                }
            }
        }

        return dto;
    }

    @Override
    public List<Relacion> findByNodoOrigen(Long idNodoOrigen) {
        return relacionDao.findByNodoOrigen(idNodoOrigen);
    }

	@Override
	public List<RelacionTrabajadorDatosDTO> searchTrabajadorOutsourcing(String nombre, String documento) {
		List<Object[]> resultados = relacionDao.listaPersonasOutsourcingNombreODocumento(nombre, documento);
        
        return resultados.stream()
                .map(this::convertToRelacionTrabajadorDatosDTO)
                .collect(Collectors.toList());
	}
	
	private RelacionTrabajadorDatosDTO convertToRelacionTrabajadorDatosDTO(Object[] result) {
        RelacionTrabajadorDatosDTO dto = new RelacionTrabajadorDatosDTO();

        Long idRel = (Long) result[0];
        dto.setIdRel(idRel);
        // TipoRelacion tipoRel = (TipoRelacion) result[2];
        Persona persona = (Persona) result[1];
        dto.setIdPersona(persona.getId());

        if (persona != null && persona.getTipoPer() != null) {
            if (persona.getTipoPer().getIdTipoPer() == 1L) {
                dto.setDocumento(persona.getDocumento());
                dto.setTipoDoc(persona.getTipoDoc().getNombre());
            	// Persona Natural
                PersonaNatural perNat = persona.getPerNat();
                if (perNat != null) {
                    dto.setNombres(perNat.getNombre());
                    dto.setApellidos(perNat.getApePaterno() + " " + perNat.getApeMaterno());
                }
            } 
        }
        dto.setFechaFin(result[2] != null ? result[2].toString() : "");
        return dto;
	}

	@Override
	public List<Relacion> buscarRelacionContactos(Long idPer, Long idTipoNodoDestino, String nombre, Long idMotivo, List<Integer> tipoRelTipos, Long tipoDoc, String nroDoc, Long estado) {
		List<Relacion> lista = relacionDao.buscarRelacionContactos(idPer, idTipoNodoDestino, idMotivo, tipoRelTipos, tipoDoc, nroDoc, estado);
		if(nombre != null) {
			lista = lista.stream().filter(r -> isNombre(r, nombre)).collect(Collectors.toList());
		}
		return lista;
	}

	private boolean isNombre(Relacion rel, String nombre) {
		if(rel.getPersona().getTipoPer().getIdTipoPer() == 1) {
			String nombreCompleto = rel.getPersona().getPerNat().getNombre() + " " + rel.getPersona().getPerNat().getApePaterno() + " " + rel.getPersona().getPerNat().getApeMaterno();
			return nombreCompleto.toLowerCase().contains(nombre.toLowerCase());
		}else {
			String razonSocYCom = rel.getPersona().getPerJur().getRazonSocial() + " " + rel.getPersona().getPerJur().getRazonComercial();
			return razonSocYCom.toLowerCase().contains(nombre.toLowerCase());
		}
	}

	@Override
	public Relacion saveAndFlush(Relacion relacion) {
		return relacionDao.saveAndFlush(relacion);
	}

    @Override
    public Relacion findByOrigenDestinoTipoEstado(Long idOrigen, Long idDestino, Long idTipoRel, Long estado) {
        return relacionDao.findByPersonaIdAndPersonaDIdAndTipoRelIdAndEstado(idOrigen, idDestino, idTipoRel, estado);
    }

    @Override
    public List<Relacion> searchRelCto(Long idPer) {
        return relacionDao.searchRelCto(idPer);
    }

}

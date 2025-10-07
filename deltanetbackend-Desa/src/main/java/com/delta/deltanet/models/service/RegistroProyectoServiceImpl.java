package com.delta.deltanet.models.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Date;
import java.util.stream.Collectors;

import com.delta.deltanet.models.dao.AdmCronogramaRepository;
import com.delta.deltanet.models.dao.IContratoDao;
import com.delta.deltanet.models.dao.IPersonaDao;
import com.delta.deltanet.models.dao.IPersonaJuridicaDao;
import com.delta.deltanet.models.dao.IPersonaNaturalDao;
import com.delta.deltanet.models.dao.IRelacionDao;
import com.delta.deltanet.models.dao.RegistroProyectoRepository;
import com.delta.deltanet.models.entity.AdmCronograma;
import com.delta.deltanet.models.entity.Contrato;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.PersonaJuridica;
import com.delta.deltanet.models.entity.PersonaNatural;
import com.delta.deltanet.models.entity.RegistroProyecto;
import com.delta.deltanet.models.entity.Relacion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RegistroProyectoServiceImpl implements RegistroProyectoService {

	private final AdmCronogramaRepository admCronogramaRepository;
	private final RegistroProyectoRepository registroProyectoRepository;
	private final IContratoDao contratoRepository;
	private final IRelacionDao relacionRepository;
	private final IPersonaDao personaRepository;
	private final IPersonaJuridicaDao personaJuridicaRepository;
	private final IPersonaNaturalDao personaNaturalRepository;

	public RegistroProyectoServiceImpl(AdmCronogramaRepository admCronogramaRepository,
			RegistroProyectoRepository registroProyectoRepository, IContratoDao contratoRepository,
			IRelacionDao relacionRepository, IPersonaDao personaRepository,
			IPersonaJuridicaDao personaJuridicaRepository,
			IPersonaNaturalDao personaNaturalRepository) {
		this.admCronogramaRepository = admCronogramaRepository;
		this.registroProyectoRepository = registroProyectoRepository;
		this.contratoRepository = contratoRepository;
		this.relacionRepository = relacionRepository;
		this.personaRepository = personaRepository;
		this.personaJuridicaRepository = personaJuridicaRepository;
		this.personaNaturalRepository = personaNaturalRepository;
	}
	
	@Override
	public List<RegistroProyecto> findAll() {
		return registroProyectoRepository.findAll();
	}

	@Override
	public Optional<RegistroProyecto> findById(Long id) {
		return registroProyectoRepository.findById(id);
	}

	@Override
	public Optional<RegistroProyecto> findByCodigo(String codigo) {
		return registroProyectoRepository.findByCodigo(codigo);
	}

	// @Override
	// public RegistroProyecto save(RegistroProyecto registroProyecto) {
	// // Asignamos valores predeterminados si no se proporcionan
	// if (registroProyecto.getEstado() == 0) {
	// registroProyecto.setEstado(0);
	// }
	// if (registroProyecto.getCreateDate() == null) {
	// registroProyecto.setCreateDate(LocalDateTime.now());
	// }
	// return registroProyectoRepository.save(registroProyecto);
	// }
	@Override
	public RegistroProyecto save(Long idPersona, String codigo, String proyectoDescripcion, String createUser) {
		RegistroProyecto nuevoRegistro = new RegistroProyecto(idPersona, codigo, proyectoDescripcion, 1, createUser,
				new Date());
		return registroProyectoRepository.save(nuevoRegistro);
	}

	@Override
	public Optional<RegistroProyecto> updateRegistroProyecto(Long idProyecto, String proyectoDescripcion,
			String updateUser) {
		Optional<RegistroProyecto> registroOptional = registroProyectoRepository.findById((long) idProyecto);

		if (registroOptional.isPresent()) {
			RegistroProyecto registro = registroOptional.get();
			registro.setProyectoDescripcion(proyectoDescripcion);
			registro.setUpdateUser(updateUser);
			registro.setUpdateDate(new Date());
			return Optional.of(registroProyectoRepository.save(registro));
		} else {
			return Optional.empty(); // Registro no encontrado
		}
	}

	// @Override
	// public Map<String, Object> changeStatus(int idCronograma, int idProyecto,
	// String updateUser) {
	// Optional<RegistroProyecto> registroOptional =
	// registroProyectoRepository.findById(new RegistroProyectoId(idCronograma,
	// idProyecto));
	// Map<String, Object> response = new HashMap<>();

	// if (registroOptional.isPresent()) {
	// RegistroProyecto registro = registroOptional.get();
	// int estadoActual = registro.getEstado();
	// int nuevoEstado = 0;

	// switch (estadoActual) {
	// case 0:
	// nuevoEstado = 1;
	// break;
	// case 1:
	// nuevoEstado = 2;
	// break;
	// case 2:
	// nuevoEstado = 1;
	// break;
	// }

	// registro.setEstado(nuevoEstado);
	// registro.setUpdateUser(updateUser);
	// registro.setUpdateDate(LocalDateTime.now());

	// RegistroProyecto registroActualizado =
	// registroProyectoRepository.save(registro);

	// response.put("message", (nuevoEstado == 1) ? "Activado con éxito" :
	// "Desactivado con éxito");
	// response.put("registro", registroActualizado);
	// } else {
	// response.put("message", "Registro no encontrado");
	// }

	// return response;
	// }

	@Override
	public Map<String, Object> delete(Long idProyecto) {
		Optional<RegistroProyecto> registroOptional = registroProyectoRepository.findById(idProyecto);
		Map<String, Object> response = new HashMap<>();

		if (registroOptional.isPresent()) {
			RegistroProyecto registro = registroOptional.get();
			int estado = registro.getEstado();

			if (estado == 1) {
				registroProyectoRepository.delete(registro);
				response.put("message", "Registro eliminado definitivamente");
			} else {
				response.put("message", "Este registro no se puede eliminar definitivamente.");
			}
		} else {
			response.put("message", "Registro no encontrado");
		}

		return response;
	}

	@Override
	public ResponseEntity<?> activate(List<Long> proyects, String updateUser) {
		Map<String, Object> successResponse = new HashMap<>();
		boolean allRecordsHaveStateZero = true;

		// Verificar si todos los registros tienen estado 0
		for (Long proyectData : proyects) {
			Optional<RegistroProyecto> registroOptional = registroProyectoRepository.findById(proyectData);
			if (registroOptional.isPresent()) {
				RegistroProyecto registro = registroOptional.get();
				int estadoActual = registro.getEstado();
				if (estadoActual != 3) {
					// Al menos uno tiene un estado diferente de 0
					allRecordsHaveStateZero = false;
					break;
				}
			}
		}

		// Verificar si todos los registros tienen estado 3 antes de realizar cambios
		if (!allRecordsHaveStateZero) {
			successResponse.put("message", "Error! No se pudo activar los registros seleccionados.");
			return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);
		} else {
			// Todos los registros tienen estado 3, proceder con la activación
			for (Long proyectData : proyects) {
				Optional<RegistroProyecto> registroOptional = registroProyectoRepository.findById(proyectData);
				if (registroOptional.isPresent()) {
					RegistroProyecto registro = registroOptional.get();
					registro.setEstado(1);
					registro.setUpdateUser(updateUser);
					registro.setUpdateDate(new Date());
					registroProyectoRepository.save(registro);
				}
			}
		}
		// Mensaje general de éxito
		successResponse.put("message", "Los registros se activaron exitosamente.");
		return new ResponseEntity<>(successResponse, HttpStatus.OK);
	}

	@Override
	public List<AdmCronograma> listCronograma(int idConsultor) {
		return admCronogramaRepository.findByIdConsultor(idConsultor);
	}

	@Override
	public List<AdmCronograma> findCronograma(Integer idConsultor) {
		return admCronogramaRepository.findByIdConsultorAndEstado(idConsultor, 1);
	}

	// @Override
	// public List<RegistroProyecto> index(int idConsultor) {
	// // Obtener los ids de AdmCronograma filtrados por id_consultor
	// List<Integer> admCronogramaIds =
	// admCronogramaRepository.findIdsByIdConsultor(idConsultor);

	// // Filtrar los registros de RegistroProyecto por los ids de AdmCronograma
	// return registroProyectoRepository.findByCronogramaIds(admCronogramaIds);
	// }

	// @Override
	// public List<Map<String, Object>> show(int idConsultor, Optional<Integer>
	// idCronograma,
	// Optional<String> proyectoDescripcion, Optional<Integer> estado) {
	// // Obtener los ids de AdmCronograma filtrados por id_consultor
	// List<Integer> admCronogramaIds =
	// admCronogramaRepository.findIdsByIdConsultor(idConsultor);

	// // Filtrar los registros de RegistroProyecto por los ids de AdmCronograma
	// List<RegistroProyecto> registros =
	// registroProyectoRepository.findByCronogramaIds(admCronogramaIds);

	// // Aplicar filtros adicionales
	// if (idCronograma.isPresent()) {
	// registros = registros.stream()
	// .filter(registro -> registro.getId().getIdCronograma() == idCronograma.get())
	// .collect(Collectors.toList());
	// }

	// if (proyectoDescripcion.isPresent()) {
	// registros = registros.stream()
	// .filter(registro ->
	// registro.getProyectoDescripcion().contains(proyectoDescripcion.get()))
	// .collect(Collectors.toList());
	// }

	// if (estado.isPresent() && estado.get() == 9) {
	// // Si el estado es 9, no aplicar filtro por estado
	// } else if (estado.isPresent()) {
	// registros = registros.stream()
	// .filter(registro -> registro.getEstado() == estado.get())
	// .collect(Collectors.toList());
	// }

	// // Mapear los resultados a una lista de mapas
	// return registros.stream()
	// .map(registro -> {
	// Map<String, Object> result = new HashMap<>();
	// result.put("id_cronograma", registro.getId().getIdCronograma());
	// result.put("id_proyecto", registro.getId().getIdProyecto());
	// result.put("proyecto_descripcion", registro.getProyectoDescripcion());
	// result.put("estado", registro.getEstado());
	// result.put("create_user", registro.getCreateUser());
	// result.put("create_date", registro.getCreateDate());
	// result.put("update_user", registro.getUpdateUser());
	// result.put("update_date", registro.getUpdateDate());
	// return result;
	// })
	// .collect(Collectors.toList());
	// }

	@Override
	public List<Map<String, Object>> index(Long idConsultor, Optional<String> proyectoDescripcion,
			Optional<Integer> estado) {
		// Filtrar los registros de RegistroProyecto por los ids de AdmCronograma
		List<RegistroProyecto> registros = registroProyectoRepository.findByCronogramaIds(idConsultor);

		if (proyectoDescripcion.isPresent()) {
			registros = registros.stream().filter(registro -> registro.getProyectoDescripcion().toLowerCase()
					.contains(proyectoDescripcion.get().toLowerCase())).collect(Collectors.toList());
		}

		if (estado.isPresent() && estado.get() == 9) {
			// Si el estado es 9, no aplicar filtro por estado
		} else if (estado.isPresent()) {
			registros = registros.stream().filter(registro -> registro.getEstado() == estado.get())
					.collect(Collectors.toList());
		}

		// Mapear los resultados a una lista de mapas con descripción de cronograma
		return registros.stream().map(registro -> {
			Map<String, Object> result = new HashMap<>();
			result.put("id", registro.getId());
			result.put("codigo", registro.getCodigo());
			result.put("proyectoDescripcion", registro.getProyectoDescripcion());
			result.put("estado", registro.getEstado());
			result.put("createUser", registro.getCreateUser());
			result.put("createDate", registro.getCreateDate());
			result.put("updateUser", registro.getUpdateUser());
			result.put("updateDate", registro.getUpdateDate());
			return result;
		}).collect(Collectors.toList());
	}

	public String getCliente(int idCronograma) {
		Optional<AdmCronograma> cronogramaOptional = admCronogramaRepository.findById(idCronograma);
		if (cronogramaOptional.isPresent()) {
			Integer idContrato = cronogramaOptional.get().getIdContrato();

			Optional<Contrato> contratoOptional = contratoRepository.findById((long) idContrato);
			if (contratoOptional.isPresent()) {
				Long idRelacion = contratoOptional.get().getIdRelacion();

				Optional<Relacion> relationOptional = relacionRepository.findById(idRelacion);
				if (relationOptional.isPresent()) {
					Long idPersona = relationOptional.get().getPersona().getId();

					Optional<Persona> personOptional = personaRepository.findById(idPersona);
					if (personOptional.isPresent()) {
						Long idPerJu = personOptional.get().getPerJur().getIdPerJur();

						Optional<PersonaJuridica> personaJuridicaOptional = personaJuridicaRepository.findById(idPerJu);
						if (personaJuridicaOptional.isPresent()) {
							String razonSocial = personaJuridicaOptional.get().getRazonSocial();
							return razonSocial;
						}
					}
				}
			}
		}

		return "---";
	}

	public String obtenerNombreSupervisor(int idConsultor) {
		Long idConsultorLong = Long.valueOf(idConsultor);

		// Buscar la persona supervisora directamente en personaRepository
		Optional<Persona> personaOptional = personaRepository.findById(idConsultorLong);

		if (personaOptional.isPresent() && personaOptional.get().getPerNat() != null) {
			Persona persona = personaOptional.get();
			Long idDatosPersonaNatural = persona.getPerNat().getIdPerNat();

			Optional<PersonaNatural> personaNaturalOptional = personaNaturalRepository.findById(idDatosPersonaNatural);
			if (personaNaturalOptional.isPresent()) {
				PersonaNatural personaNatural = personaNaturalOptional.get();
				String nombreFormateado = formatearNombre(personaNatural.getNombre(),
						personaNatural.getApePaterno(), personaNatural.getApeMaterno());
				return nombreFormateado;
			}
		}
		return "---";
	}

	public String formatearNombre(String nombres, String apePaterno, String apeMaterno) {
		String primerNombre = nombres.split("\\s+")[0]; // Obtener el primer nombre
		primerNombre = Character.toUpperCase(primerNombre.charAt(0)) + primerNombre.substring(1).toLowerCase();

		String apellidoFormateado = "";
		if (apePaterno != null && !apePaterno.isEmpty()) {
			apellidoFormateado += " " + Character.toUpperCase(apePaterno.charAt(0))
					+ apePaterno.substring(1).toLowerCase();
		}
		if (apeMaterno != null && !apeMaterno.isEmpty()) {
			apellidoFormateado += " " + Character.toUpperCase(apeMaterno.charAt(0))
					+ apeMaterno.substring(1).toLowerCase();
		}

		return primerNombre + apellidoFormateado;
	}

	public String getSupervisor(int idCronograma) {
		Optional<AdmCronograma> cronogramaOptional = admCronogramaRepository.findById(idCronograma);
		if (cronogramaOptional.isPresent()) {
			Integer idContrato = cronogramaOptional.get().getIdContrato();

			Optional<Contrato> contratoOptional = contratoRepository.findById((long) idContrato);
			if (contratoOptional.isPresent()) {
				Long idRelacion = contratoOptional.get().getIdRelacion();

				Optional<Relacion> relationOptional = relacionRepository.findById(idRelacion);
				if (relationOptional.isPresent()) {
					Long idPersona = relationOptional.get().getPersona().getId();

					Optional<Persona> personOptional = personaRepository.findById(idPersona);
					if (personOptional.isPresent()) {
						Long idPerJu = personOptional.get().getPerJur().getIdPerJur();

						Optional<PersonaJuridica> personaJuridicaOptional = personaJuridicaRepository.findById(idPerJu);
						if (personaJuridicaOptional.isPresent()) {
							String razonSocial = personaJuridicaOptional.get().getRazonSocial();
							return razonSocial;
						}
					}
				}
			}
		}

		return "---";
	}

	@Override
	public Map<String, Object> changeStatus(Long idProyecto, String update_user) {
		Optional<RegistroProyecto> registroOptional = registroProyectoRepository.findById(idProyecto);

		Map<String, Object> response = new HashMap<>();

		if (registroOptional.isPresent()) {
			RegistroProyecto registro = registroOptional.get();
			int estadoActual = registro.getEstado();

			// Cambiar el estado y actualizar la fecha si el estado no es 0
			if (estadoActual == 1) {
				registro.setEstado(2);
				response.put("message", "Desactivado con éxito");
			} else if (estadoActual == 2) {
				registro.setEstado(1);
				response.put("message", "Activado con éxito");
			} else if (estadoActual == 0) {
				response.put("message", "Error! El registro necesita ser activado.");
			}

			// Actualizar el usuario y la fecha solo si el estado no es 0
			if (estadoActual != 0) {
				registro.setUpdateUser(update_user);
				registro.setUpdateDate(new Date());
				// Guardar el registro actualizado
				registroProyectoRepository.save(registro);
			}

			// Listar el registro actualizado
			response.put("registro", getRegistroMap(registro));
		} else {
			response.put("message", "Registro no encontrado");
		}

		return response;
	}

	// Método auxiliar para obtener un mapa con los detalles del registro
	private Map<String, Object> getRegistroMap(RegistroProyecto registro) {
		Map<String, Object> registroMap = new HashMap<>();
		// registroMap.put("id_cronograma", registro.getId().getIdCronograma());
		registroMap.put("id_proyecto", registro.getId());
		registroMap.put("proyecto_descripcion", registro.getProyectoDescripcion());
		registroMap.put("estado", registro.getEstado());
		registroMap.put("create_user", registro.getCreateUser());
		registroMap.put("create_date", registro.getCreateDate());
		registroMap.put("update_user", registro.getUpdateUser());
		registroMap.put("update_date", registro.getUpdateDate());
		return registroMap;
	}
}
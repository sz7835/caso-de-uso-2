package com.delta.deltanet.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.delta.deltanet.models.dao.AdmCronogramaRepository;
import com.delta.deltanet.models.dao.IContratoDao;
import com.delta.deltanet.models.dao.IPersonaDao;
import com.delta.deltanet.models.dao.IPersonaJuridicaDao;
import com.delta.deltanet.models.dao.IRelacionDao;
import com.delta.deltanet.models.dao.OutRegistroHorasRepository;
import com.delta.deltanet.models.dao.RegistroProyectoRepository;
import com.delta.deltanet.models.entity.AdmCronograma;
import com.delta.deltanet.models.entity.Contrato;
import com.delta.deltanet.models.entity.DetalleRegistro;
import com.delta.deltanet.models.entity.OutRegistroHoras;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.PersonaJuridica;
import com.delta.deltanet.models.entity.PersonaNatural;
import com.delta.deltanet.models.entity.RegistroProyecto;
import com.delta.deltanet.models.entity.Relacion;
import com.delta.deltanet.models.entity.ShowDataResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OutRegistroHorasServiceImpl implements OutRegistroHorasService {

    @Autowired
    private OutRegistroHorasRepository registroHorasRepository;

    @Autowired
    private AdmCronogramaRepository admCronogramaRepository;

    @Autowired
    private RegistroProyectoRepository registroProyectoRepository;

    @Autowired
    private IContratoDao contratoRepository;

    @Autowired
    private IRelacionDao relacionRepository;

    @Autowired
    private IPersonaDao personaRepository;

    @Autowired
    private IPersonaJuridicaDao personaJuridicaRepository;

    @Override
    public List<Object> findByPersonaCronogramaFechaEstado(int idPersona, Date fechaInicio, Date fechaFin, int estado) {
        return registroHorasRepository.findByPersonaCronogramaFechaEstado(idPersona,fechaInicio,fechaFin,estado);
    }

        @Override
    public List<Object> findByPersonaCronogramaFechaEstado2(int idPersona, Date fechaInicio, Date fechaFin, int estado) {
        return registroHorasRepository.findByPersonaCronogramaFechaEstado2(idPersona,fechaInicio,fechaFin,estado);
    }

    @Override
    public List<OutRegistroHoras> createRegistroHoras(int idProyecto, int idPersona, List<DetalleRegistro> detalles, String dia, String createUser) {
        List<OutRegistroHoras> registros = new ArrayList<>();

        for (DetalleRegistro detalle : detalles) {
            OutRegistroHoras registro = new OutRegistroHoras();
            Optional<RegistroProyecto> proyect = registroProyectoRepository.findById((long)idProyecto);
            if(proyect.isPresent()) {
                registro.setProyect(proyect.get());
            }
            Persona per = personaRepository.buscarId((long)idPersona);
            registro.setPersona(per);
            registro.setActividad(detalle.getActividad());
            registro.setHoras(detalle.getHoras());

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dia);
                registro.setDia(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            registro.setEstado(1);
            registro.setCreateUser(createUser);
            registro.setCreateDate(new Date());

            OutRegistroHoras savedRegistro = registroHorasRepository.save(registro);
            registros.add(savedRegistro);
        }

        return registros;
    }

    @Override
    public Optional<OutRegistroHoras> updateRegistroHoras(int id, String actividad, Integer horas, String update_user) {
        Optional<OutRegistroHoras> optionalRegistro = registroHorasRepository.findById(id);

        if (optionalRegistro.isPresent()) {
            OutRegistroHoras registro = optionalRegistro.get();

            // Validar y actualizar actividad si está presente y no es vacía
            if (actividad != null && !actividad.trim().isEmpty()) {
                registro.setActividad(actividad.trim());
            } else {
                throw new IllegalArgumentException("Error! El campo 'actividad' no puede estar vacío");
            }

            // Validar y actualizar horas si está presente y es un número positivo
            if (horas != null && horas >= 0) {
                registro.setHoras(horas);
            } else {
                throw new IllegalArgumentException("Error! El campo 'horas' debe ser un número positivo");
            }

            // Actualizar update_user
            registro.setUpdateUser(update_user);

            // Actualizar update_date con la fecha actual
            registro.setUpdateDate(new Date());

            return Optional.of(registroHorasRepository.save(registro));
        } else {
            throw new IllegalArgumentException("Error! Registro no encontrado");
        }
    }

    @Override
    public List<Map<String, Object>> activarRegistroHoras(List<Integer> ids, String update_user) {
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Integer id : ids) {
            Optional<OutRegistroHoras> optionalRegistro = registroHorasRepository.findById(id);

            if (optionalRegistro.isPresent()) {
                OutRegistroHoras registro = optionalRegistro.get();

                if (registro.getEstado() == 3) {
                    // Cambiar estado a 1
                    registro.setEstado(1);

                    // Actualizar update_user
                    registro.setUpdateUser(update_user);

                    // Actualizar update_date con la fecha actual
                    registro.setUpdateDate(new Date());

                    registroHorasRepository.save(registro);

                    // Crear el mapa de respuesta
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Registro con id: " + id + ", confirmado con éxito.");
                    response.put("registro", registro);

                    responseList.add(response);
                } else {
                    // Estado ya es 1, mostrar el message de error y lista el registro
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Error! El registro con id: " + id + ", ya está confirmado.");
                    response.put("registro", registro);

                    responseList.add(response);
                }
            } else {
                // Si no se encuentra el registro, mostrar el message de error
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Error! Registro con id: " + id + ", no encontrado.");

                responseList.add(response);
            }
        }

        return responseList;
    }

    @Override
    public List<Map<String, Object>> mostrarCronograma(int idCronograma) {
        List<AdmCronograma> cronogramas = admCronogramaRepository.findByIdCronograma(idCronograma);
        List<Map<String, Object>> resultados = new ArrayList<>();

        for (AdmCronograma cronograma : cronogramas) {
            Map<String, Object> cronogramaMap = new HashMap<>();
            cronogramaMap.put("id", cronograma.getId());
            cronogramaMap.put("descripcion", cronograma.getDescripcion());
            cronogramaMap.put("estado", cronograma.getEstado());
            resultados.add(cronogramaMap);
        }

        return resultados;
    }

    @Override
    public List<RegistroProyecto> mostrarProyecto(Long idPersona) {
    	return registroProyectoRepository.findByCronogramaIds(idPersona);
    }

    @Override
    public ShowDataResponse show(int id) {
        // Obtener registroHoras por ID
        OutRegistroHoras registroHoras = registroHorasRepository.findById(id).orElse(null);

        if (registroHoras == null) {
            return null; // Puedes manejar el caso de registroHoras no encontrado según tus necesidades
        }

        // Obtener cronogramas por id y id_consultor
        List<AdmCronograma> cronogramas = admCronogramaRepository.findByIdOrIdConsultor(registroHoras.getId(), registroHoras.getIdPersona());

        // Construir y retornar el objeto ShowDataResponse
        return new ShowDataResponse(registroHoras, Collections.emptyList(), cronogramas);
    }

    @Override
    public Map<String, Object> deleteRegistroHoras(int id) {
        Map<String, Object> response = new HashMap<>();

        // Obtener el registro a eliminar
        OutRegistroHoras registroHoras = registroHorasRepository.findById(id).orElse(null);

        if (registroHoras != null) {
            if (registroHoras.getEstado() == 1) {
                // Eliminar definitivamente si el estado es 3
                registroHorasRepository.delete(registroHoras);
                response.put("message", "Registro eliminado definitivamente");
            } else {
                // Estado es 1, no se puede eliminar definitivamente
                response.put("message", "Este registro no se puede eliminar definitivamente.");
            }
        } else {
            // Registro no encontrado
            response.put("message", "Registro no encontrado");
        }

        return response;
    }

    @Override
    public List<OutRegistroHoras> index(Integer idPersona, Integer meses, Integer estado, String fechaInicio, String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        List<OutRegistroHoras> resultados = new ArrayList<>();

        try {
            // Validar que solo se proporcione meses o fechaInicio/fechaFin, no ambos
            if ((meses != null && meses != 0 && meses != 99) && (fechaInicio != null || fechaFin != null)) {
                response.put("message", "Debe proporcionar o meses o fechaInicio/fechaFin, no ambos.");
                return Collections.emptyList();
            }

            // Lógica para manejar el valor de meses
            Integer mesesFiltrado = (meses == null || meses == 99) ? null : (meses >= 1 && meses <= 12) ? meses : -1;

            // Lógica para manejar el valor de estado
            int estadoFiltrado = (estado == 9) ? estado : (estado == 3 || estado == 1) ? estado : -1;

            // Consulta diferente según el caso
            List<OutRegistroHoras> registrosFiltrados;
            if (mesesFiltrado != null) {
                registrosFiltrados = registroHorasRepository.findByFiltrosWithMeses(
                        idPersona, mesesFiltrado, estadoFiltrado
                );
            } else {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            	sdf.setLenient(false);
            	Date fechaInicioDate = null;
            	Date fechaFinDate = null;
            	if (fechaInicio != null && !fechaInicio.isEmpty()) {
            		fechaInicioDate = sdf.parse(fechaInicio);
            	}

            	if (fechaFin != null && !fechaFin.isEmpty()) {
            		fechaFinDate = sdf.parse(fechaFin);
            	}
                registrosFiltrados = registroHorasRepository.findByFiltrosWithFechas(
                        idPersona, estadoFiltrado,
                        fechaInicioDate,
                        fechaFinDate
                );
            }
            resultados = registrosFiltrados;
        } catch (Exception e) {
            // Manejo de errores generales
            response.put("message", "Error en la consulta: " + e.getMessage());
            e.printStackTrace();  // Opcional: Imprimir la traza del error para el registro
        }

        // Devolver la respuesta con los mensajes y datos correspondientes
        return resultados;
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
        Long motivoSupervisor = Long.valueOf(7); // Motivo para identificar al supervisor
        // Buscar en grafo_enlace donde id_nodo_origen = idConsultor y id_enlace_motivo = motivoSupervisor
        Optional<Relacion> relacionOptional = relacionRepository.findByNodoOrigenAndMotivo(idConsultorLong, motivoSupervisor);
        if (relacionOptional.isPresent()) {
            Relacion relacion = relacionOptional.get();
            Long idPersonaSupervisor = relacion.getPersonaD().getId(); // Persona supervisora (nodo destino)
            // Buscar la información de la persona supervisora
            Optional<Persona> personaOptional = personaRepository.findById(idPersonaSupervisor);
            if (personaOptional.isPresent() && personaOptional.get().getPerNat() != null) {
                Persona personaSupervisor = personaOptional.get();
                PersonaNatural personaNatural = personaSupervisor.getPerNat();
                // Formatear el nombre del supervisor
                String nombreFormateado = formatearNombre(
                    personaNatural.getNombre(),
                    personaNatural.getApePaterno(),
                    personaNatural.getApeMaterno()
                );
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
            apellidoFormateado += " " + Character.toUpperCase(apePaterno.charAt(0)) + apePaterno.substring(1).toLowerCase();
        }
        if (apeMaterno != null && !apeMaterno.isEmpty()) {
            apellidoFormateado += " " + Character.toUpperCase(apeMaterno.charAt(0)) + apeMaterno.substring(1).toLowerCase();
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

    // @Override
    // public List<RegistroHorasResponse> index(int idPersona, int idCronograma, Integer meses, Date fechaInicio, Date fechaFin, int estado) {

    //     // Lógica para manejar el valor de meses
    //     int mesesFiltrado = (meses != null && meses == 99) ? meses : (meses != null && meses >= 1 && meses <= 12) ? meses : -1;

    //     // Lógica para manejar la elección entre meses y rango de fechas
    //     if (mesesFiltrado != -1) {
    //         fechaInicio = null;
    //         fechaFin = null;
    //     } else if (fechaInicio != null && fechaFin != null) {
    //         mesesFiltrado = -1;
    //     } else {
    //         // Manejar el caso cuando ni meses ni rango de fechas están presentes
    //         throw new IllegalArgumentException("Debe proporcionar meses o rango de fechas");
    //     }

    //     // Lógica para manejar el valor de estado
    //     int estadoFiltrado = (estado == 9) ? estado : (estado == 0 || estado == 1) ? estado : -1;

    //     // Obtener los resultados filtrados de la tabla out_registro_horas
    //     List<OutRegistroHoras> registrosFiltrados = registroHorasRepository.findByFiltros(idPersona, idCronograma, mesesFiltrado, fechaInicio, fechaFin, estadoFiltrado);

    //     // Crear una lista para almacenar los resultados finales
    //     List<RegistroHorasResponse> resultados = new ArrayList<>();

    //     // Iterar sobre los registros filtrados para construir RegistroHorasResponse
    //     for (OutRegistroHoras registro : registrosFiltrados) {
    //         // Obtener descripcionCronograma
    //         String descripcionCronograma = obtenerDescripcionCronograma(registro.getIdCronograma());

    //         // Obtener proyectoDescripcion
    //         String proyectoDescripcion = obtenerProyectoDescripcion(registro.getIdProyecto());

    //         // Crear un objeto RegistroHorasResponse y agregarlo a la lista de resultados
    //         RegistroHorasResponse response = new RegistroHorasResponse(
    //             registro.getId(), registro.getIdCronograma(), registro.getIdProyecto(),
    //             registro.getIdPersona(), registro.getActividad(), registro.getHoras(),
    //             registro.getDia(), registro.getEstado(), registro.getCreateUser(),
    //             registro.getCreateDate(), registro.getUpdateUser(), registro.getUpdateDate(),
    //             descripcionCronograma, proyectoDescripcion
    //         );
    //         resultados.add(response);
    //     }

    //     return resultados;
    // }

    public List<OutRegistroHoras> findByIdProyecto(int id){
    	return registroHorasRepository.findByIdProyecto(id);
    }

}

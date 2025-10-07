package com.delta.deltanet.models.service;


import com.delta.deltanet.models.dao.IPersonaDao;
import com.delta.deltanet.models.dao.LiquidacionCronogramaDao;
import com.delta.deltanet.models.dao.LiquidacionesDao;
import com.delta.deltanet.models.entity.AdmCronogramaHist;
import com.delta.deltanet.models.entity.Contrato;
import com.delta.deltanet.models.entity.Cronograma;
import com.delta.deltanet.models.entity.CronogramaModalidad;
import com.delta.deltanet.models.entity.LiquidacionCronograma;
import com.delta.deltanet.models.entity.LiquidacionResponseDTO;
import com.delta.deltanet.models.entity.Liquidaciones;
import com.delta.deltanet.models.entity.Persona;
import com.delta.deltanet.models.entity.TipoContrato;
import com.delta.deltanet.models.entity.TipoServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LiquidacionesServiceImpl implements LiquidacionesService {

    @Autowired
    private LiquidacionesDao liquidacionesDAO;

    @Autowired
    private IPersonaDao personaRepository;

    @Autowired
    private LiquidacionCronogramaDao liquiCronoDAO;

    @Override
    @Transactional(readOnly = true)
    public List<LiquidacionResponseDTO> buscarLiquidacionesConFiltros(Integer idCliente, Date fechaInicio, Date fechaFin, Integer estado) {
        if ((fechaInicio != null && fechaFin == null) || (fechaInicio == null && fechaFin != null)) {
            throw new IllegalArgumentException("Debe proporcionar tanto fecha de inicio como fecha de fin, o ninguna de las dos.");
        }

        List<Liquidaciones> liquidaciones = liquidacionesDAO.buscarLiquidacionesConFiltros(idCliente, fechaInicio, fechaFin, estado);
        return liquidaciones.stream()
                .map(liquidacion -> {
                    Persona persona = personaRepository.findById(Long.valueOf(liquidacion.getIdCliente()))
                            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
                    return LiquidacionResponseDTO.fromLiquidacion(liquidacion, persona);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LiquidacionResponseDTO> buscarLiquidacionesConFiltrosPaginado(Integer idCliente, Date fechaInicio, Date fechaFin, Integer estado, Pageable pageable) {
        if ((fechaInicio != null && fechaFin == null) || (fechaInicio == null && fechaFin != null)) {
            throw new IllegalArgumentException("Debe proporcionar tanto fecha de inicio como fecha de fin, o ninguna de las dos.");
        }

        Page<Liquidaciones> liquidacionesPage = liquidacionesDAO.buscarLiquidacionesConFiltrosPaginado(idCliente, fechaInicio, fechaFin, estado, pageable);
        return liquidacionesPage.map(liquidacion -> {
            Persona persona = personaRepository.findById(Long.valueOf(liquidacion.getIdCliente()))
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            return LiquidacionResponseDTO.fromLiquidacion(liquidacion, persona);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public Liquidaciones findLiquidacionById(Long id) {
        return liquidacionesDAO.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Liquidaciones save(Liquidaciones liquidacion) {
        return liquidacionesDAO.save(liquidacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Persona> findPersonasJuridicasByTipoPersona(Long tipoPersonaId, String razonSocial, String documento) {
        return liquidacionesDAO.findPersonasJuridicasByTipoPersona(tipoPersonaId, razonSocial, documento);
    }

    @Override
    public List<Contrato> getContratosByFilters(Long idTipoContrato, Long idTipoServicio, String descripcion, Long personaId) {
        return liquidacionesDAO.findByFilters(idTipoContrato, idTipoServicio, descripcion, personaId);
    }

    @Override
    public Map<String, Object> obtenerTipos() {
        Map<String, Object> response = new HashMap<>();

        // Obtener contratos activos
        List<TipoContrato> contratos = liquidacionesDAO.findByEstadoTipoContrato(1);
        response.put("contratos", contratos);

        // Obtener servicios activos
        List<TipoServicio> servicios = liquidacionesDAO.findByEstadoTipoServicio(1);
        response.put("servicios", servicios);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerDatosCronograma(Long idContrato) {
        Map<String, Object> resultado = new HashMap<>();

        List<AdmCronogramaHist> historico = liquidacionesDAO.findHistoricoByIdContrato(idContrato);
        resultado.put("historico", historico);

        List<CronogramaModalidad> modalidades = liquidacionesDAO.findAllModalidades();
        resultado.put("modalidades", modalidades);

        List<Cronograma> vigentes = liquidacionesDAO.findVigenteByIdContrato(idContrato);
        resultado.put("vigentes", vigentes);

        return resultado;
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        liquidacionesDAO.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Liquidaciones> findAll() {
        return liquidacionesDAO.findAll();
    }

    @Override
    public Optional<LiquidacionCronograma> buscaLiqCro(Long idCronograma, Long idLiquidacion) {
        return liquidacionesDAO.buscaLiqCro(idCronograma,idLiquidacion);

    }

    @Override
    public LiquidacionCronograma saveLiqCrono(LiquidacionCronograma liquidacionCronograma) {
        return liquiCronoDAO.save(liquidacionCronograma);
    }

    @Override
    public List<LiquidacionCronograma> ListCronos(Long id) {
        return liquidacionesDAO.ListCronos(id);
    }

    @Override
    @Transactional
    public void deleteCronoLiq(Long id) {
    	liquiCronoDAO.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCronoLiqs(Long id) {
    	liquiCronoDAO.deleteCronoLiqs(id);
    }

}
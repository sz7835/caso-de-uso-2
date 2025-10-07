package com.delta.deltanet.models.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.IContratoDao;
import com.delta.deltanet.models.entity.Contrato;

@Service
public class ContratoServiceImpl implements ContratoService {

    @Autowired
    private IContratoDao contratoDao;

    @Override
    @Transactional(readOnly = true)
    public Page<Contrato> searchContratos(
            Long idRelacion,
            Long idTipoContrato,
            Long idTipoServicio,
            Long idFormaPago,
            String descripcion,
            Date fecIniRng1,
            Date fecIniRng2,
            Date fecFinRng1,
            Date fecFinRng2,
            Long estado,
            Integer swPag,
            Integer column,
            String dir,
            Pageable pageable) {
        if (swPag != null && swPag == 1) {
            Sort sort = createSort(column, dir);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }else {
        	pageable = PageRequest.of(0, Integer.MAX_VALUE)
;
        }

        idRelacion = (idRelacion != null && idRelacion == 0) ? null : idRelacion;
        idTipoContrato = (idTipoContrato != null && idTipoContrato == 0) ? null : idTipoContrato;
        idTipoServicio = (idTipoServicio != null && idTipoServicio == 0) ? null : idTipoServicio;
        idFormaPago = (idFormaPago != null && idFormaPago == 0) ? null : idFormaPago;
        estado = (estado != null && estado == 0) ? null : estado;

        return contratoDao.searchContratos(
                idRelacion,
                idTipoContrato,
                idTipoServicio,
                idFormaPago,
                descripcion,
                fecIniRng1,
                fecIniRng2,
                fecFinRng1,
                fecFinRng2,
                estado,
                pageable);
    }

    private Sort createSort(Integer column, String dir) {
        if (column == null || dir == null) {
            return Sort.unsorted();
        }

        Sort.Direction direction = "asc".equalsIgnoreCase(dir) ? Sort.Direction.ASC : Sort.Direction.DESC;

        switch (column) {
            case 1:
                return Sort.by(direction, "idRelacion");
            case 2:
                return Sort.by(direction, "idTipoContrato");
            case 3:
                return Sort.by(direction, "idTipoServicio");
            case 4:
                return Sort.by(direction, "idFormaPago");
            case 5:
                return Sort.by(direction, "descripcion");
            case 6:
                return Sort.by(direction, "fecIni");
            case 7:
                return Sort.by(direction, "fecFin");
            case 8:
                return Sort.by(direction, "estado");
            default:
                return Sort.unsorted();
        }
    }
}

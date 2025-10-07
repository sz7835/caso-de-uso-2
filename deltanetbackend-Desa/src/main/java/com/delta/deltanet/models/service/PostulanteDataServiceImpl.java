package com.delta.deltanet.models.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.delta.deltanet.models.dao.PostulanteDataDao;
import com.delta.deltanet.models.entity.PostulanteData;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PostulanteDataServiceImpl implements PostulanteDataService {

    @Autowired
    private PostulanteDataDao postulanteDataDao;

    @Override
    public List<PostulanteData> searchPostulantes(String nombre, Integer formacionAcademica, Integer gradoAcademico,
            Integer estado) {
        return postulanteDataDao.searchPostulantes(nombre, formacionAcademica, gradoAcademico, estado);
    }

    @Override
    @Transactional
    public String cambiarEstado(Long id, String usuario, int nuevoEstado) {
        Optional<PostulanteData> optionalPostulante = postulanteDataDao.findById(id);

        if (optionalPostulante.isPresent()) {
            PostulanteData postulante = optionalPostulante.get();
            int estadoActual = postulante.getEstado();
            String mensaje;

            // Validar transición de estados
            if (estadoActual == 1 && (nuevoEstado == 2 || nuevoEstado == 3)) {
                postulante.setEstado(nuevoEstado);
                mensaje = (nuevoEstado == 2) ? "Desactivado exitosamente" : "Entrevista general aprobada exitosamente";
            } else if ((estadoActual == 2 || estadoActual == 3) && nuevoEstado == 1) {
                postulante.setEstado(1);
                mensaje = "Activado exitosamente";
            } else if (estadoActual == 3 && nuevoEstado == 2) {
                postulante.setEstado(2);
                mensaje = "Desactivado exitosamente";
            } else {
                throw new IllegalStateException("Transición de estado no permitida");
            }

            // Registrar cambios
            postulante.setUpdateUser(usuario);
            postulante.setUpdateDate(new Date());
            postulanteDataDao.save(postulante);

            return mensaje;
        } else {
            throw new NoSuchElementException("Postulante no encontrado con el ID: " + id);
        }
    }
}
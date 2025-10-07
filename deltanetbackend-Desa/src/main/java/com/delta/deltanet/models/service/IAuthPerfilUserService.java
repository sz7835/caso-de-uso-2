package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.AutorizacionPerfilUsuario;

import java.util.List;
import java.util.Optional;

public interface IAuthPerfilUserService {
    public Optional<AutorizacionPerfilUsuario> findById(Long idUser);
    public List<Object> findPerfiles(Long idUser);
}

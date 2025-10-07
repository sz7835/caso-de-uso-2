package com.delta.deltanet.models.service;

import com.delta.deltanet.models.entity.JefeArea;

import java.util.List;

public interface IJefeAreaService {
    public JefeArea buscaJefeArea(Long idJefeArea);
    public List<JefeArea> buscaJefes(Long idArea);
    public List<JefeArea> buscaJefes(Long idUser, Integer estado);
    public JefeArea buscaBoss(Long idUser, Long idArea);
}

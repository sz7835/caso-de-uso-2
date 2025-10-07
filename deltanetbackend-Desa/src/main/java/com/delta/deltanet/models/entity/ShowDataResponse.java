package com.delta.deltanet.models.entity;

import java.util.List;

public class ShowDataResponse {

    private OutRegistroHoras registroHoras;
    private List<RegistroProyecto> proyectos;
    private List<AdmCronograma> cronogramas;

    // Constructor
    public ShowDataResponse(OutRegistroHoras registroHoras, List<RegistroProyecto> proyectos, List<AdmCronograma> cronogramas) {
        this.registroHoras = registroHoras;
        this.proyectos = proyectos;
        this.cronogramas = cronogramas;
    }

    // Getters y setters
    public OutRegistroHoras getRegistroHoras() {
        return registroHoras;
    }

    public void setRegistroHoras(OutRegistroHoras registroHoras) {
        this.registroHoras = registroHoras;
    }

    public List<RegistroProyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<RegistroProyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public List<AdmCronograma> getCronogramas() {
        return cronogramas;
    }

    public void setCronogramas(List<AdmCronograma> cronogramas) {
        this.cronogramas = cronogramas;
    }
}

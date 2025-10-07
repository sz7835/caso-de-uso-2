package com.delta.deltanet.models.entity;

import java.util.List;

public class lstAuth1FuncDet {
    private int idFunc;
    private String nombre;
    private String descripcion;
    private String ruta;
    private String route;
    private String icon;
    private String flgMenu;
    private String flgControl;
    private List<lstAuth1FuncSrv> servicios;

    public int getIdFunc() {
        return idFunc;
    }

    public void setIdFunc(int idFunc) {
        this.idFunc = idFunc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getFlgMenu() {
        return flgMenu;
    }

    public void setFlgMenu(String flgMenu) {
        this.flgMenu = flgMenu;
    }

    public String getFlgControl() {
        return flgControl;
    }

    public void setFlgControl(String flgControl) {
        this.flgControl = flgControl;
    }

    public List<lstAuth1FuncSrv> getServicios() {
        return servicios;
    }

    public void setServicios(List<lstAuth1FuncSrv> servicios) {
        this.servicios = servicios;
    }
}

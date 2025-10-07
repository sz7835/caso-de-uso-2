package com.delta.deltanet.models.entity;

import java.util.List;
import java.util.Map;

public class ActivarRegistroHorasRequest {
    private List<Map<String, Integer>> registro;
    private String updateUser;

    // Getters and setters
    public List<Map<String, Integer>> getRegistro() {
        return registro;
    }

    public void setRegistro(List<Map<String, Integer>> registro) {
        this.registro = registro;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
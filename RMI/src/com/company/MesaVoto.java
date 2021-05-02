package com.company;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MesaVoto implements Serializable {

    private String ip;
    private String port;
    private boolean status;
    private Departamento departamento;
    protected CopyOnWriteArrayList<Pessoa> membros;

    public MesaVoto(Departamento departamento, CopyOnWriteArrayList<Pessoa> membros, String ip, String port) {
        this.ip = ip;
        this.port = port;
        this.departamento = departamento;
        this.membros = membros;
        this.status = false;
    }

    public void setMembros(CopyOnWriteArrayList<Pessoa> membros) {
        this.membros = membros;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public CopyOnWriteArrayList<Pessoa> getMembros() {
        return membros;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public boolean isStatus() {
        return status;
    }

    public void turnOn() {
        this.status = true;
    }

    public void turnOff() {
        this.status = false;
    }

}

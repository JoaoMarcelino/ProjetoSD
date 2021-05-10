package com.company;

import java.io.Serializable;
import java.util.*;

public class Pessoa implements Serializable {

    private String nome;
    private String password;
    private Departamento departamento;
    private String telefone;
    private String morada;
    private String numberCC;
    private GregorianCalendar expireCCDate;
    private Profissao profissao;

    private boolean isAdmin;

    public Pessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao) {

        this.nome = nome;
        this.password = password;
        this.departamento = departamento;
        this.telefone = telefone;
        this.morada = morada;
        this.numberCC = numberCC;
        this.expireCCDate = expireCCDate;
        this.profissao = profissao;
        this.isAdmin = false;
    }

    public Pessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao, boolean isAdmin) {

        this.nome = nome;
        this.password = password;
        this.departamento = departamento;
        this.telefone = telefone;
        this.morada = morada;
        this.numberCC = numberCC;
        this.expireCCDate = expireCCDate;
        this.profissao = profissao;
        this.isAdmin = isAdmin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getNumberCC() {
        return numberCC;
    }

    public void setNumberCC(String numberCC) {
        this.numberCC = numberCC;
    }

    public GregorianCalendar getExpireCCDate() {
        return expireCCDate;
    }

    public void setExpireCCDate(GregorianCalendar expireCCDate) {
        this.expireCCDate = expireCCDate;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public boolean isAdmin() { return isAdmin; }

    public void setAdmin(boolean admin) { isAdmin = admin; }
}

package com.company;

import com.github.scribejava.core.model.OAuth2AccessToken;

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

    private String facebookId;
    private OAuth2AccessToken accessToken;
    private boolean admin;

    public Pessoa(String nome, String password, Departamento departamento, String telefone, String morada, String numberCC, GregorianCalendar expireCCDate, Profissao profissao) {

        this.nome = nome;
        this.password = password;
        this.departamento = departamento;
        this.telefone = telefone;
        this.morada = morada;
        this.numberCC = numberCC;
        this.expireCCDate = expireCCDate;
        this.profissao = profissao;
        this.facebookId = "null";
        this.admin = false;
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
        this.facebookId = "null";
        this.admin = isAdmin;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public void setNumberCC(String numberCC) {
        this.numberCC = numberCC;
    }

    public void setExpireCCDate(GregorianCalendar expireCCDate) {
        this.expireCCDate = expireCCDate;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getMorada() {
        return morada;
    }

    public String getNumberCC() {
        return numberCC;
    }


    public GregorianCalendar getExpireCCDate() {
        return expireCCDate;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        admin = admin;
    }

    public String getFacebookId() { return facebookId; }

    public void setFacebookId(String facebookId) { this.facebookId = facebookId; }

    public OAuth2AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(OAuth2AccessToken accessToken) {
        this.accessToken = accessToken;
    }
}

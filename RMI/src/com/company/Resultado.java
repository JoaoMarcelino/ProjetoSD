package com.company;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Resultado implements Serializable {

    private String titulo;
    private int totalVotos;
    private int brancos;
    private int nulos;
    protected CopyOnWriteArrayList<String> nomesListas;
    protected CopyOnWriteArrayList<Integer> resultados;
    protected CopyOnWriteArrayList<String> vencedores;

    public Resultado(String titulo, int totalVotos, int brancos, int nulos, CopyOnWriteArrayList<String> nomesListas, CopyOnWriteArrayList<Integer> resultados, CopyOnWriteArrayList<String> vencedores) {
        this.titulo = titulo;
        this.totalVotos = totalVotos;
        this.brancos = brancos;
        this.nulos = nulos;
        this.nomesListas = nomesListas;
        this.resultados = resultados;
        this.vencedores = vencedores;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getTotalVotos() {
        return totalVotos;
    }

    public int getBrancos() {
        return brancos;
    }

    public int getNulos() {
        return nulos;
    }

    public CopyOnWriteArrayList<String> getNomesListas() {
        return nomesListas;
    }

    public CopyOnWriteArrayList<Integer> getResultados() {
        return resultados;
    }

    public CopyOnWriteArrayList<String> getVencedores() {
        return vencedores;
    }
}

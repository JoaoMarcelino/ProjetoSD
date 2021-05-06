package com.company;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Eleicao implements Serializable {
    private GregorianCalendar dataInicio;
    private GregorianCalendar dataFim;

    private String titulo;
    private String descricao;
    private int brancos;
    private int nulos;
    protected CopyOnWriteArrayList<Lista> listas;
    protected CopyOnWriteArrayList<Voto> votos;
    protected CopyOnWriteArrayList<MesaVoto> mesas;
    protected CopyOnWriteArrayList<Profissao> profissoesPermitidas;
    protected CopyOnWriteArrayList<Departamento> departamentosPermitidos;

    public Eleicao(GregorianCalendar dataInicio, GregorianCalendar dataFim, String titulo, String descricao, CopyOnWriteArrayList<Profissao> profissoesPermitidas, CopyOnWriteArrayList<Departamento> departamentosPermitidos) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.titulo = titulo;
        this.descricao = descricao;
        this.brancos = 0;
        this.nulos = 0;

        this.votos = new CopyOnWriteArrayList<Voto>();
        this.listas = new CopyOnWriteArrayList<Lista>();

        this.mesas = new CopyOnWriteArrayList<MesaVoto>();
        this.profissoesPermitidas = profissoesPermitidas;
        this.departamentosPermitidos = departamentosPermitidos;
    }

    public void setDataInicio(GregorianCalendar dataInicio) {
        if (!this.checkStart())
            this.dataInicio = dataInicio;
    }

    public void setDataFim(GregorianCalendar dataFim) {

        if (!this.checkStart())
            this.dataFim = dataFim;
    }

    public void setTitulo(String titulo) {
        if (!this.checkStart())
            this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        if (!this.checkStart())
            this.descricao = descricao;
    }

    public GregorianCalendar getDataInicio() {
        return dataInicio;
    }

    public GregorianCalendar getDataFim() {
        return dataFim;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public CopyOnWriteArrayList<Lista> getListas() {
        return listas;
    }

    public CopyOnWriteArrayList<Profissao> getProfissoesPermitidas() {
        return profissoesPermitidas;
    }

    public CopyOnWriteArrayList<Departamento> getDepartamentosPermitidos() {
        return departamentosPermitidos;
    }

    public CopyOnWriteArrayList<MesaVoto> getMesas() {
        return mesas;
    }

    public boolean checkStart() {
        GregorianCalendar dataAtual = new GregorianCalendar();
        return dataAtual.after(this.dataInicio);
    }


    public boolean checkEnd() {
        GregorianCalendar dataAtual = new GregorianCalendar();
        return dataAtual.after(this.dataFim);
    }

    public boolean hasVoted(Pessoa pessoa) {
        for (Voto voto : votos) {
            if (voto.getPessoa().getNumberCC().equals(pessoa.getNumberCC()))
                return true;
        }
        return false;
    }

    public boolean canVote(Pessoa pessoa) {
        return (isDepartamentoPermitida(pessoa.getDepartamento()) && isProfissaoPermitida(pessoa.getProfissao()));
    }

    public String editDados(String tituloNovo, String descricaoNova, GregorianCalendar dataInicio, GregorianCalendar dataFim) {
        if (this.checkStart()) {
            return "Eleicao a decorrer ou encerrada.";
        }
        if (dataInicio.before(new GregorianCalendar())) {
            return "Data de Inicio escolhida ja passou.";
        }

        setTitulo(tituloNovo);
        setDescricao(descricaoNova);
        setDataInicio(dataInicio);
        setDataFim(dataFim);
        return "Dados de Eleicao alterados.";
    }

    public boolean addVoto(Voto voto, String nomeLista, String tipo) {
        if (checkStart() && !checkEnd() && !hasVoted(voto.getPessoa()) && canVote(voto.getPessoa())) {

            switch (tipo) {
                case "Valido":
                    this.getListaByName(nomeLista).aumentaVotos();
                    this.votos.add(voto);
                    break;

                case "Branco":
                    this.brancos++;
                    this.votos.add(voto);
                    break;

                default: //Nulo
                    this.nulos++;
                    this.votos.add(voto);
                    break;
            }


            return true;
        }

        return false;
    }

    public String addVotoAntecipado(Voto voto, String nomeLista, String tipo) {
        Lista lis = this.getListaByName(nomeLista);
        if (!tipo.equals("Branco") && !tipo.equals("Nulo") && lis == null) {
            return "Lista (" + nomeLista + ") nao existe.";
        }

        if (this.checkStart()) {
            return "Eleicao em progresso. Vote numa mesa de voto.";
        }

        if (hasVoted(voto.getPessoa())) {
            return "Ja votou nesta eleicao.";
        }

        if (!canVote(voto.getPessoa())) {
            return "Nao pode votar nesta eleicao.";
        }
        if (!tipo.equals("Branco") && !tipo.equals("Nulo") && lis.getTipoLista() != voto.getPessoa().getProfissao()) {
            return "Nao pode votar nesta lista.";
        }

        if (voto.getMesa() != null && !mesas.contains(voto.getMesa())) {
            return "Mesa nao associada a eleicao.";
        }

        switch (tipo) {
            case "Valido":
                lis.aumentaVotos();
                this.votos.add(voto);
                break;

            case "Branco":
                this.brancos++;
                this.votos.add(voto);
                break;

            case "Nulo":
                this.nulos++;
                this.votos.add(voto);
                break;
        }
        return "Voto realizado com sucesso.";
    }

    public String addLista(Lista lista) {
        if (this.checkStart()) {
            return "Eleicao em progresso. Nao e possivel adicionar listas.";
        }
        String nome = lista.getNome();
        if (this.getListaByName(nome) != null) {
            return "Lista " + nome + " ja existe.";
        }
        if (!this.profissoesPermitidas.contains(lista.getTipoLista())) {
            return "Tipo de lista incompativel com eleicao";
        }
        this.listas.add(lista);
        return "Lista " + nome + " adicionada.";

    }


    public String addMesa(MesaVoto mesa) {
		/*if(this.checkStart()){
			return "Eleicao em progresso.";
		}*/
        if (mesas.contains(mesa)) {
            return "Mesa ja associada a eleicao.";
        }

        this.mesas.add(mesa);
        return "Messa associada a eleicao";
    }

    public void addProfissaoPermitida(Profissao profissao) {
        if (!this.checkStart() && !profissoesPermitidas.contains(profissao))
            this.profissoesPermitidas.add(profissao);
    }

    public void addDepartamentoPermitido(Departamento departamento) {
        if (!this.checkStart() && !departamentosPermitidos.contains(departamento))
            this.departamentosPermitidos.add(departamento);
    }

    public String removeLista(String nome) {
        if (this.checkStart()) {
            return "Eleicao em progresso ou encerrada.";
        }
        for (Lista lista : this.listas) {
            if (lista.getNome().equals(nome)) {
                this.listas.remove(lista);
                return "Lista removida com sucesso.";
            }
        }

        return "Lista nao existe.";
    }

    public String removeMesa(MesaVoto mesa) {
        if (this.checkStart()) {
            return "Eleicao em progresso.";
        }
        if (!mesas.contains(mesa)) {
            return "Mesa nao se econtra associcada a eleicao.";
        }

        for (int i = 0; i < mesas.size(); i++) {
            if (mesas.get(i).getDepartamento().equals(mesa.getDepartamento())) {
                mesas.remove(i);
            }
        }
        return "Mesa de Voto desassociada da Eleicao.";
    }

    public void removeProfissao(Profissao profissao) {
        if (!this.checkStart())
            this.profissoesPermitidas.remove(profissao);
    }

    public void removeDepartamento(Departamento departamento) {
        if (!this.checkStart())
            this.departamentosPermitidos.remove(departamento);
    }

    public CopyOnWriteArrayList<Lista> getListasByProfissao(Profissao profissao) {

        CopyOnWriteArrayList<Lista> aux = new CopyOnWriteArrayList<>();

        for (Lista lista : this.listas) {
            if (lista.getTipoLista() == profissao)
                aux.add(lista);
        }
        return aux;
    }

    public Lista getListaByName(String nome) {
        for (Lista lista : this.listas) {
            if (lista.getNome().equals(nome))
                return lista;
        }
        return null;
    }

    public CopyOnWriteArrayList<Voto> getVotos() {
        return votos;
    }

    public Voto getVotoByName(String nome) {
        for (Voto voto : this.votos) {
            if (voto.getPessoa().getNome().equals(nome))
                return voto;
        }
        return null;
    }

    public Voto getVotoByCC(String numeroCC) {
        for (Voto voto : this.votos) {
            if (voto.getPessoa().getNumberCC().equals(numeroCC))
                return voto;
        }
        return null;
    }

    public MesaVoto getMesaVotoByDepartamento(Departamento departamento) {
        for (MesaVoto mesa : this.mesas) {
            if (mesa.getDepartamento() == departamento)
                return mesa;
        }
        return null;
    }



    public boolean isProfissaoPermitida(Profissao profissao) {
        for (Profissao aux : this.profissoesPermitidas) {
            if (aux == profissao)
                return true;
        }
        return false;
    }

    public boolean isDepartamentoPermitida(Departamento departamento) {
        for (Departamento aux : this.departamentosPermitidos) {
            if (aux == departamento)
                return true;
        }
        return false;
    }

    public int getTotalVotos() {

        int aux = 0;

        for (Lista lista : this.listas) {
            aux += lista.getVotos();
        }

        return aux + this.brancos + this.nulos;
    }

    public String getTotalVotosString() {

        String aux = "";

        for (Lista lista : this.listas) {
            aux += lista.getNome() + ": " + lista.getVotos() + "\n";

        }
        aux += "Brancos: " + this.brancos + "\n";
        aux += "Nulos: " + this.nulos + "\n";

        return aux;
    }

    public String getVencedor() {
        String vencedor = "";
        int max_value = 0;
        int aux;
        for (Lista lista : this.listas) {
            aux = lista.getVotos();
            if (max_value == aux) {
                vencedor = "Empate";
            } else if (aux > max_value) {
                vencedor = lista.getNome();
            }
        }

        return vencedor;
    }

    public String getVencedorProfissao(Profissao prof) {
        String vencedor = prof.toString() + ": Empate";
        int max_value = 0;
        int aux;
        for (Lista lista : this.listas) {
            if (lista.getTipoLista() == prof) {
                aux = lista.getVotos();
                if (max_value == aux) {
                    vencedor = prof.toString() + ": Empate";
                } else if (aux > max_value) {
                    vencedor = prof.toString() + ": " + lista.getNome();
                }
            }
        }
        return vencedor;
    }

    public Resultado getResultados() {

        CopyOnWriteArrayList<String> nomesListas = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<Integer> resultadosListas = new CopyOnWriteArrayList<>();
        CopyOnWriteArrayList<String> nomeVencedores = new CopyOnWriteArrayList<>();
        for (Lista lista : this.listas) {
            nomesListas.add(lista.getNome());
            resultadosListas.add(lista.getVotos());
        }
        for (Profissao prof : profissoesPermitidas) {
            nomeVencedores.add(getVencedorProfissao(prof));
        }

        return new Resultado(this.titulo, this.getTotalVotos(), this.brancos, this.nulos, nomesListas, resultadosListas, nomeVencedores);
    }
}

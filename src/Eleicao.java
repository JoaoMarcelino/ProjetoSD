import java.util.*;

public class Eleicao {

	public Date startTime;
	public String titulo;
	public String descricao;
	public ArrayList<Lista> listas;
	public ArrayList<Pessoa> votos;
	public ArrayList<Profissao> profissoesPermitidas;
	public ArrayList<Departamento> departamentosPermitidos;

	public Eleicao(Date startTime, String titulo, String descricao, ArrayList<Lista> listas, ArrayList<Pessoa> votos, ArrayList<Profissao> profissoesPermitidas, ArrayList<Departamento> departamentosPermitidos) {
		this.startTime = startTime;
		this.titulo = titulo;
		this.descricao = descricao;
		this.listas = listas;
		this.votos = votos;
		this.profissoesPermitidas = profissoesPermitidas;
		this.departamentosPermitidos = departamentosPermitidos;
	}

	public Date getStartTime() {
		return startTime;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public ArrayList<Lista> getListas() {
		return listas;
	}

	public ArrayList<Pessoa> getVotos() {
		return votos;
	}

	public ArrayList<Profissao> getProfissoesPermitidas() {
		return profissoesPermitidas;
	}

	public ArrayList<Departamento> getDepartamentosPermitidos() {
		return departamentosPermitidos;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setListas(ArrayList<Lista> listas) {
		this.listas = listas;
	}

	public void setVotos(ArrayList<Pessoa> votos) {
		this.votos = votos;
	}

	public void setProfissoesPermitidas(ArrayList<Profissao> profissoesPermitidas) {
		this.profissoesPermitidas = profissoesPermitidas;
	}

	public void setDepartamentosPermitidos(ArrayList<Departamento> departamentosPermitidos) {
		this.departamentosPermitidos = departamentosPermitidos;
	}

	// Inserir métodos aqui
}

import java.io.Serializable;
import java.util.*;

public class Resultado implements Serializable {

	private String titulo;
	private int totalVotos;
	private int brancos;
	private int nulos;
	protected ArrayList<String> nomesListas;
	protected ArrayList<Integer> resultados;

	public Resultado(String titulo, int totalVotos, int brancos, int nulos, ArrayList<String> nomesListas, ArrayList<Integer> resultados) {
		this.titulo = titulo;
		this.totalVotos = totalVotos;
		this.brancos = brancos;
		this.nulos = nulos;
		this.nomesListas = nomesListas;
		this.resultados = resultados;
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

	public ArrayList<String> getNomesListas() {
		return nomesListas;
	}

	public ArrayList<Integer> getResultados() {
		return resultados;
	}
}

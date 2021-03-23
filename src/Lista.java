import java.util.*;

public class Lista {

	public ArrayList<Pessoa> listaPessoas;
	public String nome;
	public Profissao tipoLista;

	public Lista(ArrayList<Pessoa> listaPessoas, Profissao tipoLista, String nome) {
		this.listaPessoas = listaPessoas;
		this.tipoLista = tipoLista;
		this.nome = nome;
	}

	public ArrayList<Pessoa> getListaPessoas() {
		return listaPessoas;
	}

	public Profissao getTipoLista() {
		return tipoLista;
	}

	public String getNome() {
		return nome;
	}

	public void setListaPessoas(ArrayList<Pessoa> listaPessoas) {
		this.listaPessoas = listaPessoas;
	}

	public void setTipoLista(Profissao tipoLista) {
		this.tipoLista = tipoLista;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	// Inserir métodos aqui
}

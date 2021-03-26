import java.util.*;

public class Lista {

	protected ArrayList<Pessoa> listaPessoas;
	private String nome;
	private Profissao tipoLista;
	private int votos = 0;

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

	public int getVotos() {
		return votos;
	}

	public void setTipoLista(Profissao tipoLista) {
		this.tipoLista = tipoLista;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void addPessoa(Pessoa pessoa) {
		if (!this.listaPessoas.contains(pessoa)){
			this.listaPessoas.add(pessoa);
		}
	}

	public void aumentaVotos(){
		this.votos++;
	}
}
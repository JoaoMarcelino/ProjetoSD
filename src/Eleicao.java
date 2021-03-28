import java.io.Serializable;
import java.util.*;

public class Eleicao implements Serializable {
	private GregorianCalendar dataInicio;
	private GregorianCalendar dataFim;

	private String titulo;
	private String descricao;
	private int brancos;
	private int nulos;
	protected ArrayList<Lista> listas;
	protected ArrayList<Voto> votos;
	protected ArrayList<MesaVoto> mesas;
	protected ArrayList<Profissao> profissoesPermitidas;
	protected ArrayList<Departamento> departamentosPermitidos;

	public Eleicao(GregorianCalendar dataInicio, GregorianCalendar dataFim, String titulo, String descricao, ArrayList<Profissao> profissoesPermitidas,ArrayList<Departamento> departamentosPermitidos) {
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.titulo = titulo;
		this.descricao = descricao;
		this.brancos = 0;
		this.nulos = 0;
		this.votos = new ArrayList<Voto>();
		this.listas = new ArrayList<Lista>();
		this.mesas = new ArrayList<MesaVoto>();
		this.votos = new ArrayList<Voto>();
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

	public ArrayList<Lista> getListas() {
		return listas;
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
			if (voto.getPessoa().equals(pessoa))
				return true;
		}
		return false;
	}

	public boolean canVote(Pessoa pessoa) {
		return (isDepartamentoPermitida(pessoa.getDepartamento()) && isProfissaoPermitida(pessoa.getProfissao()));
	}

	public void addVoto(Voto voto, String nomeLista, String tipo) {

		if (checkStart() && !hasVoted(voto.getPessoa()) && canVote(voto.getPessoa())) {

			switch (tipo) {
			case "Valido":
				this.getListaByName(nomeLista).aumentaVotos();
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
		}
	}

	public void addVotoAntecipado(Voto voto, String nomeLista, String tipo) {

		if (!this.checkStart() && !hasVoted(voto.getPessoa()) && canVote(voto.getPessoa())) {

			switch (tipo) {
			case "Valido":
				this.getListaByName(nomeLista).aumentaVotos();
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
		}
	}

	public String addLista(Lista lista) {
		if(this.checkStart()){
			return "Eleicao em progresso. Nao e possivel adicionar listas.";
		}
		String nome=lista.getNome();
		if(this.getListaByName(nome)!=null){
			return "Lista "+nome+" ja existe.";
		}
		if(!this.profissoesPermitidas.contains(lista.getTipoLista())){
			return "Tipo de lista incompativel com eleicao";
		}
		this.listas.add(lista);
		return "Lista "+nome+" adicionada.";

	}

	public void addMesa(MesaVoto mesa) {
		if (!this.checkStart() && !mesas.contains(mesa))
			this.mesas.add(mesa);
	}

	public void addProfissaoPermitida(Profissao profissao) {
		if (!this.checkStart() && !profissoesPermitidas.contains(profissao))
			this.profissoesPermitidas.add(profissao);
	}

	public void addDepartamentoPermitido(Departamento departamento) {
		if (!this.checkStart() && !departamentosPermitidos.contains(departamento))
			this.departamentosPermitidos.add(departamento);
	}

	public void removeLista(String nome) {
		if (!this.checkStart()) {
			for (Lista lista : this.listas) {
				if (lista.getNome().equals(nome)) {
					this.listas.remove(lista);
					return;
				}
			}
		}
	}

	public void removeMesa(Departamento departamento) {
		if (!this.checkStart()) {
			for (MesaVoto mesa : this.mesas) {
				if (mesa.getDepartamento() == departamento) {
					this.mesas.remove(mesa);
					return;
				}
			}
		}
	}

	public void removeProfissao(Profissao profissao) {
		if (!this.checkStart())
			this.profissoesPermitidas.remove(profissao);
	}

	public void removeDepartamento(Departamento departamento) {
		if (!this.checkStart())
			this.departamentosPermitidos.remove(departamento);
	}

	public ArrayList<Lista> getListasByProfissao(Profissao profissao) {

		ArrayList<Lista> aux = new ArrayList<>();

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

	public ArrayList<Voto> getVotos() {
		return votos;
	}

	public Voto getVotoByName(String nome) {
		for (Voto voto : this.votos) {
			if (voto.getPessoa().getNome().equals(nome))
				return voto;
		}
		return null;
	}

	public ArrayList<MesaVoto> getMesas() {
		return mesas;
	}

	public MesaVoto getMesaVotoByDepartamento(Departamento departamento) {
		for (MesaVoto mesa : this.mesas) {
			if (mesa.getDepartamento() == departamento)
				return mesa;
		}
		return null;
	}

	public ArrayList<Profissao> getProfissoesPermitidas() {
		return profissoesPermitidas;
	}

	public ArrayList<Departamento> getDepartamentosPermitidos() {
		return departamentosPermitidos;
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

	public Resultado getResultados() {

		ArrayList<String> nomesListas = new ArrayList<>();
		ArrayList<Integer> resultadosListas = new ArrayList<>();

		for (Lista lista : this.listas) {
			nomesListas.add(lista.getNome());
			resultadosListas.add(lista.getVotos());
		}

		return new Resultado(this.titulo, this.getTotalVotos(), this.brancos, this.nulos, nomesListas,
				resultadosListas);
	}
}

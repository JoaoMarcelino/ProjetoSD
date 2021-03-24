import java.util.*;

public class Voto {

	private Pessoa pessoa;
	private Date data;
	private MesaVoto mesa;

	public Voto(Pessoa pessoa, Date data, MesaVoto mesa) {
		this.pessoa = pessoa;
		this.data = data;
		this.mesa = mesa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public Date getData() {
		return data;
	}

	public MesaVoto getMesa() {
		return mesa;
	}
}

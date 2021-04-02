import java.io.Serializable;
import java.util.*;

public class Voto implements Serializable {

	private Pessoa pessoa;
	private GregorianCalendar data;
	private MesaVoto mesa;

	public Voto(Pessoa pessoa, GregorianCalendar data, MesaVoto mesa) {
		this.pessoa = pessoa;
		this.data = data;
		this.mesa = mesa;
	}

	public Voto(Pessoa pessoa, GregorianCalendar data) {
		this.pessoa = pessoa;
		this.data = data;
		this.mesa = null;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public GregorianCalendar getData() {
		return data;
	}

	public MesaVoto getMesa() {
		return mesa;
	}
}
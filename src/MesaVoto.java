import java.util.*;

public class MesaVoto {

	private String ip;
	private String port;
	private Departamento departamento;
	protected ArrayList<Pessoa> membros;

	public MesaVoto(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port) {
		this.ip = ip;
		this.port = port;
		this.departamento = departamento;
		this.membros = membros;
	}

	public void setMembros(ArrayList<Pessoa> membros) {
		this.membros = membros;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public ArrayList<Pessoa> getMembros() {
		return membros;
	}

	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}
}

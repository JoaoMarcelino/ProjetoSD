import java.util.*;

public class MesaVoto {

	public Departamento departamento;
	public ArrayList<Pessoa> membros;
	public String ip;
	public String port;

	public MesaVoto(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port) {
		this.departamento = departamento;
		this.membros = membros;
		this.ip = ip;
		this.port = port;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public void setMembros(ArrayList<Pessoa> membros) {
		this.membros = membros;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(String port) {
		this.port = port;
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

	// Inserir métodos aqui
}

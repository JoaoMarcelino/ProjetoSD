import java.io.Serializable;
import java.util.*;

public class MesaVoto implements Serializable {

	private String ip;
	private String port;
	private boolean status;
	private Departamento departamento;
	protected ArrayList<Pessoa> membros;

	public MesaVoto(Departamento departamento, ArrayList<Pessoa> membros, String ip, String port) {
		this.ip = ip;
		this.port = port;
		this.departamento = departamento;
		this.membros = membros;
		this.status = false;
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

	public boolean isStatus() {
		return status;
	}

	public void turnOn(){
		this.status = true;
	}

	public void turnOff(){
		this.status = false;
	}

}

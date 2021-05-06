/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import com.company.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;


public class HeyBean {
	public RMI_S_Interface servidor;
	private String RMIHostIP="192.168.1.69";
	private int RMIHostPort=4000;

	private String username;
	private String password;
	private String message;
	private List<String> profs;
	private String yourProf;
	private List<String> deps;
	private String yourDep;


	public HeyBean() {
		try {
			Registry r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
			servidor = (RMI_S_Interface) r.lookup("ServidorRMI");
		}
		catch(NotBoundException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}

	public ArrayList<Pessoa> getAllUsers() throws RemoteException {
		return new ArrayList<>(servidor.listPessoas()); // are you going to throw all exceptions?
	}

	public ArrayList<Eleicao> getAllElections() throws RemoteException {
		return new ArrayList<>(servidor.listEleicoes());
	}

	public void setMessage(String message){
		this.message=message;
	}

	public String getMessage(){
		return this.message;
	}

	public String getYourProf() {
		return yourProf;
	}

	public void setYourProf(String yourProf) {
		this.yourProf = yourProf;
	}

	public List<String> getProfs() {
		List<String> aux=new ArrayList<>();
		for(Profissao prof: Profissao.values()){
			aux.add(prof.name());
		}
		return aux;
	}

	public void setProfs(List<String> profs) {
		this.profs = profs;
	}

	public List<String> getDeps() {
		List<String> aux=new ArrayList<>();
		for(Departamento dep: Departamento.values()){
			aux.add(dep.name());
		}
		return aux;
	}

	public void setDeps(List<String> deps) {
		this.deps = deps;
	}

	public String getYourDep() {
		return yourDep;
	}

	public void setYourDep(String yourDep) {
		this.yourDep = yourDep;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}

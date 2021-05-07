/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import com.company.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.ResultSet;
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

	public ArrayList<MesaVoto> getAllMesas() throws RemoteException {
		return new ArrayList<>(servidor.listMesas());
	}

	public Resultado getResultados(String nomEleicao) throws RemoteException {
		return servidor.getResultados(nomEleicao);
	}

	public Voto getVoto(String nomEleicao,String numeroCC) throws RemoteException {
		return servidor.getVoto(numeroCC,nomEleicao);
	}

	public void setMessage(String message){
		this.message=message;
	}

	public String getMessage(){
		return this.message;
	}


	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}

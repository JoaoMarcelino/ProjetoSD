/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import com.company.Pessoa;
import com.company.RMI_S_Interface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;


public class HeyBean {
	public RMI_S_Interface servidor;
	private String username; // username and password supplied by the user
	private String password;
	private String RMIHostIP="192.168.1.69";
	private int RMIHostPort=4000;


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

	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}

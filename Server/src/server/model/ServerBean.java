/**
 * Raul Barbosa 2014-11-07
 */
package server.model;

import RMI.RMI_S_Interface;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Properties;
import java.rmi.registry.Registry;

public class ServerBean {
	private int number;
	private String RMIHostIP;
	private int RMIHostPort;
	private RMI_S_Interface servidor;


	public ServerBean(){
		try{
			Registry r = LocateRegistry.getRegistry(RMIHostIP, RMIHostPort);
			servidor = (RMI_S_Interface) r.lookup("ServidorRMI");

		}catch (RemoteException | NotBoundException e){
			System.out.println("Servidor RMI indisponivel.");
		}
	}
	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public ArrayList<String> getPrimes() {
		ArrayList<String> primes = new ArrayList<String>();
		int candidate = 2;
		for(int count = 0; count < number; candidate++)
			if(isPrime(candidate)) {
				primes.add((new Integer(candidate)).toString());
				count++;
			}
		return primes;
	}
	
	private boolean isPrime(int number) {
		if((number & 1) == 0)
			return number == 2;
		for(int i = 3; number >= i*i; i += 2)
			if((number % i) == 0)
				return false;
		return true;
	}
}

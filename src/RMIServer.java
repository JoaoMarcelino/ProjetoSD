import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;

public class RMIServer extends UnicastRemoteObject implements RMI_S_Interface {

	ArrayList<Eleicao> eleicoes = new ArrayList<>();
	ArrayList<Pessoa>   pessoas = new ArrayList<>();
	ArrayList<MesaVoto>   mesas = new ArrayList<>();

	public RMIServer() throws RemoteException {
		super();
	}

	// Inserir métodos aqui

	public String sayHello() throws RemoteException {
		return "¡Hola mundo, soy el servidor y he establecido contacto con el cliente!";
	}

	public static void main(String args[]) {

		try {
			RMIServer servidor = new RMIServer();
			Registry r = LocateRegistry.createRegistry(7000);
			r.rebind("ServidorRMI", servidor);

			System.out.println("¡El servidor RMI está listo!");
		}
		catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}
}

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class RMIServer extends UnicastRemoteObject implements RMI_S_Interface {

	public RMIServer() throws RemoteException {
		super();
	}

	// Inserir métodos aqui

	public String sayHello() throws RemoteException {
		return "¡Hola mundo, Soy el servidor y he establecido contacto con el cliente!";
	}

	public static void main(String args[]) {

		try {
			RMIServer servidor = new RMIServer();
			Registry r = LocateRegistry.createRegistry(25565);
			r.rebind("ServidorRMI", servidor);

			System.out.println("¡El servidor RMI está listo!");
		}
		catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}
}

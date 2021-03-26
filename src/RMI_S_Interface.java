import java.rmi.*;

public interface RMI_S_Interface extends Remote {
	public String sayHello() throws java.rmi.RemoteException;
	// Inserir headers dos métodos RMI aqui
}

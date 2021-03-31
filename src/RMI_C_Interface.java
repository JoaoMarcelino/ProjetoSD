import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI_C_Interface extends Remote {

	public void printOnClient(String s) throws java.rmi.RemoteException;
}

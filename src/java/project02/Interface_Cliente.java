package project02;

import java.rmi.*;


public interface Interface_Cliente  extends Remote {
	
	public void printMensagem(String data) throws RemoteException;
}


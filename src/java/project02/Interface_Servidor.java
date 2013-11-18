package project02;

import java.io.DataOutputStream;
import java.rmi.*;


public interface Interface_Servidor extends Remote {

	public String login (String data , Interface_Cliente IC) throws RemoteException;
	
	public String loginHTML (String data) throws RemoteException;
	
	public void logout (String data) throws RemoteException;

	public String register (String data) throws RemoteException;
	
	public String tarefas(String data) throws RemoteException;

	public String showtasks(String data)throws RemoteException;
            
            public String showTasksHTML() throws RemoteException;
            
            public String getLastNotification() throws RemoteException;
            
	public String addtask(String dados)throws RemoteException;

	public String edit(String data) throws RemoteException;

	public String deletetask(String data)throws RemoteException;
            	
	public void reconnecta(String data,Interface_Cliente IC) throws RemoteException;

	public String online()throws RemoteException;


}

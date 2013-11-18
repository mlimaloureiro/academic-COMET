package project02;


import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;




public class ServerTomCat extends UnicastRemoteObject{

	public static Interface_Servidor stub;

	static{
		conecta();
	}

	public ServerTomCat() throws RemoteException{

	}

	public static void conecta(){
            
		try {
			stub = (Interface_Servidor) LocateRegistry.getRegistry(7000).lookup("ServidorRmi");
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String registo (String user, String pass, String email) throws RemoteException {
		
		String mensagem="register|"+email+"|"+pass+"|"+user;
		String respostaServidor=stub.register(mensagem);
		String dados[] = respostaServidor.split("\\|");
		
		if (dados[0].equals("register")) {
			String resposta=dados[1];
			if (resposta.equals("accept")) {
				System.out.println("Registo efectuado com sucesso.");
				return "Registo efectuado com sucesso.";
			}
			else if (resposta.equals("denied")) {
				
				return "Registo nao efectuado. Registe-se novamente.";
			} 
		}
		return "ERRO";
	}

	public String login (String user, String pass) throws RemoteException {

		ServerTomCat c = new ServerTomCat(); 

		String mensagem="login|"+user+"|"+pass+"|";
		String respostaServidor=stub.loginHTML(mensagem);
                        System.out.println(respostaServidor);
		String dados[] = respostaServidor.split("\\|");

		if (dados[0].equals("login")) {
			String resposta=dados[1];
			if (resposta.equals("accept")) {
                                                //RESTConnection();
				return dados[2];
			}
			else if(resposta.equals("denied")){

				return "Password errada!! Autentifique-se novamente.";
			}
			else if(resposta.equals("wrongUser")){

				return "Utilizador nao existente";
			}
			else if(resposta.equals("jaLogado")){

				return "Utilizador ja esta online";
			}
		}

		return "ERRO";
	}
        
        public static String showTasksHTML() throws RemoteException {
            String msg = stub.showTasksHTML();
            return msg;
        }
        
        public static String showtasks() throws RemoteException {
            String msg = stub.showtasks("teste");
            return msg;
        }
        
        public static String getLastNotification() throws RemoteException {
            String msg = stub.getLastNotification();
            return msg;
        }

        public void logout (String user) throws RemoteException {
                    String msg= "logout|"+user;
                    stub.logout(msg);
        }
        
        public static void addtask(String msg) throws RemoteException {
            stub.addtask(msg);
        }
        
        public static void deletetask(String task) throws RemoteException {
            stub.deletetask(task);
        }

        public static void edittask(String task) throws RemoteException {
            stub.edit(task);
        }
        
        public static void RESTConnection() throws RemoteException {
           GoogleRestReader googlerestreader = new GoogleRestReader();
           googlerestreader.init();
           System.out.println("ENTROU NO REST CONNECTION");
        }
        
        
        public String online()throws RemoteException{
                    String online = stub.online();
                    return online;
        }
}

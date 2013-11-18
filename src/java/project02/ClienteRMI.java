package project02;

import java.io.IOException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ClienteRMI implements Interface_Cliente {

	public static boolean loginOK = false;
	public static boolean registoOK = false;
	public static boolean saidaOK = false;
	public static boolean servidorOK = false;
	public static String identificacao;
	public static Interface_Servidor stub;
	public static Interface_Cliente IC =null;
	public static ArrayList <String> temp_msg = new ArrayList <String>();
	public static int MAXSIZE=10;

	ClienteRMI() throws RemoteException {
		super();
	}

	public static void main(String args[]) {


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



		menu_inicial();

	}

	public static void menu_inicial() {
		int opcao = 0;
		while (true) {
			ClienteRMI.saidaOK = false;
			System.out.println("\n+++++Escolha a opcao que deseja+++++");
			System.out.println("1- Login.");
			System.out.println("2- Register.");
			System.out.print(": ");
			opcao = User.readInt();
			switch (opcao) {
			case 1:
				signIn();
				break;
			case 2:
				register();
				break;
			}
		}
	}

	public static void signIn() {
		String user;
		String password;
		System.out.print("User: ");
		user = User.readString();

		while (user.length() == 0) {
			System.out.println("Insira o User de novo:");
			System.out.print("User: ");
			user = User.readString();
		}

		System.out.print("Password: ");
		password = User.readString();

		while (password.length() == 0) {
			System.out.println("Insira a Password de novo:");
			System.out.print("Password: ");
			password = User.readString();
		}
		String mensagem = "login|" + user + "|" + password;
		try {
			ClienteRMI c = new ClienteRMI();
			IC = (Interface_Cliente) UnicastRemoteObject.exportObject(c, 0);


			String respostaServidor = stub.login(mensagem, IC);
			String dados[] = respostaServidor.split("\\|");
			if (dados[0].equals("login")) {
				String resposta = dados[1];
				if (resposta.equals("accept")) {
					System.out.println("Bem-vindo!!!\n");
					
					if (dados[2].equals("tarefa"))
					{
						String tarefas = stub.tarefas(user);
						String tar[] = tarefas.split("\\|");
						
						for (int i = 1; i < tar.length; i++) {
							System.out.println(tar[i]);
						}
					
					}
					
					
					
					ClienteRMI.loginOK = true;
				} else if (resposta.equals("denied")) {
					System.out.println("Password errada!! Autentifique-se novamente.");
					ClienteRMI.loginOK = false;
				} else if (resposta.equals("wrongUser")) {
					System.out.println("Utilizador nao existente");
					ClienteRMI.loginOK = false;
				} else if (resposta.equals("jaLogado")) {
					System.out.println("Utilizador ja esta online");
					ClienteRMI.loginOK = false;
				}
			}
		} catch (IOException e1) {
			System.out.println("Warning: servidor em baixo");
			ClienteRMI.servidorOK = false;
			menu_inicial();
		} 

		if (ClienteRMI.loginOK == true) {
			ClienteRMI.servidorOK = true;
			ClienteRMI.identificacao = user;
			menuAposLogin();
		} else {
			menu_inicial();
		}
	}

	public static void reconnect(){

		try {

			try {
				stub = (Interface_Servidor) LocateRegistry.getRegistry(7000).lookup("ServidorRmi");
			} catch (Exception e) {
				System.out.println("\n\nerro\n\n");

			}
			System.out.println("reconnecting...");
			stub.reconnecta(ClienteRMI.identificacao,IC);
			ClienteRMI.servidorOK = true;

			if(temp_msg.size()>0&&servidorOK==true){
				for(int i=0;i<temp_msg.size();i++){

					stub.addtask(temp_msg.get(i));

				}
				temp_msg.clear();
			}

		} catch (Exception e1) {
			System.out.println("Warning: servidor em baixo");
			ClienteRMI.servidorOK = false;
			menuAposLogin();
		}


	}


	public static void register() {
		String password;
		String email;
		String user;
		System.out.print("User: ");
		user = User.readString();
		while (user.length() == 0) {
			System.out.println("Insira o User de novo:");
			System.out.print("User: ");
			user = User.readString();
		}

		System.out.print("Email: ");
		email = User.readString();
		while (email.length() == 0) {
			System.out.println("Insira o Email de novo:");
			System.out.print("Email: ");
			email = User.readString();
		}

		System.out.print("Password: ");
		password = User.readString();
		while (password.length() == 0) {
			System.out.println("Insira a Password de novo:");
			System.out.print("Password: ");
			password = User.readString();
		}

		String mensagem = "register|" + email + "|" + password + "|" + user;
		try {

			String respostaServidor = stub.register(mensagem);
			String dados[] = respostaServidor.split("\\|");
			if (dados[0].equals("register")) {
				String resposta = dados[1];
				if (resposta.equals("accept")) {
					System.out.println("Registo efectuado com sucesso.");
					ClienteRMI.registoOK = true;
				} else if (resposta.equals("denied")) {
					System.out.println("Registo nao efectuado. Registe-se novamente.");
					ClienteRMI.registoOK = false;
				}
			}
		} catch (IOException e1) {
			System.out.println("Warning: servidor em baixo");
			ClienteRMI.servidorOK = false;
			reconnect();

			menu_inicial();
		} 

		if (ClienteRMI.registoOK) {
			ClienteRMI.servidorOK = true;
			menu_inicial();
		} else {
			register();
		}
	}

	public static void menuAposLogin() {
		int opcao;
		while (true) {

			System.out.println("\n+++++ Escolha a opcao que deseja +++++");
			System.out.println("1- Show tasks:");
			System.out.println("2- Add Task:");
			System.out.println("3- Edit Task:");
			System.out.println("4- Delete Task:");
			System.out.println("5- Online Users:");
			System.out.println("0- Logout:");
			System.out.print(": ");

			opcao = User.readInt();
			String msg = "";

			switch (opcao) {

			//Logout
			case 0:

				ClienteRMI.saidaOK = true;
				msg = "logout|" + ClienteRMI.identificacao;
				try {

					stub.logout(msg);
				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					ClienteRMI.servidorOK = false;
					menuAposLogin();
				} 
				menu_inicial();
				break;


			case 1:


				try {

					String respostaServidor = stub.showtasks(ClienteRMI.identificacao);
					String dados[] = respostaServidor.split("\\|");


					System.out.println("Listando tarefas:\n");

					for (int i = 1; i < dados.length; i++) {

						System.out.println(dados[i]);

					}

					

				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					ClienteRMI.servidorOK = false;
					reconnect();
					menuAposLogin();
				} 

				break;    	


			case 2:

				System.out.println("Introduza o titulo da tarefa:");

				String titulo = User.readString();

				msg = titulo+"|"+ClienteRMI.identificacao;

				try {


					String respostaServidor = stub.addtask(msg);

					System.out.println("Foi introduzida uma nova tarefa");



				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					ClienteRMI.servidorOK = false;

					if(temp_msg.size()<MAXSIZE){
						temp_msg.add(msg);

					}else{
						System.out.println("Nao sao aceites mais tarefas");
					}


					reconnect();
					menuAposLogin();
				} 

				break;


			case 3:

				String tarefa="";
				String ttl="";


				System.out.println("Qual o id da tarefa que pretende alterar?");
				tarefa = User.readString();

				System.out.println("Insira o novo titulo da tarefa");
				ttl = User.readString();


				msg = tarefa+"|"+ttl+"|"+ClienteRMI.identificacao;

				try {


					String respostaServidor = stub.edit(msg);

					System.out.println(respostaServidor);



				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					ClienteRMI.servidorOK = false;
					reconnect();
					menuAposLogin();
				} 

				break;

			case 4:

				String deleteID="";
				String user = ClienteRMI.identificacao;

				System.out.println("Introduza o ID da tarefa que pretende eliminar:");
				deleteID = User.readString();

				msg = deleteID+"|"+user;

				try {


					String respostaServidor = stub.deletetask(msg);

					System.out.println(respostaServidor);



				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					reconnect();
					ClienteRMI.servidorOK = false;
					menuAposLogin();
				} 

				break;



				//Ver users online
			case 5:

				try {

					String respostaServidor = stub.online();
					String dados[] = respostaServidor.split("\\|");

					if (dados[1].equals("vazio")) {

						System.out.println("Nao existem utilizadores Online");

					} else {

						System.out.println("Existem " + (dados.length - 1) + " utilizadores online:\n");


						for (int i = 1; i < dados.length; i++) {
							System.out.println(dados[i]);
						}

					}


				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					reconnect();
					ClienteRMI.servidorOK = false;
					menuAposLogin();
				}

				break;

			}
		}
	}


	public void printMensagem(String data) throws RemoteException {

		String dados[] = data.split("\\|");
		String notify = "notifica";
		String notifydelete = "notificaapaga";

		if (dados[0].compareTo(notify)==0) {

			System.out.println(dados[1]+" editou uma tarefa");

		}else if (dados[0].compareTo(notifydelete)==0){

			System.out.println(dados[1]+" apagou a tarefa com o seguinte ID: "+dados[2]);

		}else 
			System.out.println("foi introduzida uma nova tarefa: "+dados[1]+" "+dados[2]+" "+dados[3]);


	}

}

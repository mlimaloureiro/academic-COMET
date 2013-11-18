package project02;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

// Thread que trata de cada canal de comunica��o com um cliente
class Connection extends Thread {

	Socket clientSocket;
	DataInputStream in;
	DataOutputStream out;
	public static String identificacao;


	public Connection(Socket aClientSocket) {
		clientSocket = aClientSocket;
		this.start();
	}

	// =============================
	public void run() {
		try {
			out = new DataOutputStream(clientSocket.getOutputStream());
			in = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e1) {
			System.out.println("O user saiu");
		}

		try {

			while (true) {
				String data = in.readUTF();
				String dados[] = data.split("\\|");
				if (dados[0].equals("login")) {
					login(dados, out);
				} else if (dados[0].equals("register")){
					register(dados, out);
				} else if (dados[0].equals("showtasks")){
					showtasks(dados, out);
				} else if (dados[0].equals("addtask")){
					addtask(dados, out);	
				} else if (dados[0].equals("edit")){
					edit(dados, out);
				} else if (dados[0].equals("delete")){
					deletetask(dados, out);
				} else if (dados[0].equals("online")) {
					online(dados, out);
				}else if (dados[0].equals("reconnect")) {
					reconnect(dados, out);
				} else if (dados[0].equals("logout")) {
					logout(dados);
				}
			}

		} catch (EOFException e) {
			System.out.println("EOF:" + e);
			System.out.println("Cliente saiu");

			Enumeration<String> chaves = Servidor.autenticados.keys();
			while (chaves.hasMoreElements()) {
				String user = (String) chaves.nextElement();
				if (Servidor.autenticados.get(user) == out) {
					String del[] = new String[2];
					del[1] = user;
					logout(del);
				}
			}
		} catch (IOException e) {
			System.out.println("IO:" + e);
			System.out.println("Cliente saiu");

			Enumeration<String> chaves = Servidor.autenticados.keys();
			while (chaves.hasMoreElements()) {
				String user = (String) chaves.nextElement();
				if (Servidor.autenticados.get(user) == out) {
					String del[] = new String[2];
					del[1] = user;
					logout(del);
				}
			}
		}
	}



	static void checkPrimaryServer() {
		DatagramSocket aSocket = null;
		String s;
		int serverPort = 6789;
		try{
			aSocket = new DatagramSocket(serverPort);
			System.out.println("Socket Datagram a escuta no porto 6789");
			while(true){
				byte[] buffer = new byte[1000];                    
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				aSocket.setSoTimeout(30000);
				aSocket.receive(request);
				s=new String(request.getData(), 0, request.getLength());   
				if(s.compareTo("I_AM_ALIVE")==0){
					//System.out.println("Server is alive!");
				}
			}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){
			System.out.println("IO: " + e.getMessage());
			System.out.println("Server is dead, resuming his work!");
		}finally {if(aSocket != null) aSocket.close();}
	}

	public synchronized void login(String[] dados, DataOutputStream out) {
		try {
			String resposta = "";
			String user = dados[1];
			String password = dados[2];
			Connection.identificacao=user;
			String tarefas = "veri|";

			//Se o utilizador noa estiver logado
			if (Servidor.autenticados.containsKey(user) == false && Servidor.autenticadosRMI.containsKey(user) == false) {
				// Metodo que trata da autenticacao de um utilizador(login)
				if (Servidor.registos.containsKey(user) == true) {
					if (Servidor.registos.get(user).getPass().equals(password) == true) {
						resposta = "login|accept";
						out.writeUTF(resposta);
						out.flush();

						Servidor.autenticados.put(user, out);

						for (int j = 0; j < Servidor.registos.get(user).tarefasporverificar.size(); j++) {

							tarefas = tarefas +(Servidor.registos.get(user).tarefasporverificar.get(j))+"|";
						}




						Servidor.registos.get(user).tarefasporverificar.clear();

						out.writeUTF(tarefas);
						out.flush();



						if (Servidor.autenticados.size() + Servidor.autenticadosRMI.size() > 0) {
							System.out.println("\n\nEstao " + (Servidor.autenticados.size() + Servidor.autenticadosRMI.size()) + " users online:");
							//IMPRIME OS UTLiZADORES AUTENTICADOS
							Enumeration<String> e1 = Servidor.autenticadosRMI.keys();
							while (e1.hasMoreElements()) {
								System.out.println(e1.nextElement());
							}
							Enumeration<String> e2 = Servidor.autenticados.keys();
							while (e2.hasMoreElements()) {
								System.out.println(e2.nextElement());
							}
							System.out.println("");


						} else {
							System.out.println("\n\nNao existem users online.\n");
						}
					} else {
						resposta = "login|denied";
						out.writeUTF(resposta);
						out.flush();
					}
				} else {
					resposta = "login|wrongUser";
					out.writeUTF(resposta);
					out.flush();
				}
			} //Se o utilizador ja estiver logado
			else {
				resposta = "login|jaLogado";
				out.writeUTF(resposta);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void logout(String[] dados) {
		String user = dados[1];
		if (Servidor.autenticados.containsKey(user) == true) {
			Servidor.autenticados.remove(user);
		}
		if (Servidor.autenticados.size() + Servidor.autenticadosRMI.size() > 0) {
			System.out.println("\n\nEstao " + (Servidor.autenticados.size() + Servidor.autenticadosRMI.size()) + " users online:");
			//IMPRIME OS UTLiZADORES AUTENTICADOS
			Enumeration<String> e1 = Servidor.autenticadosRMI.keys();
			while (e1.hasMoreElements()) {
				System.out.println(e1.nextElement());
			}
			Enumeration<String> e2 = Servidor.autenticados.keys();
			while (e2.hasMoreElements()) {
				System.out.println(e2.nextElement());
			}
			System.out.println("");
		} else {
			System.out.println("\n\nNao existem users online.\n");
		}
	}

	public synchronized void register(String[] dados, DataOutputStream out) {
		try {
			// Metodo que regista um novo utilizador

			String email = dados[1];
			String password = dados[2];
			String user = dados[3];
			String resposta = "";

			if (Servidor.registos.containsKey(user)) {
				resposta = "register|denied";
				out.writeUTF(resposta);
				out.flush();
			} else {
				Utilizador newUser = new Utilizador(password, email, user);
				Servidor.registos.put(user, newUser);
				FicheiroDeObjectos f = new FicheiroDeObjectos();
				try {
					f.abreEscrita("registos.dat");
					f.escreveObjecto(Servidor.registos);
					f.fechaEscrita();
					resposta = "register|accept";
					out.writeUTF(resposta);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public synchronized void showtasks(String[] dados, DataOutputStream out) {

		String tsk="tarefas";
		String aux="";

		if (!Servidor.tarefas.isEmpty()){

			for (Enumeration en = Servidor.tarefas.keys(); en.hasMoreElements();) {

				String task = (String) en.nextElement();

				aux = aux+"|"+Servidor.tarefas.get(task).toString();

			}

			tsk=tsk+aux;

		}else tsk="vazio";

		try {
			out.writeUTF(tsk);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public synchronized void addtask(String[] dados, DataOutputStream out) {

		Servidor.ID++;
		int cont = Servidor.ID;


		String titulo = dados[1];
		String autor = dados[2];
		String ident = Integer.toString(cont);
		String estado = "enviado";
		String mensagem = "newTask"+"|"+titulo+"|"+ident+"|"+autor;

		Task newTask = new Task(titulo, ident, autor);
		String tarefa = autor+" adicionou uma tarefa com o id: "+ident;

		Servidor.tarefas.put(ident,newTask);

		FicheiroDeObjectos f = new FicheiroDeObjectos();
		try {
			f.abreEscrita("tarefas.dat");
			f.escreveObjecto(Servidor.tarefas);
			f.fechaEscrita();

		}catch (Exception e) {
			e.printStackTrace();
		}

		if (Servidor.autenticadosRMI.size() + Servidor.autenticados.size() > 1) {

			//Envia as tarefas directamente para os utilizadores ligados por RMI online
			if (Servidor.autenticadosRMI.size() > 0) {

				for (Enumeration <String>destinter = Servidor.autenticadosRMI.keys(); destinter.hasMoreElements();) {

					String key_inter = (String) destinter.nextElement();

					if (!key_inter.equals(dados[2])) {
						try {
							Servidor.autenticadosRMI.get(key_inter).printMensagem(mensagem);


						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			//Envia as tarefas directamente para os utilizadores ligados por sockets online
			if (Servidor.autenticados.size() > 0) {

				for (Enumeration destout = Servidor.autenticados.keys(); destout.hasMoreElements();) {

					String key_out = (String) destout.nextElement();
					if (!key_out.equals(dados[2])) {
						try {

							Servidor.autenticados.get(key_out).writeUTF(mensagem);
							Servidor.autenticados.get(key_out).flush();


						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}


			try {
				out.writeUTF(estado);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}else {

			estado = "offline";

			try {
				out.writeUTF(estado);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (Enumeration reg = Servidor.registos.keys(); reg.hasMoreElements();){

			String chave = (String) reg.nextElement();

			if (!Servidor.autenticados.containsKey(chave))

				Servidor.offline.add(chave);
		}
		for (int i = 0; i <Servidor.offline.size(); i++) {

			Servidor.registos.get(Servidor.offline.get(i)).tarefasporverificar.add(tarefa);
			
		}	

		Servidor.offline.clear();


	}

	public synchronized void edit(String[] dados, DataOutputStream out) {

		String ident = dados[1];
		String titulo = dados[2];
		String autor = dados[3];
		String notifica = "notifica|"+dados[3];
		String estado = "edit";
		String tarefa = autor+" editou a tarefa com o id: "+ident;

		Servidor.tarefas.remove(ident);

		Task newTask = new Task(titulo, ident, autor);
		

		Servidor.tarefas.put(ident,newTask);

		FicheiroDeObjectos f = new FicheiroDeObjectos();
		try {
			f.abreEscrita("tarefas.dat");
			f.escreveObjecto(Servidor.tarefas);
			f.fechaEscrita();

		}catch (Exception e) {
			e.printStackTrace();
		}

		if (Servidor.autenticadosRMI.size() + Servidor.autenticados.size() > 1) {

			//Envia as tarefas directamente para os utilizadores ligados por RMI online
			if (Servidor.autenticadosRMI.size() > 0) {

				for (Enumeration <String>destinter = Servidor.autenticadosRMI.keys(); destinter.hasMoreElements();) {

					String key_inter = (String) destinter.nextElement();

					if (!key_inter.equals(dados[3])) {
						try {
							Servidor.autenticadosRMI.get(key_inter).printMensagem(notifica);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			//Envia as tarefas directamente para os utilizadores ligados por sockets online
			if (Servidor.autenticados.size() > 0) {

				for (Enumeration destout = Servidor.autenticados.keys(); destout.hasMoreElements();) {

					String key_out = (String) destout.nextElement();
					if (!key_out.equals(dados[3])) {
						try {

							Servidor.autenticados.get(key_out).writeUTF(notifica);
							Servidor.autenticados.get(key_out).flush();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}

			try {
				out.writeUTF(estado);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}


		}else{
			estado="offline";

			try {
				out.writeUTF(estado);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		for (Enumeration reg = Servidor.registos.keys(); reg.hasMoreElements();){

			String chave = (String) reg.nextElement();

			if (!Servidor.autenticados.containsKey(chave))

				Servidor.offline.add(chave);

		}
		for (int i = 0; i < Servidor.offline.size(); i++) {

			Servidor.registos.get(Servidor.offline.get(i)).tarefasporverificar.add(tarefa);
		}	
		Servidor.offline.clear();

	}



	public synchronized void deletetask(String[] dados, DataOutputStream out) {

		String ident = dados[1];
		String autor = dados[2];
		String estado = "delete";
		String notifica="notificaapaga|"+autor+"|"+ident;
		String tarefa = autor+" eliminou a tarefa com o id: "+ident;
		Servidor.tarefas.remove(ident);


		FicheiroDeObjectos f = new FicheiroDeObjectos();
		try {
			f.abreEscrita("tarefas.dat");
			f.escreveObjecto(Servidor.tarefas);
			f.fechaEscrita();

		}catch (Exception e) {
			e.printStackTrace();
		}

		if (Servidor.autenticadosRMI.size() + Servidor.autenticados.size() > 1) {

			//Envia as tarefas directamente para os utilizadores ligados por RMI online
			if (Servidor.autenticadosRMI.size() > 0) {

				for (Enumeration <String>destinter = Servidor.autenticadosRMI.keys(); destinter.hasMoreElements();) {

					String key_inter = (String) destinter.nextElement();

					if (!key_inter.equals(dados[2])) {
						try {
							Servidor.autenticadosRMI.get(key_inter).printMensagem(notifica);
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			//Envia as tarefas directamente para os utilizadores ligados por sockets online
			if (Servidor.autenticados.size() > 0) {

				for (Enumeration destout = Servidor.autenticados.keys(); destout.hasMoreElements();) {

					String key_out = (String) destout.nextElement();
					if (!key_out.equals(dados[2])) {
						try {

							Servidor.autenticados.get(key_out).writeUTF(notifica);
							Servidor.autenticados.get(key_out).flush();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			}

			try {
				out.writeUTF(estado);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}else {
			estado="offline";


			try {
				out.writeUTF(estado);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (Enumeration reg = Servidor.registos.keys(); reg.hasMoreElements();){

			String chave = (String) reg.nextElement();

			if (!Servidor.autenticados.containsKey(chave))

				Servidor.offline.add(chave);

		}
		for (int i = 0; i < Servidor.offline.size(); i++) {

			Servidor.registos.get(Servidor.offline.get(i)).tarefasporverificar.add(tarefa);

		}	
		Servidor.offline.clear();

	}

	public synchronized void reconnect(String[] dados,DataOutputStream out)throws IOException{

		Servidor.autenticados.put(dados[1], out);
	}


	public synchronized void online(String[] dados, DataOutputStream out) {


		String online = "online";

		if (Servidor.autenticadosRMI.size() + Servidor.autenticados.size() > 0) {
			System.out.println("\n\nEstao " + (Servidor.autenticadosRMI.size() + Servidor.autenticados.size()) + " users online:");
			//IMPRIME OS UTLiZADORES AUTENTICADOS
			Enumeration<String> e1 = Servidor.autenticadosRMI.keys();
			while (e1.hasMoreElements()) {
				online = online + "|" + (e1.nextElement());
			}
			Enumeration<String> e2 = Servidor.autenticados.keys();
			while (e2.hasMoreElements()) {
				online = online + "|" + (e2.nextElement());
			}



		} else {
			online = online + "|vazio";
		}

		try {
			out.writeUTF(online);
			out.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}

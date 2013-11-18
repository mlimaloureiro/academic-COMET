package project02;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

class Listener implements Runnable {
	Thread t;

	public Listener() {
		t = new Thread(this);
		System.out.println("Listen thread");
		t.start(); // Inicia a Thread
	}

	public void run() {
		executa();
	}

	public void excepcao(){
		int serversocket = 5500;
		try {
			// 1o passo
			Cliente.s = new Socket("localhost", serversocket);
			if (Cliente.s == null){
				System.out.println("NAO CONSEGUIU CRIAR O SOCKET");
			}

			// 2o passo
			Cliente.out = new DataOutputStream(Cliente.s.getOutputStream());
			Cliente.in = new DataInputStream(Cliente.s.getInputStream());
			System.out.println("Servidor de novo activo");

			Cliente.servidorOK=true;


			try {
				Cliente.out.writeUTF("reconnect|"+Cliente.identificacao);
				Cliente.out.flush();

				if(Cliente.temp_msg.size()>0&&Cliente.servidorOK==true){
					for(int i=0;i<Cliente.temp_msg.size();i++){

						Cliente.out.writeUTF(Cliente.temp_msg.get(i));
						Cliente.out.flush();	
					}

					Cliente.temp_msg.clear();
				}


			} catch (IOException e) {
				System.out.println("Warning: servidor em baixo");
			}




			executa();

		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			excepcao();
		} finally {
			if (Cliente.s != null)
				try {
					Cliente.s.close();
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
		}
	}

	public void executa(){
		try {
			while(Cliente.saidaOK!=true){
				String data = Cliente.in.readUTF();
				String dados[] = data.split("\\|");
				if (dados[0].equals("login")) {
					String resposta=dados[1];
					if (resposta.equals("accept")) {
						System.out.println("Bem-vindo!!!");
						Cliente.loginOK = true;
						synchronized(Cliente.sinc) { 
							Cliente.sinc.notify();
						}
					}
					else if(resposta.equals("denied")){
						System.out.println("Password errada!! Autenticar novamente.");
						Cliente.loginOK = false;
						synchronized(Cliente.sinc) { 
							Cliente.sinc.notify();
						}
					}
					else if(resposta.equals("wrongUser")){
						System.out.println("Utilizador nao existente");
						Cliente.loginOK = false;
						synchronized(Cliente.sinc) { 
							Cliente.sinc.notify();
						}
					}
					else if(resposta.equals("jaLogado")){
						System.out.println("Utilizador ja esta online");
						Cliente.loginOK = false;
						synchronized(Cliente.sinc) { 
							Cliente.sinc.notify();
						}
					}
				}

				else if (dados[0].equals("register")) {
					String resposta=dados[1];
					if (resposta.equals("accept")) {
						System.out.println("Registo efectuado com sucesso.");
						Cliente.registoOK = true;
						synchronized(Cliente.sinc) {
							Cliente.sinc.notify();
						}
					}
					else if (resposta.equals("denied")) {
						System.out.println("Registo nao efectuado. Registe-se novamente.");
						Cliente.registoOK = false;
						synchronized(Cliente.sinc) { 
							Cliente.sinc.notify();
						}
					} 
				}


				else if (dados[0].equals("tarefas")) {


					System.out.println("Listando tarefas:\n");

					for (int i = 1; i < dados.length; i++) {

						System.out.println(dados[i]);

					}




					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}

				else if (dados[0].equals("vazio")) {

					System.out.println("Lista de tarefas vazia:\n");

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}


				else if (dados[0].equals("newTask")) {

					System.out.println(dados[3]+" introduziu uma nova tarefa com o titulo: "+dados[1]+" e o id: "+dados[2]);

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}

				else if (dados[0].equals("edit")) {

					System.out.println("Tarefa alterada");

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}



				else if (dados[0].equals("delete")) {

					System.out.println("Tarefa Eliminada");

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}

				else if (dados[0].equals("notifica")) {

					System.out.println(dados[1]+" editou uma tarefa");

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}


				else if (dados[0].equals("notificaapaga")) {

					System.out.println(dados[1]+" apagou a tarefa com o seguinte ID: "+dados[2]);

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}


				else if (dados[0].equals("veri")) {

					for (int i = 1; i < dados.length; i++) {
						System.out.println(dados[i]);
					}
					
				

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}

				else if (dados[0].equals("enviado")) {

					System.out.println("tarefa enviada com sucesso");

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}

				else if (dados[0].equals("online")) {

					if (dados[1].equals("vazio")){

						System.out.println("Nao existem utilizadores Online");

					}else{

						System.out.println("Existem "+(dados.length-1)+" utilizadores online:\n");


						for (int i=1;i<dados.length;i++)

							System.out.println(dados[i]);

					}
					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}
				else if (dados[0].equals("offline")) {

					System.out.println("Nao existem mais utilizadores online..");

					synchronized(Cliente.sinc) {
						Cliente.sinc.notify();
					}

				}


			}
		} catch (IOException e) {
			System.out.println("Servidor em baixo");

			excepcao();
		}
	}
}

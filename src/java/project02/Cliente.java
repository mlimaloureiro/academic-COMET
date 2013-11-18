package project02;

import java.net.*;
import java.io.*;
import java.util.*;

public class Cliente {

	public static boolean loginOK = false;
	public static boolean registoOK = false;
	public static boolean saidaOK = false;
	public static boolean servidorOK = false;
	public static String identificacao;
	public static Object sinc = new Object();
	public static DataOutputStream out;
	public static DataInputStream in;
	public static Socket s;

	public static ArrayList <String> temp_msg = new ArrayList <String>();
	public static int MAXSIZE=10;




	public static void main(String args[]) {
		int serversocket = 5500;
		try {
			s = new Socket("localhost", serversocket);
			if (s == null) {
				System.out.println("NAO CONSEGUIU CRIAR O SOCKET");
			}

			out = new DataOutputStream(s.getOutputStream());
			in = new DataInputStream(s.getInputStream());
			new Listener();
			menu_inicial();


		} catch (UnknownHostException e) {
			System.out.println("Sock:" + e.getMessage());
		} catch (EOFException e) {
			System.out.println("EOF:" + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO:" + e.getMessage());
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("close:" + e.getMessage());
				}
			}
		}
	}

	public static void menu_inicial() {
		int opcao ;
		while (true) {

			saidaOK = false;
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

	public synchronized static void signIn() {

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
			out.writeUTF(mensagem);
			out.flush();
		} catch (IOException e1) {
			System.out.println("Warning: servidor em baixo");
			servidorOK = false;
			menu_inicial();
		}

		try {
			synchronized (sinc) {
				sinc.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		if (loginOK == true) {

			Cliente.identificacao = user;
			menuAposLogin();
		} else {
			menu_inicial();
		}
	}

	public synchronized static void register() {
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
			out.writeUTF(mensagem);
			out.flush();
		} catch (IOException e1) {
			System.out.println("Warning: servidor em baixo");
			servidorOK = false;
			menu_inicial();
		}
		try {
			synchronized (sinc) {
				sinc.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (registoOK==true) {

			menu_inicial();
		} else {
			register();
		}
	}

	public synchronized static void menuAposLogin() {
		int opcao;
		while (true) {
			
				
			
			
			System.out.println("\n+++++ Escolha a op��o que deseja +++++");
			System.out.println("1- Show tasks:");
			System.out.println("2- Add Task:");
			System.out.println("3- Edit Task:");
			System.out.println("4- Delete Task:");
			System.out.println("5- Online Users:");
			System.out.println("6- Refresh:");
			System.out.println("0- Logout:");
			System.out.print(": ");
				
						
			opcao = User.readInt();
			String msg = "";

			switch (opcao) {

			case 1:

				msg = "showtasks|"+Cliente.identificacao;

				try {

					out.writeUTF(msg);
					out.flush();

				}catch(IOException e){
					System.out.println("Warning: servidor em baixo");
					servidorOK = false;
					menuAposLogin();
				}
				try {
					synchronized (sinc) {
						sinc.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;


			case 2:


				System.out.println("Introduza o titulo da tarefa:");

				String titulo = User.readString();



				msg = "addtask|"+titulo+"|"+Cliente.identificacao;

				try {

					out.writeUTF(msg);
					out.flush();

				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					servidorOK = false;
					if(temp_msg.size()<MAXSIZE){
						temp_msg.add(msg);

					}else{
						System.out.println("N�o s�o aceites mais tarefas");
					}

					menuAposLogin();
				}
				try {
					synchronized (sinc) {
						sinc.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;



				//Logout
			case 0:

				saidaOK = true;
				msg = "logout|" + Cliente.identificacao;
				try {
					out.writeUTF(msg);
					out.flush();
				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");
					servidorOK = false;
					menuAposLogin();
				}
				menu_inicial();
				break;



			case 3:

				String tarefa="";
				String ttl="";


				System.out.println("Qual o id da tarefa que pretende alterar?");
				tarefa = User.readString();

				System.out.println("Insira o novo titulo da tarefa");
				ttl = User.readString();


				msg = "edit|"+tarefa+"|"+ttl+"|"+Cliente.identificacao;

				try {

					out.writeUTF(msg);
					out.flush();

				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");

					servidorOK = false;
					menuAposLogin();
				}
				try {
					synchronized (sinc) {
						sinc.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;		

			case 4:

				String user = Cliente.identificacao;
				String deleteID="";

				System.out.println("Introduza o ID da tarefa que pretende eliminar:");
				deleteID = User.readString();

				msg = "delete|"+deleteID+"|"+user;

				try {

					out.writeUTF(msg);
					out.flush();

				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");

					servidorOK = false;
					menuAposLogin();
				}
				try {
					synchronized (sinc) {
						sinc.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;	


				//Ver online
			case 5:

				String online = "online";

				try {

					out.writeUTF(online);
					out.flush();

				} catch (IOException e) {
					System.out.println("Warning: servidor em baixo");

					servidorOK = false;
					menuAposLogin();
				}
				try {
					synchronized (sinc) {
						sinc.wait();
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				break;


			}
		}
	}
}

package project02;

import java.io.*;
import java.net.*;


public class Alive extends Thread{

	int backupPort;
	String backupAddress;
	DatagramSocket backupSocket = null;

	public Alive(int port, String address){
		this.backupPort = port;
		this.backupAddress = address;
	}

	public void run(){
		try {
			backupSocket = new DatagramSocket();
			String texto = "I_AM_ALIVE";

			while(true){
				byte [] m = texto.getBytes();

				InetAddress aHost = InetAddress.getByName(backupAddress);                                                  
				DatagramPacket request = new DatagramPacket(m,m.length,aHost,backupPort);
				backupSocket.send(request);
				//System.out.println("Sent ping!");
				Thread.sleep(10000);

			}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e){System.out.println("IO: " + e.getMessage());
		}catch (InterruptedException e){System.out.println("Sleep:" + e.getMessage());
		}finally {if(backupSocket != null) backupSocket.close();}
	}
}



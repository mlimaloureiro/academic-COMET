package project02;

import java.io.*;

public class FicheiroDeObjectos implements Serializable
{
	private ObjectInputStream iS;
	private ObjectOutputStream oS;

	public void abreLeitura(String nomeDoFicheiro) {
		try {
			iS = new ObjectInputStream(new FileInputStream(nomeDoFicheiro));
		} catch (Exception e) {
			System.out.println ("N�o foi poss�vel abrir o ficheiro para leitura");
		}
	}

	public void abreEscrita(String nomeDoFicheiro) {
		try {
			oS = new ObjectOutputStream(new FileOutputStream(nomeDoFicheiro));
		} catch (Exception e) {
			System.out.println ("N�o foi poss�vel abrir o ficheiro para escrita");
		}
	}

	public Object leObjecto() {
		try {
			return iS.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println ("O ficheiro n�o cont�m objectos");
		}
		catch (Exception e1) {
			System.out.println ("N�o foi poss�vel ler o ficheiro");
		}
		return iS;
	}
	public boolean exist(String nomeDoFicheiro){
		try{
			iS = new ObjectInputStream(new FileInputStream(nomeDoFicheiro));
			return true;
		}catch (IOException e){
			return false;
		}
		
	}
	public void escreveObjecto(Object o) {
		try {
			oS.writeObject(o);
		} catch (Exception e) {
			System.out.println ("N�o foi poss�vel abrir escrever no ficheiro");
		}
	}

	public void fechaLeitura() {
		try {
			iS.close();
		} catch (Exception e) {
			System.out.println ("N�o foi poss�vel fechar o ficheiro");
		}
	}

	public void fechaEscrita() {
		try {
			oS.close();
		} catch (Exception e) {
			System.out.println ("N�o foi poss�vel fechar o ficheiro");
		}
	}
}
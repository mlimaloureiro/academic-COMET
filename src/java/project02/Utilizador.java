package project02;

import java.util.ArrayList;
import java.io.Serializable;

public class Utilizador implements Serializable {
	private String password;
	private String email;
	private String user;

	public ArrayList <String> tarefasporverificar = new ArrayList <String> ();

	// Construtor
	public Utilizador(String password, String email, String user) {
		this.password = password;
		this.email = email;
		this.user=user;

	}

	public String getEmail() {
		return this.email;
	}

	public String getPass() {
		return this.password;
	}

	public String getUser() {
		return this.user;
	}


}

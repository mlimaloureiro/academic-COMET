package project02;

import java.io.Serializable;

public class Task implements Serializable {
	private String titulo;
	private String ID;
	private String autor;
	



	// Construtor
	public Task(String titulo, String ID, String autor) {
		this.titulo = titulo;
		this.ID = ID;
		this.autor=autor;

	}

	public String getTitulo() {
		return this.titulo;
	}

	public String getID() {
		return this.ID; 
	}

	public String getautor() {
		return this.autor;
	}
	
	public String toString() {
		return "titulo: "+titulo+" -> autor: "+autor+" -> ID: "+ID;
	}
            
            
            public String toHtml(){
                return "<script type='text/javascript'>document.getElementById('tasks').innerHTML='';</script><div class='list list-32'>"
                            + "<div class='list-info'>"
                                + "<div class='list-head'><form onSubmit='editTask(\""+ID+"\")'><input id='tarefa"+ID+"' style='width:401px;' type='text' class='txt-shout' placeholder='"+titulo+"'/>"
                                + "<input  class='formSubmit form-yellow' type='submit' name='submit-message' value='Editar'/></form>"
                                + "</div>"
                                + "<div class='list-body'>Por: "+autor+"</div>"
                            +"</div>"
                            
                        
                            +"<div class='list-info' style='float:right'>"
                                +"<form onSubmit='deleteTask(\""+ID+"\")'>"
                                    //+"<input type='text' class='txt-shout' style='display:none' value='"+titulo+"'/>"
                                    +"<input class='formSubmit form-red' type='submit' name='submit-message' value='Eliminar'/>"
                                +"</form>"
                            +"</div>"
                        /*
                            +"<div class='list-info' style='float:right'>"
                                +"<form onSubmit='editTask()'>"
                                    +"<input  class='formSubmit form-yellow' type='submit' name='submit-message' value='Editar'/>"
                                +"</form>"
                            +"</div>"*/
                        +"</div>";
            }

}
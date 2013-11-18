package project02;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.io.*; 
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.net.*;

public class Servidor implements Interface_Servidor {

    public static Hashtable<String, Utilizador> registos = new Hashtable<String, Utilizador>();
    public static Hashtable<String, DataOutputStream> autenticados = new Hashtable<String, DataOutputStream>();
    public static Hashtable<String, Interface_Cliente> autenticadosRMI = new Hashtable<String, Interface_Cliente>();
    public static Hashtable<String, Task> tarefas = new Hashtable<String, Task>();
    public static Hashtable<String, Task> tarefasnaorec = new Hashtable<String, Task>();
    public static Hashtable<String, String> notificationlist = new Hashtable<String,String>();
    public static int ID = 0;
    public static int notificationcounter = 0;
    public static ArrayList<String> offline = new ArrayList<String>();
    public static ArrayList<String> autenticadosHTML = new ArrayList<String>();
    public static String notification = "Sem actividade recente";
    static int backupPort = 6789;
    static int primaryPort = 5500;
    static ServerSocket ss;
    static String backupAddress = "localhost";
    static boolean primaryServerStatus = false;

    public Servidor() throws RemoteException {
        super();
    }

    public static void main(String[] args) throws Exception {



        try {
            ss = null;
            ss = new ServerSocket(primaryPort);
            ss.setReuseAddress(true);
        } catch (IOException e) {
            primaryServerStatus = true;
        } finally {
            if (ss != null) {
                ss.close();
            }
        }

        if (primaryServerStatus == true) {
            Connection.checkPrimaryServer();
        }

        Alive pinger = new Alive(backupPort, backupAddress);
        pinger.start();

        try {
            int port = 5500;
            FicheiroDeObjectos f = new FicheiroDeObjectos();


            if (f.exist("tarefas.dat")) {
                f.abreLeitura("tarefas.dat");
                tarefas = (Hashtable<String, Task>) f.leObjecto();
                f.fechaLeitura();
            } else if (!f.exist("tarefas.dat")) {
                File f2;
                f2 = new File("tarefas.dat");
                if (!f2.exists()) {
                    try {
                        f2.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("file created");
                }
            }


            if (f.exist("registos.dat")) {
                f.abreLeitura("registos.dat");
                registos = (Hashtable<String, Utilizador>) f.leObjecto();
                f.fechaLeitura();
            } else if (!f.exist("registos.dat")) {
                File f2;
                f2 = new File("registos.dat");
                if (!f2.exists()) {
                    try {
                        f2.createNewFile();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    System.out.println("file created");
                }
            }

            int max = 0;
            int num = 0;

            if (!tarefas.isEmpty()) {
                for (Enumeration en = tarefas.keys(); en.hasMoreElements();) {

                    String task = (String) en.nextElement();

                    num = Integer.parseInt(tarefas.get(task).getID());

                    if (num > max) {
                        max = num;
                    }
                }

                ID = max;
                System.out.println(ID + " é o total de tarefas existentes");

            } else {
                System.out.println("Não existem tarefas criadas no sistema");
            }


            //===================================================================
            //RMI
            Servidor s = new Servidor();
            Registry registry = LocateRegistry.createRegistry(7000);
            // Exporta o objeto remoto
            Interface_Servidor stub = (Interface_Servidor) UnicastRemoteObject.exportObject(s, 0);
            registry.rebind("ServidorRmi", stub);
            //===================================================================

            System.out.println("A Escuta no Porto 5500");
            ServerSocket listenSocket = new ServerSocket(port);
            while (true) {
                //SOCKETS
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket);
            }
        } catch (IOException e) {
            System.out.println("Listen:" + e.getMessage());
            e.printStackTrace();
        }



    }

    public synchronized String login(String data, Interface_Cliente ic) throws RemoteException {
        String dados[] = data.split("\\|");
        String resposta = "";
        String user = dados[1];
        String password = dados[2];


        //Se o utilizador nao estiver logado
        if (autenticadosRMI.containsKey(user) == false && autenticados.containsKey(user) == false && autenticadosHTML.contains(user) == false) {
            // Metodo que trata da autenticacao de um utilizador(login)
            if (registos.containsKey(user) == true) {
                if (registos.get(user).getPass().equals(password) == true) {
                    resposta = "login|accept";
                    autenticadosRMI.put(user, ic);


                    if (registos.get(user).tarefasporverificar.size() > 1) {
                        resposta = resposta + "|tarefa";
                    } else {
                        resposta = resposta + "|nada";
                    }

                    notification = user+" ficou online";
                    PrintOnline();
                    return resposta;
                } else {
                    resposta = "login|denied";
                    return resposta;
                }
            } else {
                resposta = "login|wrongUser";
                return resposta;
            }
        } else {
            resposta = "login|jaLogado";
            return resposta;
        }
    }

    public synchronized String loginHTML(String data) throws RemoteException {
        String dados[] = data.split("\\|");
        String resposta = "";
        String user = dados[1];
        String password = dados[2];



        //Se o utilizador nao estiver logado
        if (autenticadosRMI.containsKey(user) == false && autenticados.containsKey(user) == false && autenticadosHTML.contains(user) == false) {
            // Metodo que trata da autenticacao de um utilizador(login)
            if (registos.containsKey(user) == true) {
                if (registos.get(user).getPass().equals(password) == true) {
                    resposta = "login|accept|" + registos.get(user).getEmail();
                    System.out.println(resposta);
                    autenticadosHTML.add(user);
                    notification = user+" ficou online";
                    PrintOnline();
                    return resposta;
                } else {
                    resposta = "login|denied";
                    return resposta;
                }
            } else {
                resposta = "login|wrongUser";
                return resposta;
            }
        } //Se o utilizador ja estiver logado
        else {
            resposta = "login|jaLogado";
            return resposta;
        }
    }
    
    public synchronized String getLastNotification() throws RemoteException{
        return notification;
    }
    
    public synchronized String getNotificationList() throws RemoteException{
        
        String notification = "";
        String aux = "";

        if (!Servidor.notificationlist.isEmpty()) {

            for (Enumeration en = Servidor.notificationlist.keys(); en.hasMoreElements();) {

                String not = (String) en.nextElement();

                aux = aux + not;

                System.out.println(aux);
            }

            notification = notification + aux;

 
            return notification;

        } else {
            notification = "Sem actividade recente";
        }

        return notification;
    }

    public synchronized void logout(String data) throws RemoteException {
        String dados[] = data.split("\\|");
        String user = dados[1];
        if (autenticadosRMI.containsKey(user) == true) {
            notification = user+" ficou offline";
            autenticadosRMI.remove(user);
        } else if (autenticadosHTML.contains(user) == true) {
            notification = user+" ficou offline";
            autenticadosHTML.remove(user);
        }

        PrintOnline();
    }

    public synchronized String register(String data) throws RemoteException {
        String dados[] = data.split("\\|");

        String email = dados[1];
        String password = dados[2];
        String user = dados[3];
        String resposta = "";

        if (registos.containsKey(user)) {
            resposta = "register|denied";
            return resposta;
        } else {
            Utilizador newUser = new Utilizador(password, email, user);
            registos.put(user, newUser);
            FicheiroDeObjectos f = new FicheiroDeObjectos();
            f.abreEscrita("registos.dat");
            f.escreveObjecto(registos);
            f.fechaEscrita();
            resposta = "register|accept";
            return resposta;
        }
    }

    public synchronized String tarefas(String data) {

        String user = data;
        String tarefas = "veri|";

        for (int j = 0; j < registos.get(user).tarefasporverificar.size(); j++) {

            tarefas = tarefas + (registos.get(user).tarefasporverificar.get(j)) + "|";
        }

        return tarefas;

    }

    public synchronized String showTasksHTML(){
        
        String tsk = "";
        String aux = "";

        if (!Servidor.tarefas.isEmpty()) {

            for (Enumeration en = Servidor.tarefas.keys(); en.hasMoreElements();) {

                String task = (String) en.nextElement();

                aux = aux + Servidor.tarefas.get(task).toHtml();

                System.out.println(aux);
            }

            tsk = tsk + aux;

 
            return tsk;

        } else {
            tsk = "Sem tarefas";
        }

        return tsk;
    }
    
    public synchronized String showtasks(String data) {

        String tsk = "tarefas";
        String aux = "";
        String autor = data;

        if (!Servidor.tarefas.isEmpty()) {

            for (Enumeration en = Servidor.tarefas.keys(); en.hasMoreElements();) {

                String task = (String) en.nextElement();

                aux = aux + "|" + Servidor.tarefas.get(task).toString();

            }

            tsk = tsk + aux;


            return tsk;

        } else {
            tsk = "vazio";
        }

        return tsk;

    }

    public synchronized String addtask(String data) {

        String dados[] = data.split("\\|");

        Servidor.ID++;

        String resposta;
        String titulo = dados[0];
        String autor = dados[1];
        String ident = Integer.toString(Servidor.ID);
        String estado = "enviado";
        String tarefa = autor + " adicionou uma tarefa com o id: " + ident;

        String mensagem = "newTask" + "|" + titulo + "|" + ident + "|" + autor;

        Task newTask = new Task(titulo, ident, autor);
        Servidor.tarefas.put(ident, newTask);

        FicheiroDeObjectos f = new FicheiroDeObjectos();
        try {
            f.abreEscrita("tarefas.dat");
            f.escreveObjecto(Servidor.tarefas);
            f.fechaEscrita();

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Enumeration reg = registos.keys(); reg.hasMoreElements();) {

            String chave = (String) reg.nextElement();

            if (!autenticados.containsKey(chave)) {
                offline.add(chave);
            }
        }
        for (int i = 0; i < offline.size(); i++) {

            registos.get(offline.get(i)).tarefasporverificar.add(tarefa);

        }

        offline.clear();

        notification = " Tarefa "+ ident + " adicionada por " + autor;

        if (autenticadosRMI.size() + autenticados.size() > 0) {

            //Envia as tarefas directamente para os utilizadores ligados por RMI online
            if (autenticadosRMI.size() > 0) {

                for (Enumeration<String> destinter = autenticadosRMI.keys(); destinter.hasMoreElements();) {

                    String key_inter = (String) destinter.nextElement();

                    if (!key_inter.equals(dados[1])) {
                        try {
                            autenticadosRMI.get(key_inter).printMensagem(mensagem);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            }

            //Envia as tarefas directamente para os utilizadores ligados por sockets online
            if (autenticados.size() > 0) {

                for (Enumeration destout = autenticados.keys(); destout.hasMoreElements();) {

                    String key_out = (String) destout.nextElement();
                    if (!key_out.equals(dados[1])) {
                        try {
                            autenticados.get(key_out).writeUTF(mensagem);
                            autenticados.get(key_out).flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return estado;


        } else {


            estado = "Tarefa introduzida, no entanto n�o h� mais utilizadores online";

            return estado;
        }


    }

    public synchronized String edit(String data) {

        String dados[] = data.split("\\|");

        String ident = dados[0];
        String titulo = dados[1];
        String autor = dados[2];
        String msg = "Tarefa alterada";
        String notifica = "notifica|" + dados[2];
        String off = "tarefa editada, não há mais utilizadores online";
        String tarefa = autor + " editou a tarefa com o id: " + ident;

        Servidor.tarefas.remove(ident);

        Task newTask = new Task(titulo, ident, autor);
        Servidor.tarefas.put(ident, newTask);

        FicheiroDeObjectos f = new FicheiroDeObjectos();
        try {
            f.abreEscrita("tarefas.dat");
            f.escreveObjecto(Servidor.tarefas);
            f.fechaEscrita();

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Enumeration reg = registos.keys(); reg.hasMoreElements();) {

            String chave = (String) reg.nextElement();

            if (!autenticados.containsKey(chave)) {
                offline.add(chave);
            }
        }
        for (int i = 0; i < offline.size(); i++) {

            registos.get(offline.get(i)).tarefasporverificar.add(tarefa);

        }

        offline.clear();

        notification = " Tarefa "+ ident + " editada por " + autor;

        if (autenticadosRMI.size() + autenticados.size() > 1) {

            //Envia as tarefas directamente para os utilizadores ligados por RMI online
            if (autenticadosRMI.size() > 0) {

                for (Enumeration<String> destinter = autenticadosRMI.keys(); destinter.hasMoreElements();) {

                    String key_inter = (String) destinter.nextElement();

                    if (!key_inter.equals(dados[2])) {
                        try {
                            autenticadosRMI.get(key_inter).printMensagem(notifica);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

            //Envia as tarefas directamente para os utilizadores ligados por sockets online
            if (autenticados.size() > 0) {

                for (Enumeration destout = autenticados.keys(); destout.hasMoreElements();) {

                    String key_out = (String) destout.nextElement();
                    if (!key_out.equals(dados[2])) {
                        try {
                            autenticados.get(key_out).writeUTF(notifica);
                            autenticados.get(key_out).flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

            return msg;

        } else {
            return off;
        }

    }

    public synchronized String deletetask(String msg) throws RemoteException {

        String dados[] = msg.split("\\|");

        String ident = dados[0];
        String estado = "tarefa eliminada";
        String notifica = "notificaapaga|" + dados[1] + "|" + ident;
        String off = "tarefa apagada, n�o h� mais utilizadores online";
        String tarefa = dados[1] + " eliminou a tarefa com o id: " + ident;

        Servidor.tarefas.remove(ident);

        FicheiroDeObjectos f = new FicheiroDeObjectos();
        try {
            f.abreEscrita("tarefas.dat");
            f.escreveObjecto(Servidor.tarefas);
            f.fechaEscrita();

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (Enumeration reg = registos.keys(); reg.hasMoreElements();) {

            String chave = (String) reg.nextElement();

            if (!autenticados.containsKey(chave)) {
                offline.add(chave);
            }
        }
        for (int i = 0; i < offline.size(); i++) {

            registos.get(offline.get(i)).tarefasporverificar.add(tarefa);

        }

        offline.clear();

        notification = " Tarefa "+ ident + " eliminada";

        
        if (autenticadosRMI.size() + autenticados.size() > 1) {

            //Envia as tarefas directamente para os utilizadores ligados por RMI online
            if (autenticadosRMI.size() > 0) {

                for (Enumeration<String> destinter = autenticadosRMI.keys(); destinter.hasMoreElements();) {

                    String key_inter = (String) destinter.nextElement();

                    if (!key_inter.equals(dados[1])) {
                        try {
                            autenticadosRMI.get(key_inter).printMensagem(notifica);
                        } catch (RemoteException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }

            }

            //Envia as tarefas directamente para os utilizadores ligados por sockets online
            if (autenticados.size() > 0) {

                for (Enumeration destout = autenticados.keys(); destout.hasMoreElements();) {

                    String key_out = (String) destout.nextElement();
                    if (!key_out.equals(dados[1])) {
                        try {
                            autenticados.get(key_out).writeUTF(notifica);
                            autenticados.get(key_out).flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

            return estado;

        } else {
            return off;
        }

    }

    public synchronized void reconnecta(String data, Interface_Cliente ic) throws RemoteException {

        autenticadosRMI.put(data, ic);


    }

    public synchronized String online() throws RemoteException {

        String online = "online";

        if (Servidor.autenticadosRMI.size() + Servidor.autenticados.size() + Servidor.autenticadosHTML.size() > 0) {

            //IMPRIME OS UTLiZADORES AUTENTICADOS
            Enumeration<String> e1 = Servidor.autenticadosRMI.keys();
            while (e1.hasMoreElements()) {
                online = online + "|" + (e1.nextElement());
            }

            Enumeration<String> e2 = Servidor.autenticados.keys();
            while (e2.hasMoreElements()) {
                online = online + "|" + (e2.nextElement());
            }

            if (!autenticadosHTML.isEmpty()) {
                for (int i = 0; i < autenticadosHTML.size(); i++) {
                    online = online + "|" + (autenticadosHTML.get(i));
                }
            }


        } else {
            online = online + "|vazio";
        }

        return online;

    }

    public synchronized void PrintOnline() {
        if (autenticadosRMI.size() + autenticados.size() + autenticadosHTML.size() > 0) {
            System.out.println("\n\nEstao " + (autenticadosRMI.size() + autenticados.size() + autenticadosHTML.size()) + " users online:");
            //IMPRIME OS UTLiZADORES AUTENTICADOS
            Enumeration<String> e1 = autenticadosRMI.keys();
            while (e1.hasMoreElements()) {
                System.out.println(e1.nextElement());
            }
            Enumeration<String> e2 = autenticados.keys();
            while (e2.hasMoreElements()) {
                System.out.println(e2.nextElement());
            }

            for (int i = 0; i < autenticadosHTML.size(); i++) {
                System.out.println(autenticadosHTML.get(i));
            }

            System.out.println("");
        } else {
            System.out.println("\n\nNao existem users online.\n");
        }
    }
}

package server;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    Vector<ClientHandler>clients;

    ExecutorService executorService= Executors.newCachedThreadPool();

    private static final Logger log=Logger.getLogger(Server.class);


    public Server() throws SQLException {
        AuthService.connect();
        ServerSocket server=null;
        Socket socket=null;

        try {
            clients=new Vector<>();
            server=new ServerSocket(8189);
            System.out.println("the server is running");
            log.info("the server is running");

            while(true){
                socket=server.accept();
                System.out.println("client is connected");
                log.info("client is connected");
                new ClientHandler(this,socket);
            }
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }
        finally {

            try {
                socket.close();
            } catch (IOException e) {
                log.error(e);
                e.printStackTrace();
            }
            try {
                server.close();

            } catch (IOException e) {
                log.error(e);
                e.printStackTrace();
            }
            AuthService.disconnect();

        }

    }
    public void broadcastMsg(String str,String sender){
        AuthService.addMessageToDataBase(sender,null,str,"0000");
        log.info(sender+" sent all: "+str);
        for(ClientHandler o:clients){
           o.sendMsg(sender+": "+str);
        }
    }

    public void broadcastMsg(String str,String sender,String receiver){
        AuthService.addMessageToDataBase(sender,receiver,str,"0000");
        log.info(sender+" sent to "+receiver+": "+str);
        for(ClientHandler o:clients){
            if(o.getNick().equals(sender)||o.getNick().equals(receiver))
            o.sendMsg("privat ["+sender+"] to ["+receiver+"]: "+str);
        }
    }
    public void subscribe(ClientHandler clientHandler){
        clients.add(clientHandler);
        log.info(clientHandler.getNick()+" is authorized");
        broadcastClientList();
        clientHandler.sendMsg(AuthService.getMessagesFromDBToNick(clientHandler.getNick()));
    }
    public void unsubscribe(ClientHandler clientHandler){
        clients.remove(clientHandler);
        log.info(clientHandler.getNick()+" is disconnected");
        broadcastClientList();
    }
    public boolean isNickAuthorized(String nick){
        for(ClientHandler o:clients){
            if(o.getNick().equals(nick))return true;
        }
        return false;
    }
    public void broadcastClientList(){
        StringBuilder sb=new StringBuilder();
        sb.append("/clientlist ");
        for(ClientHandler o:clients){
            sb.append(o.getNick()+" ");
        }
        String msg=sb.toString();
        for(ClientHandler o:clients){
           o.sendMsg(msg);
        }
    }
}

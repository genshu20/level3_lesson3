package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    DataInputStream in;
    DataOutputStream out;
    private String nick;
    private  String login;

    public ClientHandler(Server server, Socket socket) {
        try {
        this.server = server;
        this.socket = socket;
        in=new DataInputStream(socket.getInputStream());
        out=new DataOutputStream(socket.getOutputStream());

        new Thread(()-> {
                try {
                    socket.setSoTimeout(60000);
//                    new Thread(()-> {
//                        try{
//                            Thread.sleep(5000);
//                        }catch (InterruptedException e){e.printStackTrace();}
//                        if(!server.isNickAuthorized(nick)){
//                            try {
//                                in.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
// //                           throw new RuntimeException("client disconnected by tormoz");
//                        }
//                    }).start();
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/auth")) {
                            String[] token = str.split(" ");
                            String newNick = AuthService.getNickByLoginAndPass(token[1], token[2]);
                            if (newNick != null) {
                                if (!server.isNickAuthorized(newNick)) {
                                    sendMsg("/authOk");
                                    login=token[1];
                                    nick = newNick;
                                    out.writeUTF("/get "+login+" "+nick);
                                    server.subscribe(this);
                                    try {
                                        socket.setSoTimeout(0);
                                    }catch(SocketException e){e.printStackTrace();}
                                    break;
                                } else {
                                    sendMsg("allready use");
                                }
                            } else {
                                sendMsg("wrong login/password");
                            }
                        }
                        if (str.startsWith("/reg")){
                            String[] token = str.split(" ");

                            if(AuthService.registration(token[1],token[2],token[3])){
                                sendMsg("Reg OK");
                            }else{
                                sendMsg("Reg fail");
                            }
                        }
                    }
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals("/end")) {
                            out.writeUTF("/end");
                            System.out.println("client is disconnect");
                            break;
                        }
                        if (str.startsWith("/edit")){
                            String[] token = str.split(" ");

                            if(AuthService.edition(nick,token[1],token[2],token[3])){
                                sendMsg("Ed OK");
                            }else{
                                sendMsg("Ed fail");
                            }
                            nick=token[3];
                            login=token[1];
                            out.writeUTF("/get "+login+" "+nick);
                        }

                        if (str.startsWith("/w ")) {
                            String[] token = str.split(" +", 3);
                            server.broadcastMsg(token[2], nick, token[1]);
                        } else {
                            System.out.println(str);
                            server.broadcastMsg(str, nick);
                        }
                    }
                }catch (SocketTimeoutException e){
                    System.out.println("timeout socket hashCode() "+this.hashCode());
                    sendMsg("/end");
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    server.unsubscribe(this);
                    System.out.println("client is disconnect");
                }
        }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMsg(String str){
        try {
            out.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNick() {
        return nick;
    }
}

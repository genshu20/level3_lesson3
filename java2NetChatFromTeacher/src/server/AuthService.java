package server;

import java.sql.*;

public class AuthService {
    private static Connection connection;
    private static Statement stmt;
    public static void connect() throws SQLException {
//        try {
//            Class.forName("org.sqlite.JDBC");      //работает без этого. Почему???
            connection= DriverManager.getConnection("jdbc:sqlite:main.db");
            stmt=connection.createStatement();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

    }
    public static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getNickByLoginAndPass(String login,String pass){
        String sql=String.format("SELECT nickname FROM main\n" +
                "WHERE login = '%s'\n" +
                "and password='%s'",login,pass);
        try {
            ResultSet rs=stmt.executeQuery(sql);
            if(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void addMessageToDataBase(String sender,String receiver,String text,String date){
        if(!text.startsWith("/")){
            String sql=String.format("INSERT INTO messages(sender,receiver,text,date)\n" +
                    "VALUES('%s','%s','%s','%s')",sender,receiver,text,date);
            try {
                stmt.executeUpdate(sql);
                stmt.executeUpdate("delete from messages where id=(select min(id) from messages)\n");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public static String getMessagesFromDBToNick(String nick){
        String sql=String.format("SELECT*FROM messages\n" +
                "WHERE sender='%s'\n" +
                "OR receiver='%s'\n" +
                "OR receiver='null'",nick,nick);
        StringBuilder sb=new StringBuilder();

        try {
            ResultSet rs=stmt.executeQuery(sql);
            while (rs.next()){
                String sender=rs.getString(2);
                String receiver=rs.getString(3);
                String text=rs.getString(4);
                String date=rs.getString(5);

                if(receiver.equals("null")){
                    sb.append(sender+": "+text+"\n");
                }else{
                   sb.append("privat ["+sender+"] to ["+receiver+"]: "+text+"\n");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    public static boolean registration(String login,String password,String nickname){
        String sql=String.format("INSERT INTO main(login,password,nickname)\n" +
                "VALUES('%s','%s','%s')",login,password,nickname);
        int count=0;
        try {
            count=stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count>0;
    }
    public static boolean edition(String oldNick,String login,String password,String nickname){
        String sql=String.format("UPDATE main.main SET login = '%s', password = '%s', nickname = '%s' WHERE nickname = '%s';",login,password,nickname,oldNick);
        int count=0;
        try {
            count=stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count>0;
    }
}

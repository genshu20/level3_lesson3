package server;

import org.apache.log4j.Logger;

import java.sql.SQLException;

public class Start {
    private static final Logger logStart=Logger.getLogger(Server.class);
    public static void main(String[] args) {
        try {

            new Server();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

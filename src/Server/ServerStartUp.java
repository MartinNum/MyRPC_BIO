package Server;

import RPC_Server.HelloService;
import RPC_Server.HelloServiceImp;

import java.io.IOException;

public class ServerStartUp {
    public static void main(String[] args){
        Server server = new Server();
        server.registry(HelloService.class, HelloServiceImp.class);
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

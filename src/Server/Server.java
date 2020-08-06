package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private static final HashMap<String, Class> registryCenter = new HashMap<>();

    public void registry(Class classInterface, Class classImp){
        System.out.println("注册：" + classInterface.getName());
        registryCenter.put(classInterface.getName(), classImp);
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(8080));
        System.out.println("等待客户端连接...");
        try {
            while (true){
                ServerSocketRunable serverSocketRunable = new ServerSocketRunable(serverSocket.accept());
                serverSocketRunable.run();
            }
        }finally {
            serverSocket.close();
        }
    }

    public class ServerSocketRunable implements Runnable{

        private Socket socket;

        public ServerSocketRunable(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Server启动！");
            ObjectInputStream objectInputStream = null;
            ObjectOutputStream objectOutputStream = null;
            try {
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                System.out.println("Server等待接收...");
                String className =objectInputStream.readUTF();
                String methodName = objectInputStream.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) objectInputStream.readObject();
                Object[] parameters = (Object[]) objectInputStream.readObject();

                System.out.println("Server收到" + className);
                Class serverClass = registryCenter.get(className);
                Method method = serverClass.getMethod(methodName, parameterTypes);
                Object result = method.invoke(serverClass.newInstance(), parameters);
                // Server将结果回复给Client
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectOutputStream.writeObject(result);
            } catch (Exception e) {
                System.out.println(e);
            }finally {
                if (objectInputStream != null)
                    try {
                        objectInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (objectOutputStream != null)
                    try {
                        objectOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}


package Client;

import MyProxy.MyInvokeHandler;
import RPC_Server.HelloService;

import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args){
        MyInvokeHandler myInvokeHandler = new MyInvokeHandler(HelloService.class);
        HelloService helloService = (HelloService) Proxy.newProxyInstance(HelloService.class.getClassLoader(), new Class<?>[]{HelloService.class}, myInvokeHandler);
        String result = helloService.sayHello("Martin");
        System.out.println(result);
    }

}

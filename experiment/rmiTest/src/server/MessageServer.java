package server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

/**
 * @className: MessageServer
 * @description: 用于启动服务并将服务绑定到注册中心。
 * @author: Yang Xixuan
 **/
public class MessageServer {
    /**
     * RMI注册端口号
     */
    private static final int PORT = 8888;

    /**
     * 绑定的远程对象的名称
     */
    private static final String SERVICE_NAME = "rmi://localhost:8888/MessageManager";

    /**
     * 远程对象
     */
    private static MessageManager messageManager;

    /**
     * @param :
     * @return null
     * @author yangxixuan
     * @description 启动RMI服务, 并将远程对象注册到RMI注册服务器上, 并等待客户端的调用
     */
    MessageServer() {
        try {
//            1.启动 RMI 注册服务，指定端口为 8888，这一步必不可少（否则无法绑定对象到远程注册表中)
            LocateRegistry.createRegistry(PORT);
//            2.创建一个远程对象
            messageManager = new MessageManager();
//            3.把远程对象注册到 RMI 注册服务器上，并命名为 MessageManager
//              绑定的 URL 格式为：rmi://host:port/name
            Naming.rebind(SERVICE_NAME, messageManager);
            System.out.println("MeetingServer is ready.");
        } catch (Exception e) {
            System.out.println("MeetingServer failed: " + e);
        }
    }

    public static void main(String[] args) {
        new MessageServer();
    }
}

package client;

import rface.MessageInterface;

import java.rmi.Naming;
import java.util.Scanner;

/**
 * @className: Client
 * @description: 用于启动客户端并调用远程对象的方法。
 * @author: Yang Xixuan
 **/
public class Client {

    /**
     * 远程对象url
     */
    private static final String url = "rmi://localhost:8888/MessageManager";

    /**
     * 远程对象的代理
     */
    private static MessageInterface messageInterface;

    /**
     * 键盘输入
     */
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Client().start();
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 启动客户端并调用远程对象的方法
     */
    private void start() {
        login();
        while (true) {
//            "1. register : register a new account.\n" +
//            "usage : register [username] [password]\n" +
//            "2. showusers : show all registered users.\n" +
//            "usage : showusers\n" +
//            "3. checkmessages : check all messages you have received.\n" +
//            "usage : checkmessages [username] [password]\n" +
//            "4. leavemessage : send a message to another user.\n" +
//            "usage : leavemessage [username] [password] [othername] [message]\n" +
//            "5. help : show all commands.\n" +
//            "usage : help\n" +
//            "6. exit : exit the system.\n" +
//            "usage : exit";
            System.out.println("Please input your command:");
            String command = scanner.nextLine();
            String[] commands = command.split(" ");
            try {
                switch (commands[0]) {
                    case "register":
                        if (commands.length != 3) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(messageInterface.register(commands[1], commands[2]));
                        break;
                    case "showusers":
                        if (commands.length != 1) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(messageInterface.showAllUsers());
                        break;
                    case "checkmessages":
                        if (commands.length != 3) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(messageInterface.checkMessages(commands[1], commands[2]));
                        break;
                    case "leavemessage":
                        if (commands.length < 5) {
                            System.out.println("invalid command");
                            break;
                        }
//                        留言信息可能有空格，所以需要将标题的所有参数拼接起来，最后一个参数后不用加空格
                        String message = "";
                        for (int i = 4; i < commands.length; i++) {
                            message += commands[i];
                            if (i != commands.length - 1) {
                                message += " ";
                            }
                        }
                        System.out.println(messageInterface.sendMessage(commands[1], commands[2], commands[3], message));
                        break;
                    case "help":
                        if (commands.length != 1) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(messageInterface.help());
                        break;
                    case "exit":
                        if (commands.length != 1) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(messageInterface.exit());
                        break;
                    default:
                        System.out.println("Invalid command.");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 登录远程对象
     */
    private void login() {
        try {
//            1.通过RMI注册服务器获取远程对象的存根
            messageInterface = (MessageInterface) Naming.lookup(url);
//            2.调用远程对象的方法
            System.out.println(messageInterface.help());
        } catch (Exception e) {
            System.out.println("Client failed: " + e);
        }
    }
}

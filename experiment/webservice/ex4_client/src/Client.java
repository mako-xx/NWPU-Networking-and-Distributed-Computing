import server.Exception_Exception;
import server.TodoService;
import server.TodoServiceService;

import java.util.Scanner;

/**
 * @className: Client
 * @description: 客户端，用于测试服务端的功能
 * @author: Yang Xixuan
 **/
public class Client {

    /**
     * 键盘输入
     */
    private static Scanner scanner = new Scanner(System.in);

    /**
     * 服务端
     */
    private static TodoService todoService;

    public Client() {
        todoService = new TodoServiceService().getTodoServicePort(); // 获取服务端
        try {
            System.out.println(todoService.help()); // 打印帮助信息
        } catch (Exception_Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }

    private void start() {
        while (true) {
            System.out.println("please input command:");
            String command = scanner.nextLine();
            String[] commands = command.split(" ");
            try {
                switch (commands[0]) {
                    case "register":
                        if (commands.length != 3) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(todoService.userRegister(commands[1], commands[2]));
                        break;
                    case "login":
                        if (commands.length != 3) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(todoService.userLogin(commands[1], commands[2]));
                        break;
                    case "logout":
                        if (commands.length != 1) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(todoService.userLogout());
                        break;
                    case "add":
                        if (commands.length < 3) {
                            System.out.println("invalid command");
                            break;
                        }
//                        标题可能有空格，所以需要将标题的所有参数拼接起来，最后一个参数后不用加空格
                        String title = "";
                        for (int i = 3; i < commands.length; i++) {
                            title += commands[i] + " ";
                        }
                        title = title.substring(0, title.length() - 1);
                        System.out.println(todoService.addTodoItem(commands[1], commands[2], title));
                        break;
                    case "query":
                        if (commands.length != 3) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(todoService.queryTodoItem(commands[1], commands[2]));
                        break;
                    case "delete":
                        if (commands.length != 2) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(todoService.deleteTodoItem(commands[1]));
                        break;
                    case "clear":
                        if (commands.length != 1) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(todoService.clearTodoItem());
                        break;
                    case "help":
                        if (commands.length != 1) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(todoService.help());
                        break;
                    default:
                        System.out.println("command not found");
                        break;
                }
            } catch (Exception_Exception e) {
                System.out.println("error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}



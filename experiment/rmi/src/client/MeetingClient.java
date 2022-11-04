package client;

import rface.MeetingInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * @className: MeetingClient
 * @description: 用于启动客户端并调用远程对象的方法。
 * @author: Yang Xixuan
 **/
public class MeetingClient {

    /**
     * 远程对象的代理
     */
    private static MeetingInterface meetingManager;

    /**
     * 键盘输入
     */
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new MeetingClient().start();
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 启动客户端并调用远程对象的方法
     */
    public void start() {
        login();
        while (true) {
//            1. register -u username -p password
//            2. login -u username -p password
//            3. logout
//            4. add -p participator -s startTime -e endTime -t title
//            5. query -s startTime -e endTime
//            6. delete -u uuid
//            7. clear
//            8. listUsers [[only for admin]]
//            9. listMeetings [[only for admin]]
//            10. currentStatus
//            11. help
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
                        System.out.println(meetingManager.userRegister(commands[1], commands[2]));
                        break;
                    case "login":
                        if (commands.length != 3) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(meetingManager.userLogin(commands[1], commands[2]));
                        break;
                    case "logout":
                        System.out.println(meetingManager.userLogout());
                        break;
                    case "add":
                        if (commands.length < 5) {
                            System.out.println("invalid command");
                            break;
                        }
//                        会议的标题可能有空格，所以需要将标题的所有参数拼接起来，最后一个参数后不用加空格
                        String title = "";
                        for (int i = 4; i < commands.length; i++) {
                            title += commands[i];
                            if (i != commands.length - 1) {
                                title += " ";
                            }
                        }
                        System.out.println(meetingManager.createMeeting(commands[1], commands[2], commands[3], title));
                        break;
                    case "query":
                        if (commands.length != 3) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(meetingManager.queryMeeting(commands[1], commands[2]));
                        break;
                    case "delete":
                        if (commands.length != 2) {
                            System.out.println("invalid command");
                            break;
                        }
                        System.out.println(meetingManager.deleteMeeting(commands[1]));
                        break;
                    case "clear":
                        System.out.println(meetingManager.clearMeeting());
                        break;
                    case "listUsers":
                        System.out.println(meetingManager.getUserList());
                        break;
                    case "listMeetings":
                        System.out.println(meetingManager.getMeetingList());
                        break;
                    case "currentStatus":
                        System.out.println(meetingManager.getCurrentStatus());
                        break;
                    case "help":
                        System.out.println(meetingManager.getHelp());
                        break;
                    default:
                        System.out.println("command not found");
                        break;
                }
            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * @param :
     * @return null
     * @description 登录远程对象
     */
    private void login() {
        try {
//            1.通过RMI注册服务器获取远程对象的存根
            meetingManager = (MeetingInterface) Naming.lookup("rmi://localhost:8888/MeetingManager");
//            2.调用远程对象的方法
            System.out.println(meetingManager.getHelp());
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

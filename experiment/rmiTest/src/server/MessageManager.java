package server;

import bean.Message;
import bean.User;
import rface.MessageInterface;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * @className: MessageManager
 * @description: 留言管理类，实现接口MessageInterface，用于管理留言信息
 * @author: Yang Xixuan
 **/
public class MessageManager extends UnicastRemoteObject implements MessageInterface {
    /**
     * 用户列表
     */
    private Vector<User> users;

    /**
     * 留言板
     */
    private Vector<Message> messagesBoard;

    /**
     * user数据存放路径
     */
    public static String userDataPath = "src/data/user.txt";

    /**
     * data数据存放路径
     */
    public static String messageDataPath = "src/data/message.txt";

    public MessageManager() throws RemoteException {
        super();
//        从.txt文件中读取用户列表和留言板信息
        loadUserData();
        loadMessageData();
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 初始化，从.txt文件中读取留言板信息
     */
    private void loadMessageData() {
        messagesBoard = new Vector<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(messageDataPath));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                String sender = data[0];
                String receiver = data[1];
                String content = data[2];
                Message message = new Message(sender, receiver, content);
                messagesBoard.add(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 初始化，从.txt文件中读取用户信息
     */
    private void loadUserData() {
        users = new Vector<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(userDataPath));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(" ");
                String userName = data[0];
                String password = data[1];
                User user = new User(userName, password);
                users.add(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param name:
     * @return boolean
     * @author yangxixuan
     * @description 检查用户名是否存在
     */
    private boolean checkIfExist(String name) {
        for (User u : users) {
            if (u.getUsername().equals(name)) return true;
        }
        return false;
    }

    /**
     * @param name:
     * @param pwd:
     * @return boolean
     * @author yangxixuan
     * @description 检查用户和密码是否对应
     */
    private boolean checkUserAndPWD(String name, String pwd) {
        for (User u : users) {
            if (u.getUsername().equals(name) && u.getPassword().equals(pwd)) return true;
        }
        return false;
    }

    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 注册用户
     */
    @Override
    public String register(String name, String pwd) throws Exception {
//        情况1：用户名已存在
        if (checkIfExist(name)) return "REGISTER FAIL : user name already exists, please change an account name.";
//        情况2：用户名不存在，注册成功
        users.add(new User(name, pwd));
        return "REGISTER SUCCESS";
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 显示所有注册用户
     */
    @Override
    public String showAllUsers() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (User u : users) {
            sb.append(u.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 打印用户的所有留言
     */
    @Override
    public String checkMessages(String name, String pwd) throws Exception {
//        情况1：用户名密码不正确
        if (!checkUserAndPWD(name, pwd)) return "CHECK FAIL : user name or password is incorrect.";
        int flag = 0; //标记用户是否有留言
        StringBuilder sb = new StringBuilder();
        for (Message m : messagesBoard) {
            if (m.getReceiver().equals(name)) {
                sb.append(m.toString()).append("\n");
                flag = 1;
            }
        }
//        情况2：用户没有留言
        if (flag == 0) return "CHECK FAIL : you have no messages.";
//        情况3：用户有留言，打印留言
        return sb.toString();
    }

    /**
     * @param name:
     * @param pwd:
     * @param othername:
     * @param message:
     * @return String
     * @author yangxixuan
     * @description 给其他用户留言
     */
    @Override
    public String sendMessage(String name, String pwd, String othername, String message) throws Exception {
//        情况1：用户名密码不正确
        if (!checkUserAndPWD(name, pwd)) return "SEND FAIL : user name or password is incorrect.";
//        情况2：用户名密码正确，但是othername不存在
        if (!checkIfExist(othername)) return "SEND FAIL : the user you want to send message to does not exist.";
//        情况3：留言为空,发送失败
        if (message.equals("") || message == null) return "SEND FAIL : message cannot be empty.";
//        情况4：用户名密码正确，othername存在，发送留言
        messagesBoard.add(new Message(name, othername, message));
        return "SEND SUCCESS";
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 帮助信息
     */
    @Override
    public String help() throws Exception {
        return "HELP : \n" +
                "1. register : register a new account.\n" +
                "usage : register [username] [password]\n" +
                "2. showusers : show all registered users.\n" +
                "usage : showusers\n" +
                "3. checkmessages : check all messages you have received.\n" +
                "usage : checkmessages [username] [password]\n" +
                "4. leavemessage : send a message to another user.\n" +
                "usage : leavemessage [username] [password] [othername] [message]\n" +
                "5. help : show all commands.\n" +
                "usage : help\n" +
                "6. exit : exit the system.\n" +
                "usage : exit";
    }

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 退出系统，保存数据
     */
    @Override
    public String exit() throws Exception {
//        user和message数据存入文件
        saveUserData();
        saveMessageData();
        return "EXIT : bye bye!";
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 保存留言数据，保存到.txt文件中
     */
    private void saveMessageData() {
        try {
            File file = new File(messageDataPath);
            if (!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Message m : messagesBoard) {
//                按照空格分开
                bw.write(m.getSender() + " " + m.getReceiver() + " " + m.getContent());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param :
     * @return void
     * @author yangxixuan
     * @description 保存用户数据，保存到.txt文件中
     */
    private void saveUserData() {
        try {
            File file = new File(userDataPath);
            if (!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (User u : users) {
//                按照空格分开
                bw.write(u.getUsername() + " " + u.getPassword());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

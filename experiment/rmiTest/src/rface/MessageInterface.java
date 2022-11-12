package rface;

import java.rmi.Remote;

/**
 * @className: MessageInterface
 * @description: 留言接口，定义了留言系统的所有功能
 * @author: Yang Xixuan
 **/
public interface MessageInterface extends Remote {
    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 用户注册，格式为register [username] [password]
     */
    public String register(String name, String pwd) throws Exception;

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 显示所有用户，格式为showusers
     */
    public String showAllUsers() throws Exception;

    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 打印用户的所有留言，格式为checkmessages [username] [password]
     */
    public String checkMessages(String name, String pwd) throws Exception;

    /**
     * @param name:
     * @param pwd:
     * @param othername:
     * @param message:
     * @return String
     * @author yangxixuan
     * @description 给其他用户留言，格式为sendmessage [username] [password] [otherusername] [message]
     */
    public String sendMessage(String name, String pwd, String othername, String message) throws Exception;

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 帮助信息，格式为help
     */
    public String help() throws Exception;

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 退出系统，格式为exit
     */
    public String exit() throws Exception;
}

package rface;

import java.rmi.Remote;

/**
 * @className: MeetingInterface
 * @description: 会议接口，定义了会议管理系统的所有功能
 * @author: Yang Xixuan
 **/
public interface MeetingInterface extends Remote {
    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 用户注册，格式为register [username] [password]
     */
    public String userRegister(String name, String pwd) throws Exception;

    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 用户登录，格式为login [username] [password]
     */
    public String userLogin(String name, String pwd) throws Exception;

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 用户登出，格式为logout
     */
    public String userLogout() throws Exception;

    /**
     * @param participant:
     * @param start:
     * @param end:
     * @param title:
     * @return String
     * @author yangxixuan
     * @description 添加会议，格式为add [otherusername] [start] [end] [title]
     */
    public String createMeeting(String participant, String start, String end, String title) throws Exception;

    /**
     * @param start:
     * @param end:
     * @return String
     * @author yangxixuan
     * @description 查询会议，格式为query [start] [end]
     */
    public String queryMeeting(String start, String end) throws Exception;

    /**
     * @param uuid:
     * @return String
     * @author yangxixuan
     * @description 删除会议，格式为delete [meetingid]
     */
    public String deleteMeeting(String uuid) throws Exception;

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 清除会议，格式为clear
     */
    public String clearMeeting() throws Exception;

    /**
     * @param :
     * @return String
     * @description 获取用户列表，格式为listUsers
     */
    public String getUserList() throws Exception;

    /**
     * @param :
     * @return String
     * @description 获取会议列表，格式为listMeetings
     */
    public String getMeetingList() throws Exception;

    /**
     * @param :
     * @return String
     * @description 获取当前用户状态，格式为currentStatus
     */
    public String getCurrentStatus() throws Exception;

    /**
     * @param :
     * @return String
     * @description 获取帮助信息，格式为help
     */
    public String getHelp() throws Exception;
}

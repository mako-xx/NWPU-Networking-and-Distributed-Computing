package server;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface TodoInterface {
    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 用户注册
     */
    @WebMethod
    public String userRegister(String name, String pwd) throws Exception;

    /**
     * @param name:
     * @param pwd:
     * @return String
     * @author yangxixuan
     * @description 用户登录
     */
    @WebMethod
    public String userLogin(String name, String pwd) throws Exception;


    /**
     * @param start:
     * @param end:
     * @param description:
     * @return String
     * @author yangxixuan
     * @description 添加待办事项
     */
    @WebMethod
    public String addTodoItem(String start, String end, String description) throws Exception;

    /**
     * @param start:
     * @param end:
     * @return String
     * @author yangxixuan
     * @description 查询待办事项
     */
    @WebMethod
    public String queryTodoItem(String start, String end) throws Exception;

    /**
     * @param uuid:
     * @return String
     * @author yangxixuan
     * @description 删除待办事项
     */
    @WebMethod
    public String deleteTodoItem(String uuid) throws Exception;

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 清空待办事项
     */
    @WebMethod
    public String clearTodoItem() throws Exception;

    /**
     * @param :
     * @return String
     * @author yangxixuan
     * @description 用户登出
     */
    @WebMethod
    public String userLogout() throws Exception;

    /*
     * @param null:
      * @return null
     * @author yangxixuan
     * @description 帮助信息
     */
    @WebMethod
    public String help() throws Exception;

}


package server;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the server package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Exception_QNAME = new QName("http://server/", "Exception");
    private final static QName _AddTodoItemResponse_QNAME = new QName("http://server/", "addTodoItemResponse");
    private final static QName _QueryTodoItem_QNAME = new QName("http://server/", "queryTodoItem");
    private final static QName _DeleteTodoItem_QNAME = new QName("http://server/", "deleteTodoItem");
    private final static QName _UserRegisterResponse_QNAME = new QName("http://server/", "userRegisterResponse");
    private final static QName _HelpResponse_QNAME = new QName("http://server/", "helpResponse");
    private final static QName _ClearTodoItemResponse_QNAME = new QName("http://server/", "clearTodoItemResponse");
    private final static QName _QueryTodoItemResponse_QNAME = new QName("http://server/", "queryTodoItemResponse");
    private final static QName _UserLogout_QNAME = new QName("http://server/", "userLogout");
    private final static QName _UserLogoutResponse_QNAME = new QName("http://server/", "userLogoutResponse");
    private final static QName _DeleteTodoItemResponse_QNAME = new QName("http://server/", "deleteTodoItemResponse");
    private final static QName _UserRegister_QNAME = new QName("http://server/", "userRegister");
    private final static QName _ClearTodoItem_QNAME = new QName("http://server/", "clearTodoItem");
    private final static QName _UserLoginResponse_QNAME = new QName("http://server/", "userLoginResponse");
    private final static QName _AddTodoItem_QNAME = new QName("http://server/", "addTodoItem");
    private final static QName _UserLogin_QNAME = new QName("http://server/", "userLogin");
    private final static QName _Help_QNAME = new QName("http://server/", "help");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: server
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HelpResponse }
     * 
     */
    public HelpResponse createHelpResponse() {
        return new HelpResponse();
    }

    /**
     * Create an instance of {@link DeleteTodoItem }
     * 
     */
    public DeleteTodoItem createDeleteTodoItem() {
        return new DeleteTodoItem();
    }

    /**
     * Create an instance of {@link UserRegisterResponse }
     * 
     */
    public UserRegisterResponse createUserRegisterResponse() {
        return new UserRegisterResponse();
    }

    /**
     * Create an instance of {@link QueryTodoItem }
     * 
     */
    public QueryTodoItem createQueryTodoItem() {
        return new QueryTodoItem();
    }

    /**
     * Create an instance of {@link AddTodoItemResponse }
     * 
     */
    public AddTodoItemResponse createAddTodoItemResponse() {
        return new AddTodoItemResponse();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link UserLogin }
     * 
     */
    public UserLogin createUserLogin() {
        return new UserLogin();
    }

    /**
     * Create an instance of {@link Help }
     * 
     */
    public Help createHelp() {
        return new Help();
    }

    /**
     * Create an instance of {@link AddTodoItem }
     * 
     */
    public AddTodoItem createAddTodoItem() {
        return new AddTodoItem();
    }

    /**
     * Create an instance of {@link DeleteTodoItemResponse }
     * 
     */
    public DeleteTodoItemResponse createDeleteTodoItemResponse() {
        return new DeleteTodoItemResponse();
    }

    /**
     * Create an instance of {@link UserRegister }
     * 
     */
    public UserRegister createUserRegister() {
        return new UserRegister();
    }

    /**
     * Create an instance of {@link ClearTodoItem }
     * 
     */
    public ClearTodoItem createClearTodoItem() {
        return new ClearTodoItem();
    }

    /**
     * Create an instance of {@link UserLoginResponse }
     * 
     */
    public UserLoginResponse createUserLoginResponse() {
        return new UserLoginResponse();
    }

    /**
     * Create an instance of {@link UserLogoutResponse }
     * 
     */
    public UserLogoutResponse createUserLogoutResponse() {
        return new UserLogoutResponse();
    }

    /**
     * Create an instance of {@link ClearTodoItemResponse }
     * 
     */
    public ClearTodoItemResponse createClearTodoItemResponse() {
        return new ClearTodoItemResponse();
    }

    /**
     * Create an instance of {@link QueryTodoItemResponse }
     * 
     */
    public QueryTodoItemResponse createQueryTodoItemResponse() {
        return new QueryTodoItemResponse();
    }

    /**
     * Create an instance of {@link UserLogout }
     * 
     */
    public UserLogout createUserLogout() {
        return new UserLogout();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTodoItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "addTodoItemResponse")
    public JAXBElement<AddTodoItemResponse> createAddTodoItemResponse(AddTodoItemResponse value) {
        return new JAXBElement<AddTodoItemResponse>(_AddTodoItemResponse_QNAME, AddTodoItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryTodoItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "queryTodoItem")
    public JAXBElement<QueryTodoItem> createQueryTodoItem(QueryTodoItem value) {
        return new JAXBElement<QueryTodoItem>(_QueryTodoItem_QNAME, QueryTodoItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTodoItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "deleteTodoItem")
    public JAXBElement<DeleteTodoItem> createDeleteTodoItem(DeleteTodoItem value) {
        return new JAXBElement<DeleteTodoItem>(_DeleteTodoItem_QNAME, DeleteTodoItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserRegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "userRegisterResponse")
    public JAXBElement<UserRegisterResponse> createUserRegisterResponse(UserRegisterResponse value) {
        return new JAXBElement<UserRegisterResponse>(_UserRegisterResponse_QNAME, UserRegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "helpResponse")
    public JAXBElement<HelpResponse> createHelpResponse(HelpResponse value) {
        return new JAXBElement<HelpResponse>(_HelpResponse_QNAME, HelpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearTodoItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "clearTodoItemResponse")
    public JAXBElement<ClearTodoItemResponse> createClearTodoItemResponse(ClearTodoItemResponse value) {
        return new JAXBElement<ClearTodoItemResponse>(_ClearTodoItemResponse_QNAME, ClearTodoItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryTodoItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "queryTodoItemResponse")
    public JAXBElement<QueryTodoItemResponse> createQueryTodoItemResponse(QueryTodoItemResponse value) {
        return new JAXBElement<QueryTodoItemResponse>(_QueryTodoItemResponse_QNAME, QueryTodoItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLogout }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "userLogout")
    public JAXBElement<UserLogout> createUserLogout(UserLogout value) {
        return new JAXBElement<UserLogout>(_UserLogout_QNAME, UserLogout.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLogoutResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "userLogoutResponse")
    public JAXBElement<UserLogoutResponse> createUserLogoutResponse(UserLogoutResponse value) {
        return new JAXBElement<UserLogoutResponse>(_UserLogoutResponse_QNAME, UserLogoutResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteTodoItemResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "deleteTodoItemResponse")
    public JAXBElement<DeleteTodoItemResponse> createDeleteTodoItemResponse(DeleteTodoItemResponse value) {
        return new JAXBElement<DeleteTodoItemResponse>(_DeleteTodoItemResponse_QNAME, DeleteTodoItemResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserRegister }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "userRegister")
    public JAXBElement<UserRegister> createUserRegister(UserRegister value) {
        return new JAXBElement<UserRegister>(_UserRegister_QNAME, UserRegister.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearTodoItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "clearTodoItem")
    public JAXBElement<ClearTodoItem> createClearTodoItem(ClearTodoItem value) {
        return new JAXBElement<ClearTodoItem>(_ClearTodoItem_QNAME, ClearTodoItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "userLoginResponse")
    public JAXBElement<UserLoginResponse> createUserLoginResponse(UserLoginResponse value) {
        return new JAXBElement<UserLoginResponse>(_UserLoginResponse_QNAME, UserLoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddTodoItem }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "addTodoItem")
    public JAXBElement<AddTodoItem> createAddTodoItem(AddTodoItem value) {
        return new JAXBElement<AddTodoItem>(_AddTodoItem_QNAME, AddTodoItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserLogin }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "userLogin")
    public JAXBElement<UserLogin> createUserLogin(UserLogin value) {
        return new JAXBElement<UserLogin>(_UserLogin_QNAME, UserLogin.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Help }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server/", name = "help")
    public JAXBElement<Help> createHelp(Help value) {
        return new JAXBElement<Help>(_Help_QNAME, Help.class, null, value);
    }

}

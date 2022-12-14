
package server;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "TodoServiceService", targetNamespace = "http://server/", wsdlLocation = "http://localhost:8080/webservice/TodoService?wsdl")
public class TodoServiceService
    extends Service
{

    private final static URL TODOSERVICESERVICE_WSDL_LOCATION;
    private final static WebServiceException TODOSERVICESERVICE_EXCEPTION;
    private final static QName TODOSERVICESERVICE_QNAME = new QName("http://server/", "TodoServiceService");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://localhost:8080/webservice/TodoService?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        TODOSERVICESERVICE_WSDL_LOCATION = url;
        TODOSERVICESERVICE_EXCEPTION = e;
    }

    public TodoServiceService() {
        super(__getWsdlLocation(), TODOSERVICESERVICE_QNAME);
    }

    public TodoServiceService(WebServiceFeature... features) {
        super(__getWsdlLocation(), TODOSERVICESERVICE_QNAME, features);
    }

    public TodoServiceService(URL wsdlLocation) {
        super(wsdlLocation, TODOSERVICESERVICE_QNAME);
    }

    public TodoServiceService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, TODOSERVICESERVICE_QNAME, features);
    }

    public TodoServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public TodoServiceService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns TodoService
     */
    @WebEndpoint(name = "TodoServicePort")
    public TodoService getTodoServicePort() {
        return super.getPort(new QName("http://server/", "TodoServicePort"), TodoService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns TodoService
     */
    @WebEndpoint(name = "TodoServicePort")
    public TodoService getTodoServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://server/", "TodoServicePort"), TodoService.class, features);
    }

    private static URL __getWsdlLocation() {
        if (TODOSERVICESERVICE_EXCEPTION!= null) {
            throw TODOSERVICESERVICE_EXCEPTION;
        }
        return TODOSERVICESERVICE_WSDL_LOCATION;
    }

}

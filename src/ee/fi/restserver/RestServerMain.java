package ee.fi.restserver;

import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;


public class RestServerMain {

	public static void main(String[] args) throws Exception {
		System.out.println("Start RestServer");
		
		Component component = new Component();
		component.getDefaultHost().attach(new RestServerApplication());
		component.getClients().add(Protocol.HTTP);	//must add this protocol for clients in the component to work on HTTP
		
		Server server = new Server(Protocol.HTTP,8182);
		server.setNext(component);
		server.start();
	}

}

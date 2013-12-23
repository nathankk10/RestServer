package ee.fi.restserver;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import ee.fi.restserver.applications.ClearFlowServerResource;
import ee.fi.restserver.applications.InitServerResource;
import ee.fi.restserver.applications.InsertFlowServerResource;
import ee.fi.restserver.applications.QueryFlowTableServerResource;
import ee.fi.restserver.applications.QueryLinkServerResource;
import ee.fi.restserver.applications.QuerySwitchServerResource;

/**
 * Add router that routes different API to corresponding ServerResource
 * @author ic
 *
 */
public class RestServerApplication extends Application {
	
	@Override
	public Restlet createInboundRoot(){
		System.out.println("Create Inbound Root: set up routing");
		Router router = new Router(getContext());
		//init：初始化流表
		router.attach("/init",InitServerResource.class);
		// insert a flow entry into a switch
		router.attach("/switch/flowtable/add/json",InsertFlowServerResource.class);
		// delete a flow entry
		router.attach("/switch/flowtable/clear/json",ClearFlowServerResource.class);
		// query flow tables from a switch
		router.attach("/switch/flowtable/query/json",QueryFlowTableServerResource.class);
		// query all links information
		router.attach("/topology/links/query/json",QueryLinkServerResource.class);
		// query all switches information
		router.attach("/topology/switches/query/json",QuerySwitchServerResource.class);
		return router;
	}
	
}

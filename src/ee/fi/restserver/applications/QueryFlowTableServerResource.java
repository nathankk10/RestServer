package ee.fi.restserver.applications;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import ee.fi.restserver.Configuration;

public class QueryFlowTableServerResource extends ServerResource {

	@Get
	public Representation queryFlowTable() {
		if (Configuration.D){
			System.out.println("Query Flow Table");
		}
		
		Representation result = null;
		
		try{
			// get site_name & its ip
			String site_name = getQueryValue("site");
			String site_ip = Configuration.site_name_ip.get(site_name);
			// get switch_DPID
			String switch_DPID = getQueryValue("switch");
			System.out.println("switch dpid:\t"+switch_DPID);

			String url = String.format("http://%s:8080/wm/core/switch/%s/flow/json", site_ip, switch_DPID);
			
			if (Configuration.D){
				System.out.println("Getting from:" + url);
			}
			
			ClientResource clientResource = new ClientResource(url);
			Representation response = clientResource.get();
			String response_string = response.getText();
			
			if (Configuration.D)
				System.out.println("Floodlight response:\t" + response_string);
			
			result = new StringRepresentation(response_string,MediaType.TEXT_PLAIN);
			
		} catch (Exception e){
			e.printStackTrace();
			result = new StringRepresentation("{\"status\":\"Error\",\"Error\":\""+e.toString()+"\"}");
		}
		return result;
	}
}

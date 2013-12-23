package ee.fi.restserver.applications;

import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import ee.fi.restserver.Configuration;


public class ClearFlowServerResource extends ServerResource {
	
	@Post
	public Representation clearFlow(Representation entity) {
		if (Configuration.D)
			System.out.println("Clear Flow");
		
		Representation result = null;
		
		try{
			// get site_name from JSON request, and look up its site_ip
			JsonRepresentation jsonRepresentation = new JsonRepresentation(entity);
			JSONObject jsonObject = jsonRepresentation.getJsonObject();
			String site_name = jsonObject.getString("site");
			System.out.println("site_name:\t" + site_name);
			String site_ip = Configuration.site_name_ip.get(site_name);
			
			// get switch_DPID from JSON request
			String switch_DPID = jsonObject.getString("switch");
			
			// send CLEAR GET request to Floodlight controller
			String url = "http://" + site_ip + ":8080/wm/staticflowentrypusher/clear/" + switch_DPID + "/json";
			
			if (Configuration.D){
				System.out.println("Geting from:" + url);
			}
			
			ClientResource clientResource = new ClientResource(url);
			Representation response = clientResource.get();
			String response_string = response.getText();
			
			if (Configuration.D){
				System.out.println("Floodlight response:" + response_string);
			}
			
			result = new StringRepresentation("{\"status\":\"Accepted\"}",MediaType.TEXT_PLAIN);
			
		} catch (Exception e){
			e.printStackTrace();
			result = new StringRepresentation("{\"status\":\"Error\",\"Error\":\""+e.toString()+"\"}");
		}
		return result;
	}
}

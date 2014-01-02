package ee.fi.restserver.applications;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import ee.fi.restserver.Configuration;

public class QueryLinkServerResource extends ServerResource {
	
	@Get
	public Representation queryLink() {
		if (Configuration.D){
			System.out.println("Query Link");
		}
		
		Representation result = null;
		
		try{
			// get site_name & its ip
			String site_name = getQueryValue("site");
			String site_ip = Configuration.site_name_ip.get(site_name);
			
			String url = String.format("http://%s:8080/wm/topology/links/json", site_ip);
			
			if (Configuration.D){
				System.out.println("Getting from:" + url);
			}
			
			ClientResource clientResource = new ClientResource(url);
			Representation response = clientResource.get();
			String response_string = response.getText();
			
			JSONArray array = new JSONArray(response_string);
			JSONObject result_json = new JSONObject();
			result_json.put("topology", array);
			
			if (Configuration.D)
				System.out.println("Floodlight response:\t" + response_string);
			
			result = new StringRepresentation(result_json.toString(),MediaType.TEXT_PLAIN);
			
		} catch (Exception e){
			e.printStackTrace();
			result = new StringRepresentation("{\"status\":\"Error\",\"Error\":\""+e.toString()+"\"}");
		}
		return result;
	}
}

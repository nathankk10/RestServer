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


public class InsertFlowServerResource extends ServerResource {
	
	@Post
	public Representation insertFlow(Representation entity){
		if (Configuration.D)
			System.out.println("Insert Flow");
		
		Representation result = null;
		
		try{
			// get site_name & look up its site_ip
			JsonRepresentation jsonRepresentation = new JsonRepresentation(entity);
			JSONObject jsonObject = jsonRepresentation.getJsonObject();
			String site_name = jsonObject.getString("site");
			String site_ip = Configuration.site_name_ip.get(site_name);
			// prepare the post data for Floodlight
			jsonObject.remove("site_name");
			String post_data = jsonObject.toString();
			String url = String.format("http://%s:8080/wm/staticflowentrypusher/json", site_ip);
			
			if (Configuration.D){
				System.out.println("Posting to:" + url);
				System.out.println("With Data:" + post_data);
			}
			
			ClientResource clientResource = new ClientResource(url);
			Representation response = clientResource.post(post_data, MediaType.TEXT_PLAIN);
			String response_string = response.getText();
			
			if (Configuration.D)
				System.out.println("Floodlight response:" + response_string);
			
			result = new StringRepresentation(response_string);
		}
		catch (Exception e){
			e.printStackTrace();
			result = new StringRepresentation("{\"status\":\"Error\",\"Error\":\""+e.toString()+"\"}");
		}
		return result;
	}

}

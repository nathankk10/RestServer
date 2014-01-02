package ee.fi.restserver.applications;



import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import ee.fi.restserver.Configuration;

public class InitServerResource extends ServerResource{

	@Get
	public Representation init(){
		// Initialization, write flows to switches
		if (Configuration.D){
			System.out.println("Initialization");
		}
		
		Representation result = null;
		
		// get CLIENT_IP
		String CLIENT_IP = getQueryValue("clientip");
		
		// Site-Flow stores in Configuration
		ArrayList<String> site_ip_list = Configuration.mannual_site_ip;
		ArrayList<String> site_flow_list = Configuration.mannual_site_flow;
		// Return a JSON Array with all responses form floodlight
		
		JSONArray response_array = new JSONArray();
		
		try{
			for (int i=0;i<site_ip_list.size();i++){
				String url = String.format("http://%s:8080/wm/staticflowentrypusher/json/", site_ip_list.get(i));
				String post_data = site_flow_list.get(i);
				post_data = post_data.replace("CLIENT_IP",CLIENT_IP);
				
				if (Configuration.D){
					System.out.println("Posting to:" + url);
					System.out.println("With Data:" + post_data);
				}
				
				
				// send POST request, retrieve floodlight response
				ClientResource clientResource = new ClientResource(url);
				Representation response = clientResource.post(post_data, MediaType.TEXT_PLAIN);
				String response_string = response.getText();
				
				if (Configuration.D) 
					System.out.println("Floodlight response:" + response_string);
				
				JSONObject response_json = new JSONObject();
				response_json.put("IP",site_ip_list.get(i));
				response_json.put("flow", post_data);
				response_json.put("response", response_string);
				
				response_array.put(response_json);
			}
			JSONObject result_json = new JSONObject();
			result_json.put("result", response_array);
			
			result = new StringRepresentation(result_json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result = new StringRepresentation("{\"status\":\"Error\",\"Error\":\""+e.toString()+"\"}");
		}
		return result;
	}
}
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

public class QuerySwitchServerResource extends ServerResource {
	@Get
	public Representation querySwitch() {
		
		if (Configuration.D){
			System.out.println("Query Switch");
		}
		
		System.out.println(Configuration.site_name_ip.size());
		
		Representation result = null;
		
		try{
			// get site_name & its ip
			String site_name = getQueryValue("site");
			String site_ip = Configuration.site_name_ip.get(site_name);
			
			String url = String.format("http://%s:8080/wm/core/controller/switches/json", site_ip);
			
			if (Configuration.D){
				System.out.println("Getting from:" + url);
			}
			
			ClientResource clientResource = new ClientResource(url);
			Representation response = clientResource.get();
			String response_string = response.getText();
			
			
			
			if (Configuration.D)
				System.out.println("Floodlight response:\t" + response_string);
			
			JSONArray response_array = new JSONArray(response_string);
			JSONArray result_array = new JSONArray();
			for (int i=0;i<response_array.length();i++){
				JSONObject switch_info = response_array.getJSONObject(i);
				JSONObject result_object = new JSONObject();
				result_object.put("dpid", switch_info.get("dpid"));
				result_object.put("ports",switch_info.get("ports"));
				JSONObject desc = switch_info.getJSONObject("description");	
				result_object.put("hadware",desc.get("hardware"));
				result_object.put("manufacture", desc.get("manufacturer"));
				result_array.put(result_object);
			}
			
			JSONObject result_object = new JSONObject();
			result_object.put("switches", result_array);
			System.out.println(result_object);
			
			result = new StringRepresentation(result_object.toString(),MediaType.TEXT_PLAIN);
			
		} catch (Exception e){
			e.printStackTrace();
			result = new StringRepresentation("{\"status\":\"Error\",\"Error\":\""+e.toString()+"\"}");
		}
		return result;
	}
}

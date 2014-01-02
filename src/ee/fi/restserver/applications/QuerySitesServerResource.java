package ee.fi.restserver.applications;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import ee.fi.restserver.Configuration;

public class QuerySitesServerResource extends ServerResource {

	@Get
	public Representation querySites() throws JSONException {
		if (Configuration.D){
			System.out.println("Query Sites");
		}
		
		Representation result = null;
		
		JSONArray sitesArray = new JSONArray();
		
		for (String sitename:Configuration.site_name_ip.keySet())
		{
			JSONObject site = new JSONObject();
			site.put("sitename", sitename);
			site.put("ip", Configuration.site_name_ip.get(sitename));
			sitesArray.put(site);
		}
		HashMap<String, JSONArray> result_map = new HashMap<String,JSONArray>();
		result_map.put("sites",sitesArray);
		JSONObject result_json = new JSONObject(result_map);
		
		result = new StringRepresentation(result_json.toString());
		
		return result;
	}
}

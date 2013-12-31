package ee.fi.restserver.applications;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import ee.fi.restserver.Configuration;

public class QuerySitesServerResource extends ServerResource {

	@Get
	public Representation querySites() {
		if (Configuration.D){
			System.out.println("Query Sites");
		}
		
		Representation result = null;
		
		try{
			JSONObject sites = new JSONObject(Configuration.site_name_ip);
			result = new StringRepresentation(sites.toString());
		} catch (Exception e){
			e.printStackTrace();
		}
		return result;
	}
}

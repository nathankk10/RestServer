package ee.fi.restserver.applications;



import java.util.ArrayList;

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
		// Site-Flow stores in Configuration
		ArrayList<String> site_ip_list = Configuration.mannual_site_ip;
		ArrayList<String> site_flow_list = Configuration.mannual_site_flow;
		// Return a JSON Array with all responses form floodlight
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		
		try{
			for (int i=0;i<site_ip_list.size();i++){
				String url = String.format("http://%s:8080/wm/staticflowentrypusher/json/", site_ip_list.get(i));
				String post_data = site_flow_list.get(i);
				
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
				
				sb.append(response_string);
				if (i != (site_ip_list.size()-1)){
					sb.append(",");
				}
			}
			sb.append("]");
			result = new StringRepresentation(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			result = new StringRepresentation("{\"status\":\"Error\",\"Error\":\""+e.toString()+"\"}");
		}
		return result;
	}
}
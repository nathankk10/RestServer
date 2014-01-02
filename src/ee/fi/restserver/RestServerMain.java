package ee.fi.restserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.data.Protocol;


public class RestServerMain {

	public static void main(String[] args) throws Exception {
		System.out.println("Start RestServer");
		System.out.println(System.getProperty("user.dir"));
		
//		if (args.length!=2){
//			System.out.println("Arg Error: java -jar restAPI.jar SITE.config FLOWS.config");
//			return;
//		}
//		
//		String site_filepath = args[0];
//		String flow_filepath = args[1];
		String site_filepath = "site.txt";
		String flow_filepath = "flow.txt";
		
		File site_file = new File(site_filepath);
		FileReader site_file_reader = new FileReader(site_file);
		BufferedReader site_reader = new BufferedReader(site_file_reader);
		String line;
		Configuration.site_name_ip = new HashMap<String,String>();
		while ((line=site_reader.readLine())!=null){
			String[] pairs = line.split(" ");
			Configuration.site_name_ip.put(pairs[0], pairs[1]);
		}
		site_reader.close();
		site_file_reader.close();
		
		File flow_file = new File(flow_filepath);
		FileReader flow_file_reader = new FileReader(flow_file);
		BufferedReader flow_reader = new BufferedReader(flow_file_reader);
		Configuration.mannual_site_ip = new ArrayList<String>();
		Configuration.mannual_site_flow = new ArrayList<String>();
		while((line=flow_reader.readLine())!=null){
			Configuration.mannual_site_ip.add(line);
			line = flow_reader.readLine();
			Configuration.mannual_site_flow.add(line);
		}

		
		Component component = new Component();
		component.getDefaultHost().attach(new RestServerApplication());
		component.getClients().add(Protocol.HTTP);	//must add this protocol for clients in the component to work on HTTP
		
		Server server = new Server(Protocol.HTTP,8182);
		server.setNext(component);
		server.start();
	}

}

package ee.fi.restserver;

import java.util.ArrayList;
import java.util.HashMap;

public class Configuration {
	
	static public HashMap<String, String> site_name_ip;
	
	static public ArrayList<String> mannual_site_ip;
	static public ArrayList<String> mannual_site_flow; 
	
	static public boolean D = true;
	
	static
	{
		if (site_name_ip==null){
			site_name_ip = new HashMap<String,String>();
			site_name_ip.put("Tsinghua", "166.111.65.199");
			site_name_ip.put("Hitachi", "0.0.0.0");
		}
		if ((mannual_site_flow==null) || (mannual_site_ip==null)){
			mannual_site_flow = new ArrayList<String>();
			mannual_site_ip = new ArrayList<String>();
			//增加流表请求（JSON字符串）
		}
	}
	
}

package n3phele.init;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.representation.Form;

import n3phele.service.client.N3pheleClient;
import n3phele.util.URIUtil;

public class Console {
	
	/**
	 * 
	 * Execution :
	 * java -jar executable init (rootpassword) [(serveraddress)]
	 * 
	 */
	public static void main(String[] args) {
	
		N3pheleClient client = new N3pheleClient();
		
		if( args.length < 2 )
		{
			System.out.println("The correct format for execution is \"init (rootpassword) [serverAddress]\"");
			System.out.println("serverAddress is optional, default is http://localhost:8888/resources");
			return;
		}
		
		String command = args[0];
				
		String rootPassword = args[1];
				
		String serverAddress = (args.length > 2) ? args[2] : "http://localhost:8888/resources";
		
		if(command.equals("init"))
		{			
			client.setServerAddress(serverAddress);
			
			addEC2Cloud(client, rootPassword);
			client.setBasicCredentialsFilter("root", rootPassword);
			initEC2CloudDefaults(client);
			addEC2CloudCosts(client);
			
			addHPCloud(client, rootPassword);
			initHPCloudDefaults(client);
			addCloudCostsHP(client);			
			
			Map map = client.searchCloudByName("HPZone1");
			String id = URIUtil.getIdFromUri((String)map.get("uri"));
			addTestUser(client, id);
		}
	}
	
	public static void addTestUser(N3pheleClient client, String cloudId)
	{
		client.addUser("test-user@gmail.com", "Test", "Last", "testit!", "AccountName", "account description", cloudId, "accountId", "accountSecret");
	}
	
	public static void addEC2Cloud(N3pheleClient client, String rootPassword)
	{
		client.setBasicCredentialsFilter("root", rootPassword);
		
		String myName = "EC2";
		String description = "Amazon Elastic Compute Service";
		String location = "https://ec2.amazonaws.com";
		String factory = "https://ec2factory-dev.appspot.com/resources/virtualServer";
		String factoryId = "foo";
		String mySecret = "secret";
		
		client.addCloud(myName, description, location, factory, factoryId, mySecret, "instanceType");		
	}
	
	public static void addHPCloud(N3pheleClient client, String rootPassword)
	{
		client.setBasicCredentialsFilter("root", rootPassword);
		
		String myName = "HPZone1";
		String description = "HP Cloud";
		String location = "https://az-1.region-a.geo-1.compute.hpcloudsvc.com/v1.1/12365734013392";
		String factory =  "https://nova-factory.appspot.com/resources/virtualServer";
		String factoryId = "user";
		String mySecret = "password";		
		String costDriverName = "flavorRef";
		
		client.addCloud(myName, description, location, factory, factoryId, mySecret, costDriverName);		
	}
	
	static String EC2Defaults[][] = {
		{ "instanceType", "specifies virtual machine size. Valid Values: t1.micro | m1.small | m1.large | m1.xlarge | m2.xlarge | m2.2xlarge | m2.4xlarge | c1.medium | c1.xlarge", "String", "", "t1.micro" },
		{"imageId", "Unique ID of a machine image, returned by a call to RegisterImage", "String", "", "ami-54cf5c3d"},
		{"securityGroups", "Name of the security group which controls the open TCP/IP ports for the VM.", "String", "", "n3phele-default"},
		{"userData", "Base64-encoded MIME user data made available to the instance(s). May be used to pass startup commands.", "", "", "#!/bin/bash\necho n3phele agent injection... \nset -x\n wget -q -O - https://n3phele-agent.s3.amazonaws.com/n3ph-install-tgz-basic | su - -c '/bin/bash -s ec2-user ~/agent ~/sandbox https://region-a.geo-1.objects.hpcloudsvc.com:443/v1/AUTH_dc700102-734c-4a97-afc8-50530e87a171/n3phele-agent/n3phele-agent.tgz' ec2-user\n"}		
	};
		
	public static void initEC2CloudDefaults(N3pheleClient client)  {
		for(String[] s : EC2Defaults) {	
			ClientResponse result = client.setCloudParameter("EC2", s[0], s[4], s[2]);
		}
	}
	
	static String HPCloudDefaults[][] = {
		 {"flavorRef", "Specifies the virtual machine size. Valid Values: 100 (standard.xsmall), 101 (standard.small), 102 (standard.medium), 103 (standard.large), 104 (standard.xlarge), 105 (standard.2xlarge)", "String","", "100"},
		 {"imageRef", "Unique ID of a machine image, returned by a call to RegisterImage", "String", "", "75845"},
		 {"locationId", "Unique ID of hpcloud zone. Valid Values: az-1.region-a.geo-1 | az-2.region-a.geo-1 | az-3.region-a.geo-1", "String", null, "az-1.region-a.geo-1"},
		 {"security_groups", "Name of the security group which controls the open TCP/IP ports for the VM.", "String", "", "n3phele-default"},
		 {"user_data", "Base64-encoded MIME user data made available to the instance(s). May be used to pass startup commands.", "String", "", "#!/bin/bash\necho n3phele agent injection... \nset -x\n apt-get update;  apt-get install -y openjdk-6-jre-headless \n wget -q -O - https://n3phele-agent.s3.amazonaws.com/n3ph-install-tgz-basic | su - -c '/bin/bash -s ubuntu ~/agent ~/sandbox https://region-a.geo-1.objects.hpcloudsvc.com:443/v1/AUTH_dc700102-734c-4a97-afc8-50530e87a171/n3phele-agent/n3phele-agent.tgz' ubuntu\n"},	
	};
	 
	public static void initHPCloudDefaults(N3pheleClient client)  {
		for(String[] s : HPCloudDefaults) {	
			ClientResponse result = client.setCloudParameter("HPZone1", s[0], s[4], s[2]);
		}
	}
	
	public static void addEC2CloudCosts(N3pheleClient client){
 		Map<String,Double> map = readFile("EC2_us_west_2.txt");
		
		for (Entry<String,Double> entryPrice : map.entrySet())
		{									
			ClientResponse result = client.addCloudCost("EC2", entryPrice.getKey(), entryPrice.getValue().toString());
		}
	}
	

	public static void addCloudCostsHP(N3pheleClient client){
 		Map<String,Double> map = readFile("HP.txt");
		
		for (Entry<String,Double> entryPrice : map.entrySet())
		{						
			ClientResponse result = client.addCloudCost("HPZone1", entryPrice.getKey(), entryPrice.getValue().toString());
		}
	}		
	
	public static Map<String,Double> readFile(String fileName){
		Map<String, Double> map = new HashMap<String,Double>();
		try{
			  FileInputStream fstream = new FileInputStream(fileName);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  
			  String strLine;
			  while ((strLine = br.readLine()) != null)   {	 
				 
				  String[] values = strLine.split(" ");
				  System.out.println (values[0]+" "+values[1]);
				  map.put(values[0],Double.parseDouble(values[1]));
			  }
			  in.close();
		}catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
		}
		
		return map;
	}

}

package n3phele.service.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.Assert;

import n3phele.util.JsonUtil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.representation.Form;

public class N3pheleClient {
	
	private String serverAddress = "http://localhost:8888/resources";
	private Client client;
	private WebResource webResource;
	private HTTPBasicAuthFilter authFilter;
	
	public N3pheleClient()
	{
		client = Client.create();
	}
	
	public N3pheleClient(Client client)
	{
		this.client = client;
	}
	
	public void setServerAddress(String serverAddress)
	{
		this.serverAddress = serverAddress;
	}
	
	public void setBasicCredentialsFilter(String username, String password)
	{
		authFilter = new HTTPBasicAuthFilter(username, password);
		client.addFilter(authFilter);		
	}
	
	public ClientResponse addUser(String email, String firstName, String lastName, String secret, String accountName, String description, String cloud, String accountId, String accountSecret) {

		Form form = new Form();
		form.add("email", email);
		form.add("firstName", firstName);
		form.add("lastName", lastName);
		form.add("secret", secret);    
		form.add("accountName", accountName);
		form.add("description", description);   	
		form.add("cloud", cloud);   	   	
		form.add("accountId", accountId);
		form.add("accountSecret", accountSecret);
		
		client.removeAllFilters();
		client.addFilter(new HTTPBasicAuthFilter("signup", "newuser"));		
		webResource = client.resource(UriBuilder.fromUri(serverAddress).path("/user").build());
		ClientResponse result = webResource.post(ClientResponse.class, form);
		
		client.removeAllFilters();
		client.addFilter(authFilter);

		return result;    	
	}

	public ClientResponse addCloud(String name, String description, String location, String factory, String factoryId, String secret, String costDriverName) {

		webResource = client.resource(UriBuilder.fromUri(serverAddress).path("/cloud").build());
		Form form = new Form();
		
		form.add("name", name);
		form.add("description", description);
		form.add("location", location);
		form.add("factory", factory);
		form.add("factoryId", factoryId);
		form.add("secret", secret);
		form.add("isPublic", true);
		form.add("costDriverName", costDriverName);

		ClientResponse result = webResource.post(ClientResponse.class, form);
		return result;
	}
	
	public ClientResponse setCloudParameter(String cloudName, String parameterName, String defaultValue, String type)
	{
		webResource = client.resource(UriBuilder.fromUri(serverAddress).path("/cloud").build());
		Map jsonData = searchCloudByName(cloudName);

		Form form = new Form();
		form.add("key", parameterName);
		form.add("defaultValue", defaultValue);
		form.add("type", type);
		
		ClientResponse response = null;
		try {
			webResource.uri(new URI((String) jsonData.get("uri"))).path("inputParameter").post(ClientResponse.class, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return response;
	}

	public ClientResponse deleteCloud(String name)
	{
		webResource = client.resource(UriBuilder.fromUri(serverAddress).path("/cloud").build());
		Map jsonData = searchCloudByName(name);
		String uri=(String) jsonData.get("uri");

		ClientResponse response = null;
		try {
			response = webResource.uri(new URI(uri)).delete(ClientResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Map searchCloudByName(String cloudName) {
		webResource = client.resource(UriBuilder.fromUri(serverAddress).path("/cloud").build());
		String cloudJsonString = webResource.path("byName").queryParam("id",cloudName).accept(MediaType.APPLICATION_JSON_TYPE).get(String.class);

		Map map = null;
		try {
			map = JsonUtil.parseToMap(cloudJsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}

	public ClientResponse addCloudCost(String cloudName, String key, String value){
		webResource = client.resource(UriBuilder.fromUri(serverAddress).path("/cloud").build());
		Map jsonData = searchCloudByName(cloudName);

		Form form = new Form();
		form.add("key", key);
		form.add("value", value);

		ClientResponse result = null;
		try {
			result = webResource.uri(new URI((String) jsonData.get("uri"))).path("costMap").post(ClientResponse.class, form);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}

package n3phele.test.service.client;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.net.URISyntaxException;

import n3phele.service.client.N3pheleClient;

import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

public class N3pheleClientTest {
	
	@Test
	public void addUser_WhenCalledLocalhostAddress_SendAPostToUserResourceAtLocalhost() throws URISyntaxException {

		Client jerseyClientMock = mock(Client.class);
		WebResource resourceMock = mock(WebResource.class);
		ClientResponse responseMock = mock(ClientResponse.class);
		when(resourceMock.post(any(Class.class), any(Object.class))).thenReturn(responseMock);
		when(jerseyClientMock.resource(any(URI.class))).thenReturn(resourceMock);

		N3pheleClient client = new N3pheleClient(jerseyClientMock);
		client.setServerAddress("http://localhost/resources");
				
		client.addUser("aemail", "jerry", "cook", "1234", "hpaccount", "such cloud", "HP", "ABC123", "BIGSECRET");

		URI uri = new URI("http://localhost/resources/user");
		verify(jerseyClientMock).resource(uri);
		verify(resourceMock).post(any(Class.class), any(Object.class) );
	}
	
	@Test
	public void addUser_WhenCalledForRemoteServerAddress_SendAPostToUserResourceAtRemoteAddress() throws URISyntaxException {

		Client jerseyClientMock = mock(Client.class);
		WebResource resourceMock = mock(WebResource.class);
		ClientResponse responseMock = mock(ClientResponse.class);
		when(resourceMock.post(any(Class.class), any(Object.class))).thenReturn(responseMock);
		when(jerseyClientMock.resource(any(URI.class))).thenReturn(resourceMock);

		N3pheleClient client = new N3pheleClient(jerseyClientMock);
		client.setServerAddress("http://n3phele-dev.appspost.com:8888/resources");
				
		client.addUser("aemail", "jerry", "cook", "1234", "hpaccount", "such cloud", "HP", "ABC123", "BIGSECRET");

		URI uri = new URI("http://n3phele-dev.appspost.com:8888/resources/user");
		verify(jerseyClientMock).resource(uri);
		verify(resourceMock).post(any(Class.class), any(Object.class) );
	}
	
	@Test
	public void addUser_WhenCalled_SendUserParametersInsideAForm() throws URISyntaxException {

		Client jerseyClientMock = mock(Client.class);
		WebResource resourceMock = mock(WebResource.class);
		ClientResponse responseMock = mock(ClientResponse.class);
		when(resourceMock.post(any(Class.class), any(Object.class))).thenReturn(responseMock);
		when(jerseyClientMock.resource(any(URI.class))).thenReturn(resourceMock);

		N3pheleClient client = new N3pheleClient(jerseyClientMock);
		client.setServerAddress("http://localhost/resources");
				
		client.addUser("aemail", "jerry", "cook", "1234", "hpaccount", "such cloud", "HP", "ABC123", "BIGSECRET");
		
		Form form = new Form();
		form.add("email", "aemail");
		form.add("firstName", "jerry");
		form.add("lastName", "cook");
		form.add("secret", "1234");    
		form.add("accountName", "hpaccount");
		form.add("description", "such cloud");   	
		form.add("cloud", "HP");   	   	
		form.add("accountId", "ABC123");
		form.add("accountSecret", "BIGSECRET");
		
		URI uri = new URI("http://localhost/resources/user");
		verify(jerseyClientMock).resource(uri);
		verify(resourceMock).post(any(Class.class), eq(form) );
	}
	
	@Test
	public void addUser_OnSuccessCreateResponse_ReturnStatus201() {

		Client jerseyClientMock = mock(Client.class);
		WebResource resourceMock = mock(WebResource.class);
		ClientResponse responseMock = mock(ClientResponse.class);
		when(responseMock.getStatus()).thenReturn(201);
		when(resourceMock.post(any(Class.class), any(Object.class))).thenReturn(responseMock);
		when(jerseyClientMock.resource(any(URI.class))).thenReturn(resourceMock);

		N3pheleClient client = new N3pheleClient(jerseyClientMock);
				
		ClientResponse response = client.addUser("aemail", "jerry", "cook", "1234", "hpaccount", "such cloud", "HP", "ABC123", "BIGSECRET");
		
		assertEquals(201, response.getStatus());
	}
	

	@Test
	public void addUser_OnBadRequestResponse_ReturnStatus400() {

		Client jerseyClientMock = mock(Client.class);
		WebResource resourceMock = mock(WebResource.class);
		ClientResponse responseMock = mock(ClientResponse.class);
		when(responseMock.getStatus()).thenReturn(400);
		when(resourceMock.post(any(Class.class), any(Object.class))).thenReturn(responseMock);
		when(jerseyClientMock.resource(any(URI.class))).thenReturn(resourceMock);

		N3pheleClient client = new N3pheleClient(jerseyClientMock);
				
		ClientResponse response = client.addUser("aemail", "jerry", "cook", "1234", "hpaccount", "such cloud", "HP", "ABC123", "BIGSECRET");
		
		assertEquals(400, response.getStatus());
	}
	
	

}

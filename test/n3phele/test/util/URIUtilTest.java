package n3phele.test.util;

import static org.junit.Assert.*;
import org.junit.Test;

import n3phele.util.URIUtil;

public class URIUtilTest {

	@Test
	public void getIdFromURI_URIWithOneDigitID() throws Exception{
		String uri = "http://localhost:8888/2";
		String id = URIUtil.getIdFromUri(uri);
		assertEquals("2", id);
	}

	@Test
	public void getIdFromURI_URIWithTwoDigitsID() throws Exception{
		String uri = "http://localhost:8888/19";
		String id = URIUtil.getIdFromUri(uri);
		assertEquals("19", id);
	}
	
	@Test
	public void getIdFromURI_URIWithEightDigitsID() throws Exception{
		String uri = "http://localhost:8888/12123445";
		String id = URIUtil.getIdFromUri(uri);
		assertEquals("12123445", id);
	}
	

	@Test
	public void getIdFromURI_URIWithEightDigitsIDAndTwoLevelsPath() throws Exception{
		String uri = "http://localhost:8888/resources/user/99123488";
		String id = URIUtil.getIdFromUri(uri);
		assertEquals("99123488", id);
	}
	
	
}

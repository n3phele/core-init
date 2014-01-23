package n3phele.util;

public class URIUtil {
	
	public static String getIdFromUri(String uri)
	{
		String id = "";
		
		String[] uriPath = uri.split("/");
		
		id = uriPath[uriPath.length-1];
		
		return id;		
	}
}

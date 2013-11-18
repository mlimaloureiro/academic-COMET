package project02;

import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.GoogleApi;
import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class GoogleRestReader
{
	// Access codes #1: per application used to get access codes #2		
	/*Client ID da conta Google Task*/
	private static final String API_APP_KEY =  "620233114883.apps.googleusercontent.com";
	/*Client secret da conta Google Task*/
	private static final String API_APP_SECRET = "RJ_lE5ofiBc_Qi4os_TpaItK";
	
	// Access codes #2: per user per application
	private static final String API_USER_TOKEN = "";
	private static final String API_USER_SECRET = "";
	
	private static String scope = "https://www.googleapis.com/auth/tasks";
	
	public static void main(String[] args) {

		init();
	}
            
            public static void init()
            {
                Scanner in = new Scanner(System.in);

		OAuthService service = new ServiceBuilder()
		.provider(GoogleApi.class)
		.apiKey(API_APP_KEY)
		.apiSecret(API_APP_SECRET)
		.scope(scope)
		.build();
                        
                        System.out.println("IM IN GOOGLE REST READER");
		try {
			if ( API_USER_TOKEN.equals("") || API_USER_SECRET.equals("") ) {
				System.out.println("Fetching the Request Token...");

				Token requestToken = service.getRequestToken();
				System.out.println("Now go and authorize Scribe here:");
				System.out.println(service.getAuthorizationUrl(requestToken));
				System.out.println("And paste the verifier here");
				Verifier verifier = new Verifier(in.nextLine());
				Token accessToken = service.getAccessToken(requestToken, verifier);
				System.out.println("Define API_USER_TOKEN: " + accessToken.getToken());
				System.out.println("Define API_USER_SECRET: " + accessToken.getSecret());

				System.exit(0);
			}			
		} catch(OAuthException e) {
			e.printStackTrace();
		}
            }

}
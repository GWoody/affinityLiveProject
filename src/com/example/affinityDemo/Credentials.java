package com.example.affinityDemo;

/**
 * Created by Gareth on 17/12/2014.
 * Class used for storing basic credientials for the user and to formulate URL's
 */
public class Credentials {

    private static String basePermissionURL;
    private static String permissionScope;
    private static String permissionResponseType;
    private static String baseRequestURL;
    private static String clientID;
    private static String username;
    private static String password;
    private static String grantType;
    private static String clientSecret;

    Credentials()
    {
        basePermissionURL = "https://demo.api.staging.affinitylive.com/oauth2/v0/authorize?";
        baseRequestURL = "https://demo.api.staging.affinitylive.com/oauth2/v0/token?";
        permissionScope = "scope=read(all)&";
        permissionResponseType = "response_type=code&";
        clientID = "client_id=0a5971f7ce@demo.staging.affinitylive.com";
        clientSecret = "client_secret=eERGXMo2CMsPtuo1jDkzmp2iPhgjcTth";
        username = "username=sample_user_1&";
        password = "password=RedWine";
        grantType = "grant_type=client_credentials&";

    }

    public String getAuthorizationURL()
    {
       String authorURL = "";

      authorURL = basePermissionURL + permissionScope +permissionResponseType + clientID ;

       return authorURL;

    }

    public String ReceiveCredientials(String code)
    {
        String requestAccessURL = "";

        requestAccessURL = baseRequestURL + "code=" + code + "&" + grantType + clientID + "&" + clientSecret + "&" + username + password;

        return requestAccessURL;
    }

}

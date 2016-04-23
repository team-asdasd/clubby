package web.helpers;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

import javax.ws.rs.client.Entity;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Mindaugas on 22/04/2016.
 */
public  class HttpClient {
    private static HttpRequestFactory _requestFactory;
    private static JsonObjectParser  _jsonObjectParser;
    private static String _baseUrl;
    static {
        HttpTransport transport = new ApacheHttpTransport();
        _requestFactory = transport.createRequestFactory();
        _jsonObjectParser = new GsonFactory().createJsonObjectParser();

        _baseUrl = System.getenv("OPENSHIFT_APP_DNS");
        if(_baseUrl == null || _baseUrl.isEmpty()){
            _baseUrl = "localhost:8080";
        }

    }

    public static<T> T sendGetRequest(String url, Class<T> entityClass){
        T entity = null;
        try {
            HttpRequest request = _requestFactory.buildGetRequest(new GenericUrl(url)).setParser(_jsonObjectParser);
            entity = request.execute().parseAs(entityClass);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entity;
    }

    public static HttpResponse postJson(String url, Object entity, String cookie){
        HttpResponse response = null;
        try {
            GenericUrl ur = new GenericUrl("http://"+_baseUrl);
            ur.setRawPath(url);

            HttpRequest request = _requestFactory.buildPostRequest(ur, new ClubbyJsonHttpContent(entity));

            if(cookie != null){
                HttpHeaders headers = request.getHeaders();
                headers.set("Cookie", cookie);
                request.setHeaders(headers);
            }

            response = request.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

}

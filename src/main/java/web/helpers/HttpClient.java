package web.helpers;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mindaugas on 22/04/2016.
 */
public class HttpClient {
    private static HttpRequestFactory _requestFactory;
    private static JsonObjectParser _jsonObjectParser;
    private static Gson _gson;
    private static String _baseUrl;
    private static Logger logger = LogManager.getLogger(HttpClient.class.getName());

    static {
        HttpTransport transport = new ApacheHttpTransport();
        _requestFactory = transport.createRequestFactory();
        _jsonObjectParser = new GsonFactory().createJsonObjectParser();
        _gson = new Gson();

        _baseUrl = System.getenv("OPENSHIFT_APP_DNS");
        if (_baseUrl == null || _baseUrl.isEmpty()) {
            _baseUrl = "localhost:8080";
        }

    }

    public static <T> T sendGetRequest(String url, Class<T> entityClass, Map<String, String> params, String cookie) {
        GenericUrl ur = new GenericUrl("http://" + _baseUrl);
        ur.setRawPath(url);
        setParams(ur, params);

        T entity = null;
        try {
            HttpRequest request = _requestFactory.buildGetRequest(ur).setParser(_jsonObjectParser);
            setCookie(request, cookie);

            HttpResponse response = request.execute();
            String json = response.parseAsString();
            entity = _gson.fromJson(json, entityClass);
        } catch (IOException e) {
            try {
                entity = _gson.fromJson(((HttpResponseException) e).getContent(), entityClass);
            } catch (Exception ex) {
                logger.error(ex);
            }

        }

        return entity;
    }

    public static <T> T postJson(String url, Object entity, String cookie, Class<T> entityClass) {
        T response = null;
        try {
            GenericUrl ur = new GenericUrl("http://" + _baseUrl);
            ur.setRawPath(url);

            HttpRequest request = _requestFactory.buildPostRequest(ur, new ClubbyJsonHttpContent(entity));
            setCookie(request, cookie);
            HttpResponse httpResp = request.execute();
            response = _gson.fromJson(httpResp.parseAsString(), entityClass);
        } catch (IOException e) {
            try {
                response = _gson.fromJson(((HttpResponseException) e).getContent(), entityClass);
            } catch (Exception ex) {
                logger.error(ex);
            }

        }

        return response;
    }

    private static void setCookie(HttpRequest request, String cookie) {
        if (cookie != null) {
            ArrayList cookieList = new ArrayList();
            cookieList.add(cookie);
            HttpHeaders headers = request.getHeaders();
            headers.setContentEncoding("utf-8");
            headers.setContentType("application/json");
            headers.set("Cookie", cookieList);
            request.setHeaders(headers);
        }
    }

    private static void setParams(GenericUrl url, Map<String, String> params) {
        if (params != null) {
            for (String name : params.keySet()) {
                url.set(name, params.get(name));
            }
        }
    }

}

package web.helpers;

import com.google.api.client.http.AbstractHttpContent;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by Mindaugas on 23/04/2016.
 */
public class ClubbyJsonHttpContent extends AbstractHttpContent {
    private final Object _data;
    private final Gson _gson;
    public ClubbyJsonHttpContent(Object data){
        super("application/json; charset=UTF-8");
        _data = data;
        _gson = new Gson();
    }

    @Override
    public void writeTo(OutputStream outputStream) throws IOException {
        String json = _gson.toJson(_data);
        outputStream.write(json.getBytes(Charset.forName("UTF-8")));
        outputStream.flush();
    }
}

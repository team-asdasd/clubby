package logging;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.gson.GsonFactory;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Mindaugas on 09/04/2016.
 */
@Plugin(name="SlackAppender", category="Core", elementType="appender", printObject=true)
public final class SlackAppenderImpl extends AbstractAppender {

    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final Lock readLock = rwLock.readLock();
    private GenericUrl url;
    private String logsUrl;

    protected SlackAppenderImpl(String name, Filter filter,
                                Layout<? extends Serializable> layout, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        logsUrl = "http://" + System.getenv("OPENSHIFT_APP_DNS")+ "/admin/logs";

        url = new GenericUrl(System.getenv("SLACK_HOOK"));

    }

    @Override
    public void append(LogEvent event) {
        readLock.lock();
        try {
            /*TODO refactor to use normal entity class*/
            /*temporary hack*/
            Map<String,String> attachment = new HashMap<String, String>();
            attachment.put("title",event.getLevel().name());
            attachment.put("title_link",logsUrl);
            attachment.put("text",event.getMessage().getFormattedMessage());
            attachment.put("color",resolveColor(event));
 
            ArrayList<Map<String,String>> attachments =
                    new ArrayList<Map<String,String>>();
            attachments.add(attachment);

            Map<String, Object> json = new HashMap<String, Object>();
            json.put("attachments", attachments);
            /*end of temporary hack*/

            HttpTransport transport = new NetHttpTransport();
            HttpRequestFactory requestFactory = transport.createRequestFactory();
            HttpRequest req = requestFactory.buildPostRequest(url,new JsonHttpContent(new GsonFactory(),json));
            HttpResponse response = req.execute();

        } catch (Exception ex) {
            if (!ignoreExceptions()) {
                throw new AppenderLoggingException(ex);
            }
        } finally {
            readLock.unlock();
        }
    }

    @PluginFactory
    public static SlackAppenderImpl createAppender(
            @PluginAttribute("name") String name,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("otherAttribute") String otherAttribute) {
        if (name == null) {
            LOGGER.error("No name provided for SlackAppender");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        return new SlackAppenderImpl(name, filter, layout, true);
    }

    private String resolveColor(LogEvent event) {
        String color;
        switch (event.getLevel().name())
        {
            case "ERROR" :
                color = "danger";
                break;
            case "FATAL":
                color = "danger";
                break;
            case "WARN":
                color = "warning";
                break;
            default: color = "good";
        }

        return color;
    }
}
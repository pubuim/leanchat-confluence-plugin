package in.ashwanthkumar.chatwork.webhook.service;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Maps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static in.ashwanthkumar.utils.lang.StringUtils.isNotEmpty;
import static in.ashwanthkumar.utils.lang.StringUtils.startsWith;

public class ChatworkService {
    private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();

    public void push(String webHookUrl, String text, String url, String action, String space_id, String username, String imageOrIcon) throws IOException {
        Map<String, String> payload = new HashMap<String, String>();
        //GenericData payload = new GenericData();
        if (isNotEmpty(url)) {
            payload.put("url", url);
        }
        if (isNotEmpty(action)) {
            payload.put("action", action);
        }
        if (isNotEmpty(space_id)) {
            payload.put("space_id", space_id);
        }
        if (isNotEmpty(username)) {
            payload.put("username", username);
        }
        if (startsWith(imageOrIcon, "http")) {
            payload.put("icon_url", imageOrIcon);
        } else if (isNotEmpty(imageOrIcon)) {
            payload.put("icon_emoji", imageOrIcon);
        }

        payload.put("text", text);
        execute(webHookUrl, payload);
    }

    public void execute(String webHookUrl, Map<String, String> payload) throws IOException {
        //String jsonEncodedMessage = new Gson().toJson(payload);
        //HashMap<Object, Object> payloadToSend = Maps.newHashMap();
        //payloadToSend.put("payload", jsonEncodedMessage);

        requestFactory.buildPostRequest(new GenericUrl(webHookUrl), new UrlEncodedContent(payload))
                .execute();
    }

}

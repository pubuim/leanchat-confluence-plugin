package in.ashwanthkumar.chatwork.webhook;

import in.ashwanthkumar.chatwork.webhook.service.ChatworkService;
import in.ashwanthkumar.utils.collections.Lists;

import java.io.IOException;
import java.util.List;

import static in.ashwanthkumar.utils.lang.StringUtils.isEmpty;


/**
 * Chatwork provides programmatic access to Chatwork web hooks
 */
public class Chatwork {
    private String webhookUrl;
    private String user;
    private String icon;
    private String url;
    private String action;
    private String space_id;
    private ChatworkService chatworkService = new ChatworkService();

    public Chatwork(String webhookUrl) {
        if (isEmpty(webhookUrl)) {
            throw new IllegalArgumentException("Webhook url is not provided");
        }
        this.webhookUrl = webhookUrl;
    }

    /**
     * Change the display name
     *
     * @param user Display name
     */
    public Chatwork displayName(String user) {
        this.user = user;
        return this;
    }

    /**
     * Change the bot icon
     *
     * @param imageOrIcon Icon Image URL or emoji code from http://www.emoji-cheat-sheet.com/
     */
    public Chatwork icon(String imageOrIcon) {
        this.icon = imageOrIcon;
        return this;
    }

    /**
     * Change the url
     *
     * @param url Page url
     */
    public Chatwork sendToUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * Change the action type
     *
     * @param action Action type
     */
    public Chatwork sendToAction(String action) {
        this.action = action;
        return this;
    }

    /**
     * Change the Space ID
     *
     * @param space_id Space ID
     */
    public Chatwork sendToSpaceId(String space_id) {
        this.space_id = space_id;
        return this;
    }

    /**
     * Publishes messages to Chatwork Webhook
     *
     * @param message Message to send
     * @throws IOException
     */
    public void push(String message) throws IOException {
        if (message != null) {
            chatworkService.push(webhookUrl, message, url, action, space_id, user, icon);
        }
    }

}

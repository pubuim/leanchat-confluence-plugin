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
    private String channel;
    private String user;
    private String icon;
    private ChatworkService chatworkService = new ChatworkService();

    public Chatwork(String webhookUrl) {
        if (isEmpty(webhookUrl)) {
            throw new IllegalArgumentException("Webhook url is not provided");
        } else if (!webhookUrl.startsWith("https://hooks.chatwork.com/services/")) {
            throw new IllegalArgumentException("Chatwork Webhook url starts with https://hooks.chatwork.com/services/");
        }
        this.webhookUrl = webhookUrl;
    }

    /**
     * Used for tests
     */
    Chatwork(String webhookUrl, ChatworkService mockService) {
        this.webhookUrl = webhookUrl;
        chatworkService = mockService;
    }

    /**
     * Send the message to a particular channel
     *
     * @param channel Channel to send
     */
    public Chatwork sendToChannel(String channel) {
        this.channel = "#" + channel;
        return this;
    }

    /**
     * Send the message to a particular user
     *
     * @param sendToUser User to send
     */
    public Chatwork sendToUser(String sendToUser) {
        this.channel = "@" + sendToUser;
        return this;
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
     * Publishes messages to Chatwork Webhook
     *
     * @param message Message to send
     * @throws IOException
     */
    public void push(ChatworkMessage message) throws IOException {
        if (message != null) {
            chatworkService.push(webhookUrl, message, user, icon, channel);
        }
    }

    /**
     * Publish message as ChatworkAttachment
     *
     * @param attachment ChatworkAttachment to send
     * @throws IOException
     */
    public void push(ChatworkAttachment attachment) throws IOException {
        if (attachment != null) {
            chatworkService.push(webhookUrl, new ChatworkMessage(), user, icon, channel, Lists.of(attachment));
        }
    }

    /**
     * Publish message as ChatworkAttachment
     *
     * @param attachments ChatworkAttachment to send
     * @throws IOException
     */
    public void push(List<ChatworkAttachment> attachments) throws IOException {
        chatworkService.push(webhookUrl, new ChatworkMessage(), user, icon, channel, attachments);
    }
}

package in.ashwanthkumar.chatwork.webhook;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Ref - https://api.chatwork.com/docs/attachments
 */
public class ChatworkAttachment {
    @SerializedName("fallback")
    private String fallback;
    @SerializedName("color")
    private String color;
    @SerializedName("pretext")
    private String pretext;
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("author_link")
    private String authorLink;
    @SerializedName("author_icon")
    private String authorIcon;
    @SerializedName("title")
    private String title;
    @SerializedName("title_link")
    private String titleLink;
    @SerializedName("text")
    private String text;
    @SerializedName("image_url")
    private String imageUrl;
    @SerializedName("fields")
    private List<Field> fields = new ArrayList<Field>();

    public ChatworkAttachment(String text) {
        text(text);
    }

    public static class Field {
        @SerializedName("title")
        private String title;
        @SerializedName("value")
        private String value;
        @SerializedName("short")
        private boolean isShort;

        public Field(String title, String value, boolean isShort) {
            this.title = title;
            this.value = value;
            this.isShort = isShort;
        }

        public String getTitle() {
            return title;
        }

        public String getValue() {
            return value;
        }

        public boolean isShort() {
            return isShort;
        }
    }

    public ChatworkAttachment fallback(String fallbackText) {
        this.fallback = fallbackText;
        return this;
    }

    public ChatworkAttachment color(String colorInHex) {
        this.color = colorInHex;
        return this;
    }

    public ChatworkAttachment preText(String pretext) {
        this.pretext = pretext;
        return this;
    }

    public ChatworkAttachment author(String name) {
        this.authorName = name;
        return this;
    }

    public ChatworkAttachment author(String name, String link) {
        this.authorLink = link;
        return author(name);
    }

    public ChatworkAttachment author(String name, String link, String iconOrImageLink) {
        this.authorIcon = iconOrImageLink;
        return author(name, link);
    }

    public ChatworkAttachment title(String title) {
        this.title = title;
        return this;
    }

    public ChatworkAttachment title(String title, String link) {
        this.titleLink = link;
        return title(title);
    }

    public ChatworkAttachment imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ChatworkAttachment text(String text) {
        this.text = text;
        return this;
    }

    public ChatworkAttachment text(ChatworkMessage message) {
        return text(message.toString());
    }

    public ChatworkAttachment addField(Field field) {
        this.fields.add(field);
        return this;
    }

    public String getText() {
        return text;
    }
}

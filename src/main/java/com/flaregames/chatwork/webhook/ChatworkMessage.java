package in.ashwanthkumar.chatwork.webhook;


import in.ashwanthkumar.utils.lang.StringUtils;

/**
 * Wrapper to build rich text content in Chatwork
 * Ref - https://api.chatwork.com/docs/formatting
 */
public class ChatworkMessage {
    private StringBuilder textBuffer = new StringBuilder();

    public ChatworkMessage() {
    }

    public ChatworkMessage(String text) {
        text(text);
    }

    public ChatworkMessage text(String text) {
        textBuffer.append(text);
        return this;
    }

    public ChatworkMessage link(String url) {
        link(url, "");
        return this;
    }

    public ChatworkMessage link(String url, String text) {
        if (StringUtils.isNotEmpty(text)) {
            textBuffer.append("<").append(url).append("|").append(text).append(">");
        } else {
            textBuffer.append("<").append(url).append(">");
        }

        return this;
    }

    public ChatworkMessage bold(String text) {
        textBuffer.append("*").append(text).append("*");
        return this;
    }

    public ChatworkMessage italic(String text) {
        textBuffer.append("_").append(text).append("_");
        return this;
    }

    public ChatworkMessage code(String code) {
        textBuffer.append("`").append(code).append("`");
        return this;
    }

    public ChatworkMessage preformatted(String text) {
        textBuffer.append("```").append(text).append("```");
        return this;
    }

    public ChatworkMessage quote(String text) {
        textBuffer.append("\n> ").append(text).append("\n");
        return this;
    }

    public String toString() {
        return textBuffer.toString();
    }

    public String rawText() {
        // We're not removing link because it's readable the way it is.
        return textBuffer.toString()
            .replaceAll("(.*)\\*(.*)\\*(.*)", "$1$2$3") // Remove bold formatting
            .replaceAll("(.*)_(.*)_(.*)", "$1$2$3")     // Remove italic formatting
            .replaceAll("(.*)```(.*)```(.*)", "$1$2$3") // Remove pretext formatting
            .replaceAll("(.*)`(.*)`(.*)", "$1$2$3")     // Remove code formatting
            .replaceAll("\n>\\s+(.*)\n", "$1");         // Remove Quote formatting
    }
}

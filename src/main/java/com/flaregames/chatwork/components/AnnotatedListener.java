package com.flaregames.chatwork.components;

import com.atlassian.confluence.event.events.content.ContentEvent;
import com.atlassian.confluence.event.events.content.blogpost.BlogPostCreateEvent;
import com.atlassian.confluence.event.events.content.blogpost.BlogPostUpdateEvent;
import com.atlassian.confluence.event.events.content.page.PageCreateEvent;
import com.atlassian.confluence.event.events.content.page.PageUpdateEvent;
import com.atlassian.confluence.pages.AbstractPage;
import com.atlassian.confluence.pages.TinyUrl;
import com.atlassian.confluence.user.ConfluenceUser;
import com.atlassian.confluence.user.PersonalInformationManager;
import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.webresource.UrlMode;
import com.atlassian.plugin.webresource.WebResourceUrlProvider;
import com.atlassian.user.User;
import in.ashwanthkumar.chatwork.webhook.Chatwork;
import in.ashwanthkumar.chatwork.webhook.ChatworkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class AnnotatedListener implements DisposableBean, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotatedListener.class);

    private final WebResourceUrlProvider webResourceUrlProvider;
    private final EventPublisher eventPublisher;
    private final ConfigurationManager configurationManager;
    private final PersonalInformationManager personalInformationManager;

    public AnnotatedListener(EventPublisher eventPublisher, ConfigurationManager configurationManager,
                             PersonalInformationManager personalInformationManager, WebResourceUrlProvider webResourceUrlProvider) {
        this.eventPublisher = checkNotNull(eventPublisher);
        this.configurationManager = checkNotNull(configurationManager);
        this.personalInformationManager = checkNotNull(personalInformationManager);
        this.webResourceUrlProvider = checkNotNull(webResourceUrlProvider);
    }

    @EventListener
    public void blogPostCreateEvent(BlogPostCreateEvent event) {
        sendMessages(event, event.getBlogPost(), "创建了博客");
    }

    @EventListener
    public void blogPostUpdateEvent(BlogPostUpdateEvent event) {
        sendMessages(event, event.getBlogPost(), "更新了博客");
    }

    @EventListener
    public void pageCreateEvent(PageCreateEvent event) {
        sendMessages(event, event.getPage(), "创建了文档");
    }

    @EventListener
    public void pageUpdateEvent(PageUpdateEvent event) {
        sendMessages(event, event.getPage(), "更新了文档");
    }

    private void sendMessages(ContentEvent event, AbstractPage page, String action) {
        if (event.isSuppressNotifications()) {
            LOGGER.info("Suppressing notification for {}.", page.getTitle());
            return;
        }
        String message = getMessage(page, action);
        sendMessage(message, page, action);
    }


    private String getMessage(AbstractPage page, String action) {
        ConfluenceUser user = page.getLastModifier() != null ? page.getLastModifier() : page.getCreator();
        String message = action + "<a href=\"" +tinyLink(page) + "\"> " + page.getTitle() + " </a> - " + user.getFullName();
        return message;
    }

    private void sendMessage(String message, AbstractPage page, String action) {
        LOGGER.info("Sending to {} with message {}.", configurationManager.getWebhookUrl(), message.toString());
        try {
            new Chatwork(configurationManager.getWebhookUrl()).sendToSpaceId(page.getSpace().getKey()).sendToUrl(tinyLink(page)).sendToAction(action).push(message);
        } catch (IOException e) {
            LOGGER.error("Error when sending LeanChat message", e);
        }
    }

    private ChatworkMessage appendPersonalSpaceUrl(ChatworkMessage message, User user) {
        if (null == user) {
            return message.text("unknown user");
        }
        return message.link(webResourceUrlProvider.getBaseUrl(UrlMode.ABSOLUTE) + "/"
                + personalInformationManager.getOrCreatePersonalInformation(user).getUrlPath(), user.getFullName());
    }

    private ChatworkMessage appendPageLink(ChatworkMessage message, AbstractPage page) {
        return message.link(tinyLink(page), page.getSpace().getDisplayTitle() + " - " + page.getTitle());
    }

    private String tinyLink(AbstractPage page) {
        return webResourceUrlProvider.getBaseUrl(UrlMode.ABSOLUTE) + "/x/" + new TinyUrl(page).getIdentifier();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("Register LeanChat event listener");
        eventPublisher.register(this);
    }

    @Override
    public void destroy() throws Exception {
        LOGGER.debug("Un-register LeanChat event listener");
        eventPublisher.unregister(this);
    }
}

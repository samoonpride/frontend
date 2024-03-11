package com.samoonpride.line.config;

import com.samoonpride.line.enums.MessageKeys;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageSourceConfig {
    private static MessageSource messageSource;

    public MessageSourceConfig() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        MessageSourceConfig.messageSource = messageSource;
    }

    public static String getMessage(MessageKeys messageKeys, Locale locale) {
        return messageSource.getMessage(messageKeys.getKey(), null, locale);
    }

    public static String getMessage(MessageKeys messageKeys) {
        return messageSource.getMessage(messageKeys.getKey(), null, Locale.ENGLISH);
    }

    public static String getMessage(MessageKeys messageKeys, Object[] args) {
        return messageSource.getMessage(messageKeys.getKey(), args, Locale.ENGLISH);
    }
}

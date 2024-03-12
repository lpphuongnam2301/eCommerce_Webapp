package com.project.shopapp.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtil {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public String getLocale(String messageKey, Object ... params)
    {
        Locale locale = localeResolver.resolveLocale(getCurrentRequest());
        return messageSource.getMessage(messageKey, params, locale);
    }

    private HttpServletRequest getCurrentRequest()
    {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }
}

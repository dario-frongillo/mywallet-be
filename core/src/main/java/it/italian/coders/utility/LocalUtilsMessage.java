package it.italian.coders.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocalUtilsMessage {
    @Autowired
    @Qualifier("errorMessageSource")
    private ReloadableResourceBundleMessageSource messageSource;

    public String getI18nMessage(String keyMessage, Object[] params, Locale locale){
        try {
            return messageSource.getMessage(keyMessage, params, locale);
        }catch (NoSuchMessageException ex){
            return "??" + keyMessage + "??";
        }
    }


    public String getI18nMessage(String keyMessage, Object[] params){
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(keyMessage, params, locale);
        }catch (NoSuchMessageException ex){
            return "??" + keyMessage + "??";
        }
    }
}

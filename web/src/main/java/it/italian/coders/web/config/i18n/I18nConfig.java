package it.italian.coders.web.config.i18n;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import java.util.Locale;


@Configuration
public class I18nConfig    extends WebMvcConfigurerAdapter {
    @Bean
    public LocaleResolver localeResolver() {
        SmartLocaleResolver smartLocaleResolver= new SmartLocaleResolver();
        smartLocaleResolver.setDefaultLocale(Locale.ITALIAN);
        return  smartLocaleResolver;
    }


    @Bean(name="errorMessageSource")
    public ReloadableResourceBundleMessageSource  errorMessageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/i18n/errors/messages","classpath:/i18n/errors/validations");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}


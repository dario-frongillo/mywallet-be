package it.italian.coders.service.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String build(Map<String,String> parameters, String template) {
        Context context = new Context();

        parameters.keySet().forEach(key->{
            context.setVariable(key, parameters.get(key));
        });

        return templateEngine.process(template, context);
    }

}
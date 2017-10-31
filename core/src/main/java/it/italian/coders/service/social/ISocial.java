package it.italian.coders.service.social;

import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;

public interface ISocial {
    User updInsSocialUser(String accessToken);
}

package it.italian.coders.service.social;

import it.italian.coders.model.authentication.User;

public interface SocialManager {
    User findByUserByAccessToken(String accessToken);
    User findByUserByAccessToken(String accessToken, boolean autoInsert);

}

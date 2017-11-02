package it.italian.coders.service.signup;

import it.italian.coders.model.authentication.SignUp;
import it.italian.coders.model.authentication.User;

public interface SignupManager {
    User signupUser(SignUp signUp);
    void sendConfirmSignMail(User user);
    void confirmSignup(String locale, String userIn, String tokenIdIn);
}

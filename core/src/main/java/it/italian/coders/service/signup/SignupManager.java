package it.italian.coders.service.signup;

import it.italian.coders.model.authentication.SignUp;
import it.italian.coders.model.authentication.User;

import javax.servlet.http.HttpServletRequest;

public interface SignupManager {
    User signupUser(SignUp signUp, HttpServletRequest request);
    void sendConfirmSignMail(User user, HttpServletRequest request);
    void confirmSignup(String locale, String userIn, String tokenIdIn);
}

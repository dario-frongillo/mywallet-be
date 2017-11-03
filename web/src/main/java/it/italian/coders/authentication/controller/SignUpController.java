package it.italian.coders.authentication.controller;

import it.italian.coders.authentication.jwt.JwtAuthenticationRequest;
import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.exception.RestException;
import it.italian.coders.model.authentication.SignUp;
import it.italian.coders.model.authentication.User;
import it.italian.coders.service.resetAccount.ResetAccountManager;
import it.italian.coders.service.signup.SignupManager;
import it.italian.coders.utility.LocalUtilsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class SignUpController {

    @Autowired
    UserDao userDao;

    @Autowired
    SignupManager signupManager;

    @Autowired
    LocalUtilsMessage localUtilsMessage;

    @Autowired
    ResetAccountManager resetAccountManager;

    private final static Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @RequestMapping(value = "public/v1/signup", method = RequestMethod.POST)
    public ResponseEntity<?> signup(@RequestBody @Valid SignUp signup, Device device, HttpServletRequest request) throws AuthenticationException {
        User user = userDao.findByUsernameIgnoreCase(signup.getUsername());
        if(signup.getUsername().contains(" ")||signup.getUsername().contains("@")){
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("SignUpController.InvalidUsername.title",null),
                    localUtilsMessage.getI18nMessage("SignUpController.InvalidUsername.detail",new Object[] { signup.getUsername()}),
                    0);
        }

        if(user != null){
            throw new RestException(HttpStatus.BAD_REQUEST,
                                    localUtilsMessage.getI18nMessage("SignUpController.UserAlreadyExists.title",null),
                                    localUtilsMessage.getI18nMessage("SignUpController.UserAlreadyExists.detail",new Object[] { signup.getUsername()}),
                                        0);
        }

        return ResponseEntity.ok(signupManager.signupUser(signup));
    }

    @RequestMapping(value = "public/v1/signup/confirm", method = RequestMethod.GET)
    public ResponseEntity<?> confirmSignup(@RequestParam(value = "username", required = true) String username,
                                           @RequestParam(value = "verificationToken", required = true) String verificationToken,
                                           @RequestParam(value = "language", required = true) String language) {

        try{
           signupManager.confirmSignup(language,username,verificationToken);
           return ResponseEntity.ok( localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.ok",null));
        }catch(RestException ex){
            logger.error("Error on confirm signup",ex);
            return ResponseEntity.ok(ex.getDetail());
        }

    }

}
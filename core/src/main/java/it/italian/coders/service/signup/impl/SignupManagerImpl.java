package it.italian.coders.service.signup.impl;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.dao.verification.VerificationTokenDao;
import it.italian.coders.exception.RestException;
import it.italian.coders.model.authentication.SignUp;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.model.verification.VerificationStatusEnum;
import it.italian.coders.model.verification.VerificationToken;
import it.italian.coders.model.verification.VerificationTypeEnum;
import it.italian.coders.service.mail.MailService;
import it.italian.coders.service.signup.SignupManager;
import it.italian.coders.utility.Constants;
import it.italian.coders.utility.LocalUtilsMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.apache.commons.lang3.LocaleUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

@Service
public class SignupManagerImpl implements SignupManager {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    @Autowired
    VerificationTokenDao verificationTokenDao;

    @Autowired
    LocalUtilsMessage localUtilsMessage;

    private final static Logger logger = LoggerFactory.getLogger(SignupManagerImpl.class);



    @Value("${signup.verificationToken.expiration}")
    private Integer tokenExpiration;

    @Value("${appplication.baseUrl}")
    private String baseUrl;

    @Value("${signup.confirmVerificationToken.path}")
    private String confirmVerificationTokenPath;

    @Autowired
    UserDao userDao;

    private String getConfirmVerificationTokenUrl(String username, String tokenId){
        Locale locale = LocaleContextHolder.getLocale();

        return baseUrl+"/"+confirmVerificationTokenPath+"?"+"username="+username+"&verificationToken="+tokenId+"&language="+locale.getLanguage();

    }


    public void sendConfirmSignMail(User user, HttpServletRequest request){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, tokenExpiration);
        Date expiration =  new Date(cal.getTime().getTime());
        String code = RandomStringUtils.randomAlphabetic(5);

        VerificationToken verificationToken = VerificationToken.newBuilder()
                                                        .expiryDate(expiration)
                                                        .id(code)
                                                        .username(user.getUsername())
                                                        .status(VerificationStatusEnum.WAITING)
                                                        .type(VerificationTypeEnum.Signup)
                                                        .build();

        verificationToken = verificationTokenDao.save(verificationToken);
        Map<String,String> mailParams = new HashMap<>();
        mailParams.put("activationUrl", getConfirmVerificationTokenUrl(user.getUsername(),verificationToken.getId()));
        mailParams.put("displayName", user.getDisplayName());
        mailParams.put("signature", "Lo Staff di myWallet");
        mailParams.put("code", code);

        mailService.sendMailByTemplate(user.getEmail(),"subj", "signupMail",mailParams);

    }


    @Override
    public void confirmSignup(String language, String userIn, String tokenIdIn) {
        User user = userDao.findByUsernameIgnoreCase(userIn);
        Locale locale = LocaleUtils.toLocale(language);
        if(user == null){
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    0);
        }
        VerificationToken verificationToken = verificationTokenDao.findById(tokenIdIn);

        if(verificationToken == null){
            logger.error("Invalid verification token [{}]",tokenIdIn);
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    0);
        }

        if(verificationToken.getStatus()!=VerificationStatusEnum.WAITING){
            logger.error("Invalid verification token [{}]",tokenIdIn);
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    0);
        }

        if(!verificationToken.getUsername().equals(userIn)){
            logger.error("Invalid verification token [{}]",tokenIdIn);
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    localUtilsMessage.getI18nMessage("SignupManager.confirmSignup.InvalidToke",null),
                    0);
        }

        user.setSignUpConfirmed(true);
        userDao.save(user);
        verificationToken.setStatus(VerificationStatusEnum.CLOSED);
        verificationToken = verificationTokenDao.save(verificationToken);
    }

    @Override
    public User signupUser(SignUp signUp, HttpServletRequest request) {
        String firstname="",lastname="";
        String displayName= signUp.getUsername();

        if(!StringUtils.isEmpty(signUp.getFirstname())){
            firstname=signUp.getFirstname();
        }

        if(!StringUtils.isEmpty(signUp.getLastname())){
            lastname=signUp.getLastname();
        }

        if(!StringUtils.isEmpty(firstname)){
            displayName = firstname;
            if(!StringUtils.isEmpty(lastname)){
                displayName += " "+lastname;
            }
        }


        User user = User.newBuilder()
                        .username(signUp.getUsername())
                        .password(passwordEncoder.encode(signUp.getPassword()))
                        .email(signUp.getEmail())
                        .enabled(true)
                        .isSignUpConfirmed(false)
                        .firstname(signUp.getFirstname())
                        .lastname(signUp.getLastname())
                        .displayName(displayName)
                        .gender(signUp.getGender())
                        .profileImageUrl(signUp.getProfileImageUrl())
                        .socialType(SocialEnum.None)
                        .authorities(Constants.DEFAUL_AUTHORITIES)
                        .build();

        sendConfirmSignMail(user,request);
        return userDao.save(user);
    }
}

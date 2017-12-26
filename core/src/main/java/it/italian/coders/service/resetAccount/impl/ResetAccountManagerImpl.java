package it.italian.coders.service.resetAccount.impl;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.dao.verification.VerificationTokenDao;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.verification.VerificationStatusEnum;
import it.italian.coders.model.verification.VerificationToken;
import it.italian.coders.model.verification.VerificationTypeEnum;
import it.italian.coders.service.mail.MailService;
import it.italian.coders.service.resetAccount.ResetAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ResetAccountManagerImpl implements ResetAccountManager {


    @Value("${signup.verificationToken.expiration}")
    private Integer tokenExpiration;

    @Autowired
    private UserDao userDao;


    @Value("${appplication.baseUrl}")
    private String baseUrl;

    @Value("${resetAccount.confirmResetPaswordVerificationToken.path}")
    private String confirmResetPassordVerificationTokenPath;

    @Autowired
    VerificationTokenDao verificationTokenDao;

    @Autowired
    MailService mailService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public void sendResetMailRequest(User user) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, tokenExpiration);
        Date expiration =  new Date(cal.getTime().getTime());
        String uuid =UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.newBuilder()
                .expiryDate(expiration)
                .id(uuid)
                .username(user.getUsername())
                .confirmResetCode(uuid.substring(0,7))
                .status(VerificationStatusEnum.WAITING)
                .type(VerificationTypeEnum.ResetPassword)
                .build();

        Map<String,String> mailParams = new HashMap<>();
        mailParams.put("displayName", user.getDisplayName());
        mailParams.put("confirmResetCode",uuid.substring(0,7));
        mailParams.put("signature", "mySocial Staff");
        verificationToken = verificationTokenDao.save(verificationToken);
        mailService.sendMailByTemplate(user.getEmail(),"subj", "resetPasswordMail",mailParams);

    }

    @Override
    public void confirmResetPassword(User user, VerificationToken verificationToken, String newPassord) {
        verificationToken.setStatus(VerificationStatusEnum.CLOSED);
        user.setPassword(passwordEncoder.encode(newPassord));
        userDao.save(user);
        verificationTokenDao.save(verificationToken);
    }
}

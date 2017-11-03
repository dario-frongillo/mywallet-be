package it.italian.coders.authentication.controller;

import it.italian.coders.dao.authentication.UserDao;
import it.italian.coders.dao.verification.VerificationTokenDao;
import it.italian.coders.exception.NoSuchEntityException;
import it.italian.coders.exception.RestException;
import it.italian.coders.model.authentication.User;
import it.italian.coders.model.social.SocialEnum;
import it.italian.coders.model.verification.VerificationStatusEnum;
import it.italian.coders.model.verification.VerificationToken;
import it.italian.coders.model.verification.VerificationTypeEnum;
import it.italian.coders.service.resetAccount.ResetAccountManager;
import it.italian.coders.utility.LocalUtilsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResetAccountController {
    @Autowired
    UserDao userDao;

    @Autowired
    LocalUtilsMessage localUtilsMessage;

    @Autowired
    VerificationTokenDao verificationTokenDao;

    @Autowired
    ResetAccountManager resetAccountManager;

    @RequestMapping(value = "public/v1/reset-password-account/{user}", method = RequestMethod.POST)
    public ResponseEntity<?> resetUserPassword(@PathVariable(value = "user") String username) {
        User user = null;
        if(username.contains("@")){
            user = userDao.findByUsernameIgnoreCase(username);
        }else{
            user = userDao.findByEmailIgnoreCase(username);
        }

        if(user == null){
            throw new NoSuchEntityException();
        }
        if(user.getSocialEnum()!= SocialEnum.None){
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("ResetAccountController.UserIsSocial.notAllowed.title",null),
                    localUtilsMessage.getI18nMessage("ResetAccountController.UserIsSocial.notAllowed.detail",new Object[] { username}),
                    0);
        }

        resetAccountManager.sendResetMailRequest(user);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "public/v1/confirm-reset-password-account/{user}", method = RequestMethod.POST)
    public ResponseEntity<?> confirmResetUserPassword(@PathVariable(value = "user") String username,
                                                      @RequestParam(required = true) String confirmResetCode,
                                                      @RequestParam(required = true) String newPassord) {
        User user = null;
        if(username.contains("@")){
            user = userDao.findByUsernameIgnoreCase(username);
        }else{
            user = userDao.findByEmailIgnoreCase(username);
        }


        if(user == null){
            throw new NoSuchEntityException();
        }
        if(user.getSocialEnum()!= SocialEnum.None){
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("ResetAccountController.UserIsSocial.notAllowed.title",null),
                    localUtilsMessage.getI18nMessage("ResetAccountController.UserIsSocial.notAllowed.detail",new Object[] { username}),
                    0);
        }

        VerificationToken verificationToken = verificationTokenDao.findByConfirmResetCode(confirmResetCode);
        if(verificationToken== null || verificationToken.getType()!= VerificationTypeEnum.ResetPassword || verificationToken.getStatus()!= VerificationStatusEnum.WAITING){
            throw new RestException(HttpStatus.BAD_REQUEST,
                    localUtilsMessage.getI18nMessage("ResetAccountController.InvalidVerificationToken.title",null),
                    localUtilsMessage.getI18nMessage("ResetAccountController.InvalidVerificationToken.detail",new Object[] { username}),
                    0);
        }


        resetAccountManager.confirmResetPassword(user,verificationToken,newPassord);
        return ResponseEntity.noContent().build();
    }
}

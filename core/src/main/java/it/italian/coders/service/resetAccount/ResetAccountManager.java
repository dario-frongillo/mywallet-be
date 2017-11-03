package it.italian.coders.service.resetAccount;

import it.italian.coders.model.authentication.User;
import it.italian.coders.model.verification.VerificationToken;

public interface ResetAccountManager {
    void sendResetMailRequest(User user);
    void confirmResetPassword(User user, VerificationToken verificationToken, String newPassord);
}

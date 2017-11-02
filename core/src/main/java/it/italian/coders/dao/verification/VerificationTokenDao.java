package it.italian.coders.dao.verification;

import it.italian.coders.model.authentication.User;
import it.italian.coders.model.verification.VerificationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VerificationTokenDao extends MongoRepository<VerificationToken, String> {

    VerificationToken findById(String username);


}

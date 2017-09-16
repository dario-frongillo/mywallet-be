package it.italian.coders.dao.authentication;

import it.italian.coders.model.authentication.User;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface UserDao extends MongoRepository<User, String> {

    User findByUsernameIgnoreCase( String username);
    User findByUsername(String username);
    User findByEmailIgnoreCase(String email);


}

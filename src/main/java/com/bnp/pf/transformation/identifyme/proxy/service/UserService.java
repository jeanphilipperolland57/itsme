package com.bnp.pf.transformation.identifyme.proxy.service;

import com.bnp.pf.transformation.identifyme.proxy.entity.SecUser;
import com.bnp.pf.transformation.identifyme.proxy.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void saveUserData(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String nrn = oAuth2User.getAttribute("eid");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String birthDate = oAuth2User.getAttribute("birthdate");

        var found = userRepo.findById(nrn);

        if (found.isPresent()) {
            var user = found.get();
            log.info("SecUser {} already exists", user.getId());
        } else {
            var newUser = new SecUser();
            newUser.setEmail(email);
            newUser.setDateOfBirth(birthDate);
            newUser.setId(nrn);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);

            userRepo.save(newUser);
        }
    }

    public SecUser findById(String id) {
        return userRepo.findById(id).orElse(null);
    }
}

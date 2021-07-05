package com.enseignementsuperieur.coucheapi.appuser;

import com.enseignementsuperieur.coucheapi.registration.token.ConfirmationToken;
import com.enseignementsuperieur.coucheapi.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/*
* \brief implémentation de l'interface se sécurité pour la classe
*       AppUser, ici inpléement la procedure pour se connecter également
* */

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG =
        "User with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException(
                        String.format(USER_NOT_FOUND_MSG,email)) );
    }

    public String signUpUser(AppUser appUser){
    boolean userExist=appUserRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if (userExist) {
            throw new IllegalStateException("email already taken");
        }
          String encodePassword= bCryptPasswordEncoder.
                  encode(appUser.getPassword());
        appUser.setPassword(encodePassword);

        appUserRepository.save(appUser);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(

              token,
              LocalDateTime.now(),
              LocalDateTime.now().plusMinutes(15),
                appUser

        );

        confirmationTokenService.saveConfirmationToken(
                confirmationToken
        );

        // TODO: SEND EMAIL
        return token;

    }
}

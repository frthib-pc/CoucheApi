package com.enseignementsuperieur.coucheapi.registration;

import com.enseignementsuperieur.coucheapi.appuser.AppUser;
import com.enseignementsuperieur.coucheapi.appuser.AppUserRoles;
import com.enseignementsuperieur.coucheapi.appuser.AppUserService;
import com.enseignementsuperieur.coucheapi.registration.token.ConfirmationToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail){
            throw new IllegalStateException("email not valid");
        }

        return appUserService.signUpUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRoles.USER


                )
        );
    }
   /** @Transactional
    public String confirmationToken(String token)
    {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(()->
                        new IllegalStateException("token not found"));
        if(confirmationToken.getConfirmedAt()!=null)
        {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if(expiredAt.isBefore(LocalDateTime.now())){
            throw new IllegalStateException("taken expired")
        }

        cofirmationTkenService.setConfirmationAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getEmail());
        return "Confirmed";
    }*/
}

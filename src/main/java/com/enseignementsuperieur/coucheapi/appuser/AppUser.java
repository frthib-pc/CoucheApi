package com.enseignementsuperieur.coucheapi.appuser;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Table(name = "AppUser", indexes = {
        @Index(name = "idx_appuser_firstname", columnList = "firstName")
})
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity // pour spécifier que c'est la table utilisateur

/*
* \ brief déclaration des attributs de l'utilisateurs.
* */
public class AppUser implements UserDetails {


    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AppUserRoles appUserRoles;
    private Boolean locked= false; // pour verifier si le compte est sécurisé
    private Boolean enabled= false; // pour vérifier si le compte est activé.

// pas besoin de faire une construteur sue le Id car il sera générer automatiquement
    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String password,
                   AppUserRoles appUserRoles
                   )
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRoles = appUserRoles;

    }

    @Override
    // accorde un acces a un objet
    // on créer un objet d'authorisation qu'on accorde a une type d'utilisateur par son nom
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(appUserRoles.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

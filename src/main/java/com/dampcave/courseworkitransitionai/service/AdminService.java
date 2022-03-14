package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.models.Film;
import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.FilmRepository;
import com.dampcave.courseworkitransitionai.repositoryes.RoleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final StorageService storageService;
    private final FilmRepository filmRepository;
    private final Role admin = new Role(2L, "ADMIN");

    public Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Value("${site.url}")
    private String urlSite;

    @Autowired
    public AdminService(UserRepository userRepository,
                        RoleRepository roleRepository,
                        BCryptPasswordEncoder passwordEncoder,
                        MailSender mailSender,
                        StorageService storageService,
                        FilmRepository filmRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.storageService = storageService;
        this.filmRepository = filmRepository;
    }

    public void addOrRemoveRoleAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        if (user.getRoles().contains(admin)) {
            user.getRoles().remove(admin);
        } else {
            user.getRoles().add(admin);
        }
        userRepository.save(user);
    }

    public boolean hasRoleAdmin(String username){
        User user = userRepository.findByUsername(username).orElseThrow();
        return user.getRoles().contains(admin);
    }


    public boolean hasAccess(String username) {
        return username.equals(getAuth().getName());
    }

    public boolean hasAccessOnFilm(Long id) {
      Film film =  filmRepository.findById(id).orElseThrow();
        return film.getAuthor().getUsername().equals(getAuth().getName());
    }

}

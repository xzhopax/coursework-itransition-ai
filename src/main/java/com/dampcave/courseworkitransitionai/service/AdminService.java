package com.dampcave.courseworkitransitionai.service;

import com.dampcave.courseworkitransitionai.models.Role;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final Role admin = new Role(2L, "ADMIN");

    @Autowired
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addOrRemoveRoleAdmin(User user) {
        if (user.getRoles().contains(admin)) {
            user.getRoles().remove(admin);
        } else {
            user.getRoles().add(admin);
        }
        userRepository.save(user);
    }

    public boolean hasRoleAdmin(User user) {
        return user.getRoles().contains(admin);
    }

    public String getViewIfHasRoleAdmin(User user,
                                        String ifAdmin,
                                        String ifNotAdmin) {
        if (user.getRoles().contains(admin)) {
            return ifAdmin;
        }
        return ifNotAdmin;
    }

    public void deleteUser(User user) {
        if (!hasRoleAdmin(user)) {
            userRepository.delete(user);
        }
    }

    public void blockOrUnblockUser(User user) {
        if (!hasRoleAdmin(user)) {
            user.setActive(!user.isActive());
            userRepository.save(user);
        }
    }

}

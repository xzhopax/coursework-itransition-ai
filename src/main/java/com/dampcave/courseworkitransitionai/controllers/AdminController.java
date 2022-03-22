package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.service.AdminService;
import com.dampcave.courseworkitransitionai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAuthority('ADMIN')")
@Controller
@RequestMapping("/users")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    @Autowired
    public AdminController(UserService userService,
                           AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/get-admin/{id}")
    public String addAdminOrRemoveAdmin(@PathVariable(value = "id") Long id) {
        adminService.addOrRemoveRoleAdmin(userService.findUserById(id));
        return "redirect:/users";
    }

    @GetMapping()
    public String showAllUser(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("title", "Users");
        return "profiles/users";
    }

    @GetMapping("/profile/{user}")
    public String showProfileUserFromAdmin(@PathVariable User user,Model model) {
        model.addAttribute("user", user);
        return "profiles/profile";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        adminService.deleteUser(userService.findUserById(id));
        return "redirect:/users";
    }

    @RequestMapping(value = "/{id}/is-active", method = RequestMethod.GET)
    public String userIsActive(@PathVariable(value = "id") Long id) {
        adminService.blockOrUnblockUser(userService.findUserById(id));
        return "redirect:/users";
    }
}

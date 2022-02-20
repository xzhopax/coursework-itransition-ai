package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.People;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.PeopleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class UserController {

    private PeopleRepository peopleRepository;
    private UserRepository userRepository;
    private UserAuthService userAuthService;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;

    @Autowired
    public UserController(PeopleRepository peopleRepository, UserRepository userRepository,
                          UserAuthService userAuthService, HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse) {
        this.peopleRepository = peopleRepository;
        this.userRepository = userRepository;
        this.userAuthService = userAuthService;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("title", "All Users");
        Iterable<People> peoples = peopleRepository.findAll();
        model.addAttribute("peoples", peoples);
        return "users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable(value = "id") Long id, Model model) {
        People people = peopleRepository.findById(id).orElseThrow();
        peopleRepository.delete(people);
        return "redirect:/users";
    }

    @GetMapping("/users/deleteall")
    public String deleteAllUsers() {
        peopleRepository.deleteAll();
        return "redirect:/users";
    }


    public String userTargetDelete(String[] usersId) {
        for (int i = 0; i < usersId.length; i++) {
            Long id = Long.parseLong(usersId[i]);
            People people = peopleRepository.findById(id).orElseThrow();
            peopleRepository.delete(people);
        }
        return "redirect:/users";
    }




}

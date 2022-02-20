package com.dampcave.courseworkitransitionai.controllers;

import com.dampcave.courseworkitransitionai.models.People;
import com.dampcave.courseworkitransitionai.models.User;
import com.dampcave.courseworkitransitionai.repositoryes.PeopleRepository;
import com.dampcave.courseworkitransitionai.repositoryes.UserRepository;
import com.dampcave.courseworkitransitionai.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    @GetMapping("/users/blocked")
    public String userBlockedAll(HttpServletRequest request, HttpServletResponse response) {
        List<People> users = peopleRepository.findAll();
        for (People people : users) {
            people.getUser().setActive(false);
            peopleRepository.save(people);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/login?logout";
    }

    @GetMapping("/users/unblocked")
    public String userUnblockedAll() {
        List<People> users = peopleRepository.findAll();
        for (People people : users) {
            people.getUser().setActive(true);
            peopleRepository.save(people);
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/users/unblocked/{id}", method = RequestMethod.GET)
    public String userIsActive(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable(value = "id") Long id) {
        People people = peopleRepository.findById(id).orElseThrow();
        people.getUser().setActive(!people.getUser().isActive());
        peopleRepository.save(people);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (people.getUser().getUsername().equals(auth.getName())) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return "redirect:/login?logout";
        }
        return "redirect:/users";
    }

    @GetMapping("/usersblock")
    public String deleteUserId(@RequestParam(value = "isChecked") List<String> isChecked, Model model) {
        if (isChecked != null) {
            for (String str : isChecked) {
                Long id = Long.parseLong(str);
                User user = userRepository.getById(id);
                user.setActive(false);
                People people = peopleRepository.findByUserUsername(user.getUsername()).orElseThrow();
                peopleRepository.save(people);
            }
        }
        return "redirect:/users";
    }

    public String userTargetBlock(String[] usersId) {
        boolean isFlag = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        for (int i = 0; i < usersId.length; i++) {
            Long id = Long.parseLong(usersId[i]);
            People people = peopleRepository.findById(id).orElseThrow();
            people.getUser().setActive(false);
            peopleRepository.save(people);

            if (people.getUser().getUsername().equals(auth.getName())) {
                isFlag = true;
            }
        }
        if (isFlag){
            new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
            return "redirect:/login?logout";
        }
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

    public String userTargetUnblock(String[] usersId) {
        for (int i = 0; i < usersId.length; i++) {
            Long id = Long.parseLong(usersId[i]);
            People people = peopleRepository.findById(id).orElseThrow();
            people.getUser().setActive(true);
            peopleRepository.save(people);
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/user-target", method = RequestMethod.GET)
    public String userTarget(HttpServletRequest request) {

        String[] checkeds = request.getParameterValues("isChecked");
        String[] deletes = request.getParameterValues("delete");
        String[] blocked = request.getParameterValues("block");
        String[] unblocked = request.getParameterValues("unblock");

        if (checkeds == null) {
            return "redirect:/users";
        } else if (deletes == null) {
            if (blocked == null) {
                if (unblocked == null) {
                    return "redirect:/users";
                } else {
                    userTargetUnblock(checkeds);
                    return "redirect:/users";
                }
            } else {
                userTargetBlock(checkeds);
                return "redirect:/users";
            }
        } else {
            userTargetDelete(checkeds);
            return "redirect:/users";
        }
    }
}

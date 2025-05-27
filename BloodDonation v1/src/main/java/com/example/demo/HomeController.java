package com.example.demo;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); // get logged-in username
        model.addAttribute("username", username);
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        // Encrypt the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return "redirect:/login";  // after registration, go to login page
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/users")
    public String listAllUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);

        return "users";
    }

    @GetMapping("/api/users/search")
    @ResponseBody
    public List<User> searchUsers(
            @RequestParam(required = false) String bloodGroup,
            @RequestParam(required = false) String city
    ) {
        if ((bloodGroup != null && !bloodGroup.isEmpty()) && (city != null && !city.isEmpty())) {
            return userRepository.findByBloodGroupContainingIgnoreCaseAndCityContainingIgnoreCase(bloodGroup, city);
        } else if (bloodGroup != null && !bloodGroup.isEmpty()) {
            return userRepository.findByBloodGroupContainingIgnoreCase(bloodGroup);
        } else if (city != null && !city.isEmpty()) {
            return userRepository.findByCityContainingIgnoreCase(city);
        } else {
            return userRepository.findAll();
        }
    }

    @GetMapping("/contact")
    public String showContactPage() {
        return "contact";
    }
}
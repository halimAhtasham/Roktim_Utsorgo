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

import java.security.Principal;
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
    @GetMapping("/admin-login")
    public String adminLoginPage() {
        return "admin-login";
    }

//    @GetMapping("/admin")
//    public String showAdminDashboard(Model model) {
//        List<User> users = userRepository.findAll();
//        model.addAttribute("users", users);
//        return "admin"; // maps to templates/admin.html
//    }

    @GetMapping("/admin")
    public String showAdminDashboard(Model model, Principal principal) {
        System.out.println("Logged in user: " + principal.getName());
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user", user);
        return "edit-user"; // maps to edit-user.html
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userRepository.save(user); // saves changes
        return "redirect:/admin";
    }


}
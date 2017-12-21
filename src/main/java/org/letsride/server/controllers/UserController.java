package org.letsride.server.controllers;

import org.letsride.server.models.User;
import org.letsride.server.repositories.UserRepository;
import org.letsride.server.requests.auth.UserSignup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    public ResponseEntity getMe() {
        String emailAddress = SecurityContextHolder.getContext().getAuthentication().getName();
        if (emailAddress == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.BAD_REQUEST);
        }

        User foundUser = this.userRepository.findByAccount_Username(emailAddress);

        if (foundUser == null) {
            return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.ok(foundUser);
    }


    @PutMapping("/me")
    public ResponseEntity editUser(UserSignup user) {
        String emailAddress = SecurityContextHolder.getContext().getAuthentication().getName();

        User u = this.userRepository.findByAccount_Username(emailAddress);

        if (user.getEmail() != null) {
            u.setEmailAddress(emailAddress);
        }

        if (user.getFirstname() != null) {
            u.setFirstName(user.getFirstname());
        }

        if (user.getLastname() != null) {
            u.setLastName(user.getLastname());
        }

        if (user.getUsername() != null) {
            u.setUsername(user.getUsername());
        }

        this.userRepository.save(u);

        return ResponseEntity.ok(u);
    }



    @PutMapping("/picture")
    public ResponseEntity handlePictureUpload(@RequestParam("file") MultipartFile inputFile) {
        if (inputFile.isEmpty()) {
            return new ResponseEntity("Missing file", HttpStatus.BAD_REQUEST);
        } else {
            try {
                String newFilename = UUID.randomUUID().toString();

                File f = new File("/Users/Lucas/Desktop/SPRING_DATA/" + newFilename);
                inputFile.transferTo(f);

                String emailAddress = SecurityContextHolder.getContext().getAuthentication().getName();
                User u = this.userRepository.findByAccount_Username(emailAddress);

                u.setProfilePicture(newFilename);

                this.userRepository.save(u);

                return ResponseEntity.ok(u);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity("Could not process file", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

    }

}

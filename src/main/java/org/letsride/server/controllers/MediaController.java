package org.letsride.server.controllers;

import org.letsride.server.models.User;
import org.letsride.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final UserRepository userRepository;

    @Autowired
    public MediaController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public void getPictureForUser(HttpServletResponse response, @PathVariable String id) {
        try {

            File f = new File("/Users/Lucas/Desktop/SPRING_DATA/" + id);

            if (f.exists()) {

                response.setContentType("image/png");

                response.setContentLength((int) f.length());

                InputStream inputStream = new BufferedInputStream(new FileInputStream(f));

                //Copy bytes from source to destination(outputstream in this example), closes both streams.
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            } else {
                response.sendError(400, "File not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

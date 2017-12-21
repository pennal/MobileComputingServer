package org.letsride.server.controllers;

import org.letsride.server.models.Ride;
import org.letsride.server.models.User;
import org.letsride.server.repositories.RideRepository;
import org.letsride.server.repositories.UserRepository;
import org.letsride.server.requests.auth.RideData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rides")
public class RideController {

    private final UserRepository userRepository;
    private final RideRepository rideRepository;

    @Autowired
    public RideController(UserRepository userRepository, RideRepository rideRepository) {
        this.userRepository = userRepository;
        this.rideRepository = rideRepository;
    }

    @PostMapping
    public ResponseEntity postRide(@RequestBody RideData ride) {
        String emailAddress = SecurityContextHolder.getContext().getAuthentication().getName();

        User u = this.userRepository.findByAccount_Username(emailAddress);

        Ride r = new Ride();
        r.setDataPoints(ride.getDataPoints());
        r.setStartDate(ride.getStartDate());
        r.setEndDate(ride.getEndDate());
        r.setPublic(false);

        this.rideRepository.save(r);
        u.addRide(r);
        this.userRepository.save(u);

        return ResponseEntity.ok("");
    }

    @GetMapping("/mine")
    public ResponseEntity getMyRides() {
        String emailAddress = SecurityContextHolder.getContext().getAuthentication().getName();

        User u = this.userRepository.findByAccount_Username(emailAddress);

        List<Ride> rides = u.getRides();
        if (rides == null) {
            rides = new ArrayList<>();
        }

        return ResponseEntity.ok(rides);

    }

    @GetMapping("/public")
    public ResponseEntity getAllRides() {
        return ResponseEntity.ok(this.rideRepository.findAll().stream().filter(Ride::isPublic).collect(Collectors.toList()));

    }


    @PutMapping("/{id}/makePublic")
    public ResponseEntity makeRoutePublic(@PathVariable String id) {
        String emailAddress = SecurityContextHolder.getContext().getAuthentication().getName();

        User u = this.userRepository.findByAccount_Username(emailAddress);
        Ride r = this.rideRepository.findById(id);

        boolean hasRide = false;

        for (Ride ur: u.getRides()) {
            if (ur.getId().equals(r.getId())) {
                hasRide = true;
            }
        }


        if (u == null || r == null || !hasRide) {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        r.setPublic(true);

        this.rideRepository.save(r);

        return ResponseEntity.ok(r);




    }


}

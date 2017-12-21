//package org.letsride.server.services;
//
//import org.letsride.server.models.User;
//import org.letsride.server.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//
//public class UserService {
//    private final UserRepository userRepository;
////    private final AccountRepository accountRepository;
//
//    @Autowired
//    public UserService(UserRepository userRepository/*, AccountRepository accountRepository*/) {
//        this.userRepository = userRepository;
////        this.accountRepository = accountRepository;
//    }
//
//
//    public User lookup(String username) {
//        return this.userRepository.findByAccount_Username(username);
//    }
//
//    public void save(User user) {
//        this.userRepository.save(user);
//    }
//
//    public boolean usernameExists(String username) {
////        return this.accountRepository.findByUsername(username) != null;
//        return this.userRepository.findByAccount_Username(username) != null;
//
//    }
//
//}

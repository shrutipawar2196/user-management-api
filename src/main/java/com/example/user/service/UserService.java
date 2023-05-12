package com.example.user.service;

import com.example.user.NotFoundException;
import com.example.user.repo.UserRepository;
import com.example.user.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userDao;
    public User createUser(User user){
        return userDao.save(user);
    }

    public User getUser(Long id) {
        Optional<User> optUser = userDao.findById(id);
        return optUser.orElse(null);
    }

    public List<User> getAll() {
        return new ArrayList<>(userDao.findAll());
    }

    public User findByUsername(String username){
        Optional<User> optUser = userDao.findByUsername(username);
        return optUser.orElseThrow();
    }

    @Transactional
    public User updateUser(String username, User user) {
        User currentUser = findByUsername(username);
        currentUser.setEmail( getOrDefault(user.getEmail(), currentUser.getEmail()) );
        currentUser.setUsername( getOrDefault(user.getUsername(), currentUser.getUsername()) );
        currentUser.setPassword( getOrDefault(user.getPassword(), currentUser.getPassword()) );

        userDao.updateUserById(
                currentUser.getId(),
                currentUser.getUsername(),
                currentUser.getPassword(),
                currentUser.getEmail()
        );

        return currentUser;
    }

    public void deleteUser(String username) throws NotFoundException {
        User currentUser = findByUsername(username);
        if(currentUser == null){
            throw new NotFoundException( String.format("user %s not found", username));
        }
        userDao.delete(currentUser);
    }

    private <T> T getOrDefault(T field, T defaultValue){
        if( !ObjectUtils.isEmpty(field) ){
            return field;
        }

        return defaultValue;
    }
}

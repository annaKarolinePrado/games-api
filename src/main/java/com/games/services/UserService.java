package com.games.services;

import com.games.DTO.UserDTO;
import com.games.exceptions.BussinesException;
import com.games.models.User;
import com.games.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<UserDTO> findUsersByEmail(String email) {
        List<User> users = userRepository.findByEmailContainingIgnoreCase(email);
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    public UserDTO updateUser(UserDTO user) {
        if (user.getId() == null) {
            throw new BussinesException("Campo id não informado.");
        }
        return saveUser(user);
    }

    public void updateUserLote(List<UserDTO> users) {
        users.forEach(user -> {
            if (user.getId() == null) {
                throw new BussinesException("Campo id não informado.");
            }
            saveUser(user);
        });
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUserLote(List<UserDTO> users) {
        users.forEach(user -> {
            if (user.getId() == null) {
                throw new BussinesException("Campo id não informado.");
            }
            deleteUserById(user.getId());
        });
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RestClientException("User not found with id " + id));
    }

    public UserDTO findDTOById(Long id) {
        return convertToDTO(userRepository.findById(id)
                .orElseThrow(() -> new RestClientException("User not found with id " + id)));
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setProfile(user.getProfile());
        userDTO.setActive(user.getActive());
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setProfile(userDTO.getProfile());
        user.setActive(userDTO.isActive());
        return user;
    }
}

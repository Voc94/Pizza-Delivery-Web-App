package com.pizzaDeliveryProject.delivery.service.impl;

import com.pizzaDeliveryProject.delivery.dto.UserDTO;
import com.pizzaDeliveryProject.delivery.dto.UserDTOWrapper;
import com.pizzaDeliveryProject.delivery.mappers.UserMapper;
import com.pizzaDeliveryProject.delivery.models.user.Role;
import com.pizzaDeliveryProject.delivery.models.user.User;
import com.pizzaDeliveryProject.delivery.repository.UserRepository;
import com.pizzaDeliveryProject.delivery.service.AdminService;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        user = userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setRole(Role.valueOf(userDTO.getRole()));
        // Handle restaurant assignment if necessary
        if (userDTO.getRestaurant() != null) {
            user.setRestaurant(userDTO.getRestaurant());
        }
        user = userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.userToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::userToUserDTO).collect(Collectors.toList());
    }

    @Override
    public String exportUsersAsXml() throws Exception {
        List<UserDTO> users = getAllUsers();
        JAXBContext context = JAXBContext.newInstance(UserDTOWrapper.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        UserDTOWrapper wrapper = new UserDTOWrapper();
        wrapper.setUsers(users);

        StringWriter sw = new StringWriter();
        marshaller.marshal(wrapper, sw);

        return sw.toString();
    }
}

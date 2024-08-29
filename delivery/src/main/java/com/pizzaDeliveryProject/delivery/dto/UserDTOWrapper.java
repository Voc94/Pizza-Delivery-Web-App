package com.pizzaDeliveryProject.delivery.dto;

import com.pizzaDeliveryProject.delivery.dto.UserDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "users")
public class UserDTOWrapper {

    private List<UserDTO> users;

    @XmlElement(name = "user")
    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}

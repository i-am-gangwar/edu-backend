package com.edubackend.service;


import com.edubackend.dto.UserDto;
import com.edubackend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private  UserRepo userRepo;
    public String getUserById(String id) {
        Optional<UserDto> userDto = userRepo.findById(id);
        if(userDto.isPresent() && userDto.get().getUsername()!=null){
            return userDto.get().getUsername();
        }
        else return id;
    }

    public String updateUser(UserDto userDto) throws Exception {
       try{
           userRepo.save(userDto);
       }
       catch (Exception e){
           throw new  Exception("unable to save new password");
       }
        return "try later";
    }
}
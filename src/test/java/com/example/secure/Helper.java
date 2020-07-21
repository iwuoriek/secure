package com.example.secure;

import com.example.secure.dto.FundsTransfer;
import com.example.secure.dto.UserCredentials;
import com.example.secure.dto.UserDto;

public class Helper {
    public static UserDto getAdminTestUser(){
        UserDto adminUser = new UserDto();
        adminUser.setFirstName("Tom");
        adminUser.setLastName("Mark");
        adminUser.setEmail("tommark@mail.com");
        adminUser.setUsername("tommark");
        adminUser.setPassword("password4Admin");
        adminUser.setAddress("100 Varsity Avenue");
        adminUser.setRoles("admin");
        return adminUser;
    }

    public static UserDto getCustomerTestUser(){
        UserDto customerUser = new UserDto();
        customerUser.setFirstName("Jane");
        customerUser.setLastName("Foster");
        customerUser.setEmail("janefoster@mail.com");
        customerUser.setUsername("janefoster");
        customerUser.setPassword("password4User");
        customerUser.setAddress("200 Varsity Avenue");
        customerUser.setRoles("customer");
        return customerUser;
    }

    public static UserDto getThirdWheelUser(){
        UserDto customerUser = new UserDto();
        customerUser.setFirstName("Erik");
        customerUser.setLastName("Von");
        customerUser.setEmail("erikvon@mail.com");
        customerUser.setUsername("erikvon");
        customerUser.setPassword("password4User");
        customerUser.setAddress("200 Varsity Avenue");
        customerUser.setRoles("customer");
        return customerUser;
    }

    public static UserCredentials getLoginCredentials(){
        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("erikvon");
        credentials.setPassword("password4User");
        return credentials;
    }

    public static FundsTransfer getFundsTransferInfo(long to, long from){
        FundsTransfer fundsTransfer = new FundsTransfer();
        fundsTransfer.setAccountToTransferTo(to);
        fundsTransfer.setAccountToTransferFrom(from);
        return fundsTransfer;
    }
}

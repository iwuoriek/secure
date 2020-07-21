package com.example.secure.helper;

import com.example.secure.entities.Role;
import com.example.secure.dto.AccountDto;
import com.example.secure.dto.UserDto;
import com.example.secure.dto.PublicUser;
import com.example.secure.entities.Account;
import com.example.secure.entities.User;

public class Convert {
    public static AccountDto toAccountDto(Account account){
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setAccountNumber(account.getAccountNumber());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }

    public static Account toAccount(AccountDto accountDto){
        Account account = new Account();
        account.setId(accountDto.getId());
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setAccountType(accountDto.getAccountType());
        account.setBalance(accountDto.getBalance());
        return account;
    }

    public static User toUser(UserDto userDto){
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public static PublicUser toPublicUser(User user){
        PublicUser userDto = new PublicUser();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        Role role = (Role) user.getRoles().toArray()[0];
        if (role.getRoleName().equals(RoleType.CUSTOMER.name())){
            user.getAccounts()
                    .forEach(account -> userDto.setAccounts(toAccountDto(account)));
        }
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}

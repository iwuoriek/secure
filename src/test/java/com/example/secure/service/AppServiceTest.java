package com.example.secure.service;

import com.example.secure.Helper;
import com.example.secure.dto.*;
import com.example.secure.entities.Account;
import com.example.secure.repository.AccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@Transactional
public class AppServiceTest {
    @Autowired
    private AppService service;
    @Autowired
    private AccountRepository repository;

    private static UserDto ADMINUSER;
    private static  UserDto CUSTOMERUSER;
    private static UserCredentials CREDENTIALS;

    @BeforeAll
    public static void createUser(){
        ADMINUSER = Helper.getAdminTestUser();
        CUSTOMERUSER = Helper.getCustomerTestUser();
        CREDENTIALS = Helper.getLoginCredentials();
    }

    @BeforeEach
    public void insertThirdWheel(){
        service.registerUser(Helper.getThirdWheelUser());
    }

    @Test
    public void happySignupTest() {
        Optional<String> adminMessage = service.registerUser(ADMINUSER);
        Assertions.assertEquals(adminMessage.get(), "success");
        Optional<String> customerMessage = service.registerUser(CUSTOMERUSER);
        Assertions.assertEquals(customerMessage.get(), "success");
    }

    @Test
    public void unhappySignupTest() {
        ADMINUSER.setUsername("erikvon");
        Optional<String> adminMessage = service.registerUser(ADMINUSER);
        Assertions.assertNotEquals(adminMessage.get(), "success");
        CUSTOMERUSER.setUsername("erikvon");
        Optional<String> customerMessage = service.registerUser(CUSTOMERUSER);
        Assertions.assertNotEquals(customerMessage.get(), "success");
    }

    @Test
    public void userLoginTest(){
        Optional<String> token1 = service.userLogin(CREDENTIALS);
        Assertions.assertTrue(token1.isPresent());
        CREDENTIALS.setUsername("errikvonn");
        Optional<String> token2 = service.userLogin(CREDENTIALS);
        Assertions.assertFalse(token2.isPresent());
    }

    @Test
    public void updateUserTest(){
        UserDto original = Helper.getThirdWheelUser();
        original.setFirstName("Henry");
        original.setLastName("Ford");
        Optional<PublicUser> updated = service.updateCustomer(original);
        Assertions.assertTrue(updated.isPresent());
        Assertions.assertEquals(updated.get().getFirstName(), "Henry");
        Assertions.assertEquals(updated.get().getLastName(), "Ford");
        Assertions.assertEquals(updated.get().getEmail(), original.getEmail());
    }

    @Test
    public void depositFundsTest(){
        List<Account> accounts = repository.findAll();
        FundsTransfer transfer = Helper.getFundsTransferInfo(
                        accounts.get(0).getAccountNumber(), 0);
        transfer.setFunds(500.0);
        AccountDto updated = service.makeDeposit(transfer).get();
        Assertions.assertEquals(updated.getBalance(), 500.0);
    }

    //This would be so much easier if I used an accountBuilder class
    @Test
    public void happyTransferFundsTest(){
        List<Account> accounts = repository.findAll();
        FundsTransfer transferDetails = Helper.getFundsTransferInfo(
                accounts.get(0).getAccountNumber(), 0);
        transferDetails.setFunds(500.0);
        AccountDto original = service.makeDeposit(transferDetails).get();

        transferDetails.setAccountToTransferFrom(accounts.get(0).getAccountNumber());
        transferDetails.setAccountToTransferTo(accounts.get(1).getAccountNumber());
        transferDetails.setFunds(250.0);
        AccountDto updated = service.makeTransfer(transferDetails).get();

        Assertions.assertNotEquals(updated.getBalance(), original.getBalance());
        Assertions.assertEquals(updated.getBalance(), (original.getBalance() - 250.0));
    }

    @Test()
    public void unhappyTransferFundsTest(){
        List<Account> accounts = repository.findAll();
        FundsTransfer transferDetails = Helper.getFundsTransferInfo(
                accounts.get(0).getAccountNumber(), 0);
        transferDetails.setFunds(500.0);
        service.makeDeposit(transferDetails);

        transferDetails.setAccountToTransferFrom(accounts.get(0).getAccountNumber());
        transferDetails.setAccountToTransferTo(accounts.get(1).getAccountNumber());
        transferDetails.setFunds(750.0);
        Exception exception = Assertions.assertThrows(UnsupportedOperationException.class, () -> service.makeTransfer(transferDetails));
        Assertions.assertEquals(exception.getMessage(), "Insufficient funds");
    }

    @Test()
    public void unhappyTransferFunds2Test(){
        List<Account> accounts = repository.findAll();
        FundsTransfer transferDetails = Helper.getFundsTransferInfo(
                accounts.get(0).getAccountNumber(), 0);
        transferDetails.setFunds(500.0);
        service.makeDeposit(transferDetails);

        transferDetails.setAccountToTransferFrom(accounts.get(0).getAccountNumber());
        transferDetails.setAccountToTransferTo(10203003);
        transferDetails.setFunds(250.0);
        Exception exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.makeTransfer(transferDetails));
        Assertions.assertEquals(exception.getMessage(), "Cannot find beneficiary account");
    }
}

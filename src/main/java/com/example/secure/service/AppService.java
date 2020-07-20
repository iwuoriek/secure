package com.example.secure.service;

import com.example.secure.auth.JwtProvider;
import com.example.secure.auth.Role;
import com.example.secure.dto.FundsTransfer;
import com.example.secure.dto.PublicUser;
import com.example.secure.dto.UserCredentials;
import com.example.secure.dto.UserDto;
import com.example.secure.entities.Account;
import com.example.secure.entities.User;
import com.example.secure.helper.AccountType;
import com.example.secure.helper.Convert;
import com.example.secure.helper.RoleType;
import com.example.secure.repository.AccountRepository;
import com.example.secure.repository.RoleRepository;
import com.example.secure.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;


@Service
@Transactional
public class AppService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final Logger logger = LoggerFactory.getLogger(AppService.class);

    @Autowired
    public AppService(UserRepository userRepository, AccountRepository accountRepository,
                      RoleRepository roleRepository, AuthenticationManager authenticationManager,
                      PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.encoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public Optional<String> registerUser(UserDto userDto) {
        Optional<String> message = Optional.empty();
        String password = encoder.encode(userDto.getPassword());
        if (!usernameOrEmailExists(userDto.getUsername(), userDto.getEmail())) {
            User user = Convert.toUser(userDto);
            user.setPassword(password);
            if (userDto.getRoles().equalsIgnoreCase("customer")) {
                Role role = roleRepository.findByRoleName(RoleType.CUSTOMER.name()).get();
                user.setRoles(role);

                Account savings = createAccount(AccountType.SAVINGS_ACCOUNT);
                savings.setUser(user);
                Account checking = createAccount(AccountType.CHECKING_ACCOUNT);
                checking.setUser(user);

                Set<Account> accounts = new HashSet<>();
                accounts.add(savings);
                accounts.add(checking);
                user.setAccounts(accounts);
            } else if (userDto.getRoles().equalsIgnoreCase("admin")) {
                Role role = roleRepository.findByRoleName(RoleType.ADMIN.name()).get();
                user.setRoles(role);
            }
            userRepository.save(user);
            message = Optional.of("success");
            logger.info("Successfully registered new user");
        } else {
            message = Optional.of("forbidden");
            logger.warn("Username or email already exists");
        }
        return message;
    }

    public Optional<String> userLogin(UserCredentials credentials) {
        Optional<String> token = Optional.empty();
        Optional<User> user = userRepository
                .findByUsername(credentials.getUsername());
        if (user.isPresent()) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
                token = Optional.of(jwtProvider.createToken(credentials.getUsername(), user.get().getRoles()));

            } catch (AuthenticationException e) {
                logger.warn("Invalid user login credentials");
            }
        }
        return token;
    }

    public List<PublicUser> findAllCustomers() {
        List<PublicUser> customerList = new ArrayList<>();
        userRepository.findAll()
                .forEach(user -> customerList.add(Convert.toPublicUser(user)));
        return customerList;
    }

    public void updateCustomer(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail()).get();
        user.setUsername(userDto.getUsername());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setPassword(userDto.getPassword());
        userRepository.save(user);
    }

    public void makeDeposit(FundsTransfer funds) {
        Account account = accountRepository.findByAccountNumber(funds.getAccountToTransferTo()).get();
        double newBalance = account.getBalance() + funds.getFunds();
        account.setBalance(newBalance);
        accountRepository.save(account);
    }

    public void makeTransfer(FundsTransfer funds) {
        Account transferFrom = accountRepository.findByAccountNumber(funds.getAccountToTransferTo()).get();
        Optional<Account> transferTo = accountRepository.findByAccountNumber(funds.getAccountToTransferTo());

        if (funds.getFunds() > transferFrom.getBalance()) throw new UnsupportedOperationException("Insufficient funds");
        if (!transferTo.isPresent()) throw new EntityNotFoundException("Cannot find beneficiary account");

        double newBalance = transferFrom.getBalance() - funds.getFunds();
        transferFrom.setBalance(newBalance);
        accountRepository.save(transferFrom);
        makeDeposit(new FundsTransfer(funds.getAccountToTransferTo(), funds.getFunds()));
    }

    private Account createAccount(AccountType type) {
        long number = 2_020_000 + new Random().nextInt(999);
        while (accountRepository.existsByAccountNumber(number)) {
            number = 2_020_000 + new Random().nextInt(999);
        }
        Account account = new Account();
        account.setAccountNumber(number);
        account.setAccountType(type.name());
        account.setBalance(0.00);
        return account;
    }

    private boolean usernameOrEmailExists(String username, String email) {
        return userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
    }
}

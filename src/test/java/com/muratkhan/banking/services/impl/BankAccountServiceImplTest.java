package com.muratkhan.banking.services.impl;
import com.muratkhan.banking.dto.AccountBalanceInfo;
import com.muratkhan.banking.model.BankAccount;
import com.muratkhan.banking.model.User;
import com.muratkhan.banking.repositories.BankAccountRepository;
import com.muratkhan.banking.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.lang.reflect.Field;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;



    @Test
    void transferMoneyByLogin_success() {
        // Arrange
        String fromUserLogin = "fromUser";
        String toUserLogin = "toUser";
        Double amount = 100.0;

        User fromUser = new User();
        User toUser = new User();
        BankAccount fromAccount = new BankAccount();
        fromAccount.setBalance(200.0);
        BankAccount toAccount = new BankAccount();
        toAccount.setBalance(50.0);

        fromUser.setBankAccount(fromAccount);
        toUser.setBankAccount(toAccount);

        when(userRepository.findByLogin(fromUserLogin)).thenReturn(fromUser);
        when(userRepository.findByLogin(toUserLogin)).thenReturn(toUser);
        when(userRepository.findById(fromUser.getId())).thenReturn(Optional.of(fromUser));
        when(userRepository.findById(toUser.getId())).thenReturn(Optional.of(toUser));

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        bankAccountService.transferMoneyByLogin(fromUserLogin, toUserLogin, amount);

        // Assert
        verify(userRepository).findByLogin(fromUserLogin);
        verify(userRepository).findByLogin(toUserLogin);
        verify(userRepository).save(fromUser);
        verify(userRepository).save(toUser);

        assertEquals(100.0, fromAccount.getBalance());
        assertEquals(150.0, toAccount.getBalance());

        verify(redisTemplate, times(2)).delete(anyString());
    }

    @Test
    void transferToDeposit_success() {
        // Arrange
        Long userId = 1L;
        Double amount = 100.0;

        User user = new User();
        user.setId(userId);
        BankAccount account = new BankAccount();
        account.setBalance(200.0);
        user.setBankAccount(account);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        bankAccountService.transferToDeposit(userId, amount);

        // Assert
        assertEquals(100.0, account.getBalance());
        assertEquals(100.0, account.getDepositBalance());
        assertEquals(100.0, account.getInitialDeposit());

        verify(bankAccountRepository).save(account);
        verify(redisTemplate).delete(anyString());
    }

    @Test
    void getAccountBalanceInfo_Cached() {
        // Arrange
        Long userId = 1L;
        String cacheKey = "accountBalance:" + userId;
        AccountBalanceInfo expectedBalanceInfo = new AccountBalanceInfo();

        // Stubbing RedisTemplate and ValueOperations
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(cacheKey)).thenReturn(expectedBalanceInfo);

        // Act
        AccountBalanceInfo actualBalanceInfo = bankAccountService.getAccountBalanceInfo(userId);

        // Assert
        assertNotNull(actualBalanceInfo);
        assertEquals(expectedBalanceInfo, actualBalanceInfo);

        // Verify interactions
        verify(redisTemplate).opsForValue();
        verify(valueOperations).get(cacheKey);
    }


    @Test
    void getAccountBalanceInfo_NotCached() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Long userId = 1L;
        String cacheKey = "accountBalance:" + userId;

        // Mocks
        RedisTemplate<String, AccountBalanceInfo> redisTemplate = mock(RedisTemplate.class);
        ValueOperations<String, AccountBalanceInfo> valueOperations = mock(ValueOperations.class);
        UserRepository userRepository = mock(UserRepository.class);


        // Using reflection to inject mocks
        Field userRepositoryField = bankAccountService.getClass().getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(bankAccountService, userRepository);

        Field redisTemplateField = bankAccountService.getClass().getDeclaredField("redisTemplate");
        redisTemplateField.setAccessible(true);
        redisTemplateField.set(bankAccountService, redisTemplate);

        User user = new User();
        BankAccount account = new BankAccount();
        account.setBalance(200.0);
        account.setDepositBalance(100.0);
        user.setBankAccount(account);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(cacheKey)).thenReturn(null); // Simulate cache miss
        when(userRepository.findById(userId)).thenReturn(Optional.of(user)); // User found

        // Act
        AccountBalanceInfo result = bankAccountService.getAccountBalanceInfo(userId);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(account.getBalance(), result.getBalance(), "Balance should match");
        assertEquals(account.getDepositBalance(), result.getDepositBalance(), "Deposit balance should match");

        // Verify caching logic was invoked due to cache miss
        verify(valueOperations).set(eq(cacheKey), any(AccountBalanceInfo.class));
    }

}


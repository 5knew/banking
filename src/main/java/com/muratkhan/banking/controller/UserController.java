package com.muratkhan.banking.controller;
import com.muratkhan.banking.dto.AccountBalanceInfo;
import com.muratkhan.banking.dto.DepositTransferRequest;
import com.muratkhan.banking.dto.TransferRequest;
import com.muratkhan.banking.dto.UserContactUpdateRequest;
import com.muratkhan.banking.model.User;
import com.muratkhan.banking.repositories.UserRepository;
import com.muratkhan.banking.services.BankAccountService;
import com.muratkhan.banking.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final BankAccountService bankService;
    private final UserRepository userRepository;
    @GetMapping
    public ResponseEntity<String> sayHello(){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok("Hi User: " + currentUser.getUsername());
    }

    @PatchMapping("/updateEmail")
    public ResponseEntity<?> updateEmail(@RequestBody String email) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long currentUserId = currentUser.getId();
            userService.updateUserEmail(currentUserId, email);
            return ResponseEntity.ok().body("User email updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/updatePhone")
    public ResponseEntity<?> updatePhone(@RequestBody String phone) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long currentUserId = currentUser.getId();
            userService.updateUserPhone(currentUserId, phone);
            return ResponseEntity.ok().body("User phone updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/deleteContactInfo")
    public ResponseEntity<?> deleteContactInfo(@RequestBody UserContactUpdateRequest request) {
        try {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long currentUserId = currentUser.getId();

            boolean emailToDelete = "DELETE".equalsIgnoreCase(request.getEmail());
            boolean phoneToDelete = "DELETE".equalsIgnoreCase(request.getPhone());

            if (!emailToDelete && !phoneToDelete) {
                throw new IllegalArgumentException("No contact information specified for deletion.");
            }

            userService.deleteContactInfo(currentUserId, emailToDelete, phoneToDelete);
            return ResponseEntity.ok().body("Contact information deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        bankService.transferMoneyByLogin(currentUsername, request.getToUserLogin(), request.getAmount());
        return ResponseEntity.ok().body("Transfer completed successfully");
    }

    @PostMapping("/transferToDeposit")
    public ResponseEntity<?> transferToDeposit(@RequestBody DepositTransferRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByLogin(username);

        bankService.transferToDeposit(currentUser.getId(), request.getAmount());
        return ResponseEntity.ok().body("Funds transferred to deposit successfully");
    }

    @GetMapping("/account/balance")
    public ResponseEntity<AccountBalanceInfo> getAccountBalance() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByLogin(username);

        AccountBalanceInfo balanceInfo = bankService.getAccountBalanceInfo(currentUser.getId());

        return ResponseEntity.ok(balanceInfo);
    }








}



package com.internal.api.controller;

import com.internal.api.entity.User;
import com.internal.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveUsers(@RequestParam(value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file: files) {
            userService.saveUser(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<ResponseEntity> findAllUsers() {
        return userService.findAllUsers().thenApply(ResponseEntity :: ok);
    }

    @GetMapping(value = "/getUsersByThread", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUsers() {
        CompletableFuture<List<User>> userTask1 = userService.findAllUsers();
        CompletableFuture<List<User>> userTask2 = userService.findAllUsers();
        CompletableFuture<List<User>> userTask3 = userService.findAllUsers();
        CompletableFuture.allOf(userTask1, userTask2, userTask3).join();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}

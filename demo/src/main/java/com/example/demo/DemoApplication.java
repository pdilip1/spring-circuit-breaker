package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableRetry(proxyTargetClass = true)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}


@RestController
class UserRestController{

    private final UserService userService;

    @Autowired
    public UserRestController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getnumber")
    public int getNumber(){
        return this.userService.getNumber();
    }

}

@Service
class UserService {

    @Recover
    public int fallback(RuntimeException re){
        System.out.println("fallback");
        return 2;
    }

    @CircuitBreaker (include = RuntimeException.class)
    public int getNumber(){
        System.out.println("getNumber");
        throw new RuntimeException();
    }
}

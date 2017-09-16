package it.italian.coders.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class FakeController {

    @RequestMapping(value="/public/api/hello", method= RequestMethod.GET)
    public ResponseEntity<?> publicHelloWorld(){
        return ResponseEntity.ok("ciao pubblico");
    }

    @RequestMapping(value="/protected/api/hello", method= RequestMethod.GET)
    public ResponseEntity<?> privateHelloWorld(){
        return ResponseEntity.ok("ciao privato");
    }

    @RequestMapping(value="/protected/api/hello", method= RequestMethod.POST)
    public ResponseEntity<?> privatePostHelloWorld(@RequestBody @Valid Person person){
        return ResponseEntity.ok("ciao privato");
    }
}

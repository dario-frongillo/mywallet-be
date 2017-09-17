package it.italian.coders.web.controller.fake;

import it.italian.coders.exception.RestException;
import org.springframework.http.HttpStatus;
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
        if(person.getName().equals("dao")){
            throw new RestException(HttpStatus.BAD_REQUEST,"PROVA.TITLE","PROVA.DETAIL",0);
        }
        return ResponseEntity.ok("ciao privato");
    }
}

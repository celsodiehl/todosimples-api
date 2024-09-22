package com.celsodiehl.todosimples.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.celsodiehl.todosimples.models.User;
import com.celsodiehl.todosimples.models.User.CreateUser;
import com.celsodiehl.todosimples.models.User.UpdateUser;
import com.celsodiehl.todosimples.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService uService;

    @GetMapping("/{id}") // no Get e delete não pode pasasr dados no RequestBody, só no create e update
    // Resonse Entity retorna Entidade de resposta do tipo Usuário
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User obj = this.uService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    @Validated(CreateUser.class)
    public ResponseEntity<Void> create(@Valid @RequestBody User obj) {
        this.uService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated(UpdateUser.class)               
    public ResponseEntity<Void> update(@Valid @RequestBody User obj, @PathVariable Long id){
        obj.setId(id);
        this.uService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.uService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

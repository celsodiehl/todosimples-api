package com.celsodiehl.todosimples.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.celsodiehl.todosimples.models.User;
import com.celsodiehl.todosimples.models.dto.UserCreateDTO;
import com.celsodiehl.todosimples.models.dto.UserUpdateDTO;
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
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj) {
        User user = this.uService.fromDTO(obj);
        User newUser = this.uService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")             
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable Long id){
        obj.setId(id);
        User user = this.uService.fromDTO(obj);
        this.uService.update(user);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.uService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

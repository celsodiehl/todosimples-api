package com.celsodiehl.todosimples.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.celsodiehl.todosimples.repositories.UserRepository;
import com.celsodiehl.todosimples.models.User;

@Service
public class UserService {

    // CONSTRUTOR do Service
    @Autowired
    private UserRepository uRepository;

    public User findById(Long id) {
        Optional<User> user = this.uRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException(
                "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional
    public User create(User obj) {
        obj.setId(null);
        return obj;
    }

    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        return this.uRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.uRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Não é Possível excluir, possui Relacionamentos!");
        }
        
    }
}

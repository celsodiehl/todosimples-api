package com.celsodiehl.todosimples.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.celsodiehl.todosimples.repositories.UserRepository;
import com.celsodiehl.todosimples.security.UserSpringSecurity;
import com.celsodiehl.todosimples.services.exceptions.AuthorizationException;
import com.celsodiehl.todosimples.services.exceptions.DataBindingViolationException;
import com.celsodiehl.todosimples.services.exceptions.ObjectNotFoundException;

import com.celsodiehl.todosimples.models.ProfileEnum;
import com.celsodiehl.todosimples.models.User;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // CONSTRUTOR do Service, injeção de dependência
    @Autowired
    private UserRepository uRepository;

    public User findById(Long id) {
        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
            throw new AuthorizationException("Acesso Negado!");

        Optional<User> user = this.uRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional
    public User create(User obj) {
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        obj = this.uRepository.save(obj);
        return obj;
    }

    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.uRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.uRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é Possível excluir, possui Relacionamentos!");
        }
    }

    // Usuário Logado
    public static UserSpringSecurity authenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

}

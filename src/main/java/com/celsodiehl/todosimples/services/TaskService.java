package com.celsodiehl.todosimples.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.celsodiehl.todosimples.models.ProfileEnum;
import com.celsodiehl.todosimples.models.Task;
import com.celsodiehl.todosimples.models.User;
import com.celsodiehl.todosimples.repositories.TaskRepository;
import com.celsodiehl.todosimples.security.UserSpringSecurity;
import com.celsodiehl.todosimples.services.exceptions.AuthorizationException;
import com.celsodiehl.todosimples.services.exceptions.DataBindingViolationException;
import com.celsodiehl.todosimples.services.exceptions.ObjectNotFoundException;


@Service
public class TaskService {

    @Autowired
    private TaskRepository tRepository;

    @Autowired
    private UserService uService;

    public Task findById(Long id) {
        Task task = this.tRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)|| !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");

        return task;
    }

    public List<Task> findAllByUser() {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        List<Task> tsk = this.tRepository.findByUser_Id(userSpringSecurity.getId());
        return tsk;
    }

    @Transactional
    public Task create(Task obj) {

        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        User use = this.uService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(use);
        obj = this.tRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.tRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.tRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é Possível excluir, possui Relacionamentos!");
        }
    }

    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }

}

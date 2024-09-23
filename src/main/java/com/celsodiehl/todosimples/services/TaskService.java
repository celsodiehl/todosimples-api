package com.celsodiehl.todosimples.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.celsodiehl.todosimples.models.Task;
import com.celsodiehl.todosimples.models.User;
import com.celsodiehl.todosimples.repositories.TaskRepository;
import com.celsodiehl.todosimples.services.exceptions.DataBindingViolationException;
import com.celsodiehl.todosimples.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository tRepository;

    @Autowired
    private UserService uService;

    public Task findById(Long id){
        Optional<Task> tk = this.tRepository.findById(id);
        return tk.orElseThrow(() -> new ObjectNotFoundException(
                "Tarefa não encontrada! Id: " + id + ", Tipo: " + Task.class.getName()));
    }

    public List<Task> findAllByUserId(Long userId){
        List<Task> tsk = this.tRepository.findByUser_Id(userId);
        return tsk;
    }

    @Transactional
    public Task create(Task obj){
        User use = this.uService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(use);
        obj = this.tRepository.save(obj);
        return obj;
    }

    public Task update(Task obj){
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return this.tRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        try {
            this.tRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é Possível excluir, possui Relacionamentos!");
        }
    }
    
}

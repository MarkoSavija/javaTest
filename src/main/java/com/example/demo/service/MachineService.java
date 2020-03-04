package com.example.demo.service;

import com.example.demo.model.Machine;
import com.example.demo.model.MachineStatus;
import com.example.demo.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class MachineService {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private UserService userService;

    public void createMachine(Machine machine){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        machine.setCreatedBy(userService.findOne(principal.toString()));
        machine.setCreatedAt(LocalDateTime.now());
        machine.setStatus(MachineStatus.STOPPED);
        machine.setActive(true);
        machineRepository.save(machine);
    }

    public void startMachine(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElse(null);
        Random random = new Random();
        int randomSeconds = random.nextInt(6) + 10;
        try{
            Thread.sleep(randomSeconds*1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        MachineStateMachine.makeStateTransition(machine, MachineStateMachine.Event.START);
        machineRepository.save(machine);
    }

    public void stopMachine(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElse(null);
        machine.setStatus(MachineStatus.STOPPED);
        machineRepository.save(machine);
        Random random = new Random();
        int randomSeconds = random.nextInt(6) + 10;
        try{
            Thread.sleep(randomSeconds*1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

    }
}

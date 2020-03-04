package com.example.demo.service;

import com.example.demo.model.Machine;
import com.example.demo.model.MachineStatus;
import com.example.demo.repository.MachineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import javax.transaction.Transactional;

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

    @Transactional
    public void startMachine(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElse(null);
        int randomSeconds = generateRandomSeconds(10, 15);
        try{
            Thread.sleep(randomSeconds*1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        MachineStateMachine.makeStateTransition(machine, MachineStateMachine.Event.START);
        machineRepository.save(machine);
    }

    @Transactional
    public void stopMachine(Long machineId) {
        Machine machine = machineRepository.findById(machineId).orElse(null);
        int randomSeconds = generateRandomSeconds(5, 10);
        try{
            Thread.sleep(randomSeconds*1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        MachineStateMachine.makeStateTransition(machine, MachineStateMachine.Event.STOP);
        
        /*
         * In assignment text it was stated that machine can be stopped only if it is in RUNNING state,
         * and that it goes to RUNNING state after it is stopped after 5-10 seconds. It didn't make sense
         * to me, but I still implemented it in that way. 
         */

    }
    
    @Transactional
    public void restartMachine(Long machineId) {
      Machine machine = machineRepository.findById(machineId).orElse(null);
      machineRepository.save(machine);
      int randomSeconds = generateRandomSeconds(5, 10);
      try{
          Thread.sleep(randomSeconds*1000/2);
          machine.setStatus(MachineStatus.STOPPED);
          Thread.sleep(randomSeconds*1000/2);
          machine.setStatus(MachineStatus.RUNNING);
      } catch (InterruptedException ie) {
          Thread.currentThread().interrupt();
      }
    }
    
    public void destroyMachine(Long machineId) {
      Machine machine = machineRepository.findById(machineId).orElse(null);
      if(machine.getStatus().equals(MachineStatus.STOPPED)) {
        machine.setActive(false);
        machineRepository.save(machine);
      }
    }
    
    private int generateRandomSeconds(int min, int max) {
      Random random = new Random();
      return random.nextInt(max + 1 - min) + min;
    }
    
    public List<Machine> search(String name, List<MachineStatus> status, Date dateFrom, Date dateTo){
      return machineRepository.search(name, status, dateFrom, dateTo);
    }
    
}

package com.example.demo.controller;

import com.example.demo.model.Machine;
import com.example.demo.service.MachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "machines")
public class MachineController {

    @Autowired
    private MachineService machineService;

    @PostMapping
    public void create(@RequestBody Machine machine){
        machineService.createMachine(machine);
    };

    @GetMapping(value = "/start")
    public void start(@RequestParam Long machineId){
        machineService.startMachine(machineId);
    }

    @GetMapping(value = "/stop")
    public void stop(@RequestParam Long machineId){
        machineService.startMachine(machineId);
    }
    
    @GetMapping(value = "/restart")
    public void restart(@RequestParam Long machineId){
        machineService.restartMachine(machineId);
    }
    
    @GetMapping(value = "/destroy")
    public void destroy(@RequestParam Long machineId){
        machineService.destroyMachine(machineId);
    }

    


}

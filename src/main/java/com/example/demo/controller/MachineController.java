package com.example.demo.controller;

import com.example.demo.model.Machine;
import com.example.demo.model.MachineStatus;
import com.example.demo.service.MachineService;
import java.sql.Date;
import java.util.List;
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

    @GetMapping(value = "/search")
    public List<Machine> searchMachines(@RequestParam String name, @RequestParam List<MachineStatus> status, 
        @RequestParam Date dateFrom, @RequestParam Date dateTo){
        return machineService.search(name, status, dateFrom, dateTo);
    } 


}

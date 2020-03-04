package com.example.demo.service;

import java.util.EnumSet;

import com.example.demo.model.Machine;
import com.example.demo.model.MachineStatus;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class MachineStateMachine {
  
  private static final String EVENT_NOT_ACCEPTED_ERROR_MESSAGE_TEMPLATE =
      "Event %s not accepted, machine in state %s";
  
  public static void makeStateTransition(Machine machine, Event event) {
    StateMachine<MachineStatus, Event> machineStateMachine;
    try {
      machineStateMachine = buildMachineStateMachine(machine);
    } catch (Exception e) {
      throw new StateMachineException(e.getMessage());
    }

    machineStateMachine.start();
    boolean stateMachineEventAccepted = machineStateMachine.sendEvent(event);
    machineStateMachine.stop();

    if (!stateMachineEventAccepted) {
      throw new RuntimeException(
          String.format(EVENT_NOT_ACCEPTED_ERROR_MESSAGE_TEMPLATE, event,
              machine.getStatus()));
    }
  }
  
  private static StateMachine<MachineStatus, Event> buildMachineStateMachine(Machine machine)
      throws Exception {
    // @formatter:off
    Builder<MachineStatus, Event> builder = StateMachineBuilder.builder();
    builder.configureStates()
        .withStates().states(EnumSet.allOf(MachineStatus.class))
        .initial(machine.getStatus());
    builder.configureTransitions()
        .withExternal().source(MachineStatus.STOPPED).target(MachineStatus.RUNNING).event(Event.START).and()
        .withExternal().source(MachineStatus.RUNNING).target(MachineStatus.RUNNING).event(Event.STOP).and()
        .withExternal().source(MachineStatus.RUNNING).target(MachineStatus.STOPPED).event(Event.RESTART).and();

    StateMachine<MachineStatus, Event> machineStateMachine = builder.build();
    machineStateMachine.stop();
    machineStateMachine.addStateListener(new StateMachineListenerAdapter<MachineStatus, Event>() {

      @Override
      public void stateChanged(State<MachineStatus, Event> from, State<MachineStatus, Event> to) {
        machine.setStatus(to.getId());
      }

    });

    return machineStateMachine;
  }

  enum Event {
    START, STOP, RESTART, CREATE, DESTROY, SEARCH
  }

}

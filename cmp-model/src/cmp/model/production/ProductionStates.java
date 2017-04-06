/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmp.model.production;

/**
 *
 * @author adrianohrl
 */
public enum ProductionStates {
    STARTED, RESTARTED, PAUSED, FINISHED, RETURNED;
    
    public boolean isStartingState() {
        return this == STARTED || this == RESTARTED;
    }
    
    public boolean isFinishingState() {
        return this == FINISHED || this == RETURNED;
    }
    
    public boolean isStarted() {
        return this == STARTED;
    }
    
    public boolean isRestarted() {
        return this == RESTARTED;
    }
    
    public boolean isPaused() {
        return this == PAUSED;
    }
    
    public boolean isFinished() {
        return this == FINISHED;
    }
    
    public boolean isReturned() {
        return this == RETURNED;
    }
    
}
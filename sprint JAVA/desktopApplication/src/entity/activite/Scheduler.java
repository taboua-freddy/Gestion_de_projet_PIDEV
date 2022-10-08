/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.activite;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Freddy
 */
public class Scheduler {
    private Timer timer;
    private String identifiant;

    public Scheduler() {
    }
    
    public Scheduler(String identifiant) {
        /*if(this.identifiant.equals(identifiant))
        {
            timer.cancel();
            timer = null;
        }
        this.identifiant = identifiant;*/
        timer = new Timer();
    }
   
    public void planJob(TimerTask run,Long delay) 
    {
        timer.schedule(run,0, delay);
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
    


    @Override
    protected void finalize() throws Throwable {
        super.finalize(); 
        //timer.cancel();
    }

    
}

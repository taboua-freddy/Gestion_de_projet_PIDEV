package entity.activite;


import FXML.controller.HomeController;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.NotIdentifiableEvent;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.DateBuilder.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Freddy
 */
public class Sheduler {
    private JobDetail jobDetail;
    private Trigger trigger;
    private Scheduler scheduler;

    public Sheduler() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            if(!scheduler.isStarted())
                scheduler.start();
        } catch (SchedulerException ex) {
            Logger.getLogger(Sheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    public static void main(String[] args) throws SchedulerException {
       
        
        Trigger t = TriggerBuilder.newTrigger().withIdentity("CroneTrigger")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever()).build();
        
       Sheduler s = new Sheduler();
        
        
        String identifiant="q";
        LocalDateTime date= LocalDateTime.now().plusSeconds(10);
        LocalDateTime fin= date.plusSeconds(3);
        //s.planJob(testJotb.class, identifiant, date,fin);
        
    }
    
    public void planJob(Class aClass,String identifiant,LocalDateTime dateDebut,LocalDateTime dateFin)
    {
        try {

            Date debut = dateOf(dateDebut.getHour(), dateDebut.getMinute(), dateDebut.getSecond(), dateDebut.getDayOfMonth(), dateDebut.getMonthValue(), dateDebut.getYear());
            Date fin = dateOf(dateFin.getHour(), dateFin.getMinute(), dateFin.getSecond(), dateFin.getDayOfMonth(), dateFin.getMonthValue(), dateFin.getYear());
            Trigger t1 = TriggerBuilder.newTrigger().withIdentity(identifiant).startAt(debut).build();
            JobDetail j  = JobBuilder.newJob(aClass).build();
            
            if(!scheduler.checkExists(t1.getKey()))
            scheduler.scheduleJob(j,t1);
            else
            {
                scheduler.clear();
                scheduler.scheduleJob(j,t1);
            }
            
        } catch (SchedulerException ex) {
            Logger.getLogger(Sheduler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    
   public static class testJotb implements Job{

        @Override
        public void execute(JobExecutionContext jec) throws JobExecutionException {
            
            System.err.println("Job doing");
        }
    
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize(); 
        scheduler.shutdown();
    }

    
    
    
}

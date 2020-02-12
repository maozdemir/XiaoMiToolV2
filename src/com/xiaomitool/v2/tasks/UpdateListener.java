package com.xiaomitool.v2.tasks;

import com.xiaomitool.v2.logging.Log;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UpdateListener {
    private List<OnStart> onStarts = new ArrayList<>();
    private List<OnUpdate> onUpdates = new ArrayList<>();
    private List<OnFinished> onFinisheds = new ArrayList<>();
    private List<OnError> onErrors = new ArrayList<>();
    public UpdateListener(){

    }
    public UpdateListener(OnStart onStart, OnUpdate onUpdate, OnFinished onFinished, OnError onError){
       addOnStart(onStart);
       addOnUpdate(onUpdate);
      addOnFinished(onFinished);
      addOnError(onError);
    }

    public void onStart(long totalSize){
        onStarts.sort(Comparator.comparingInt(o -> o.priority));
        for (OnStart toRun : onStarts){
            toRun.run(totalSize);
        }
    }
    public void onUpdate(long downloaded, long totalSize, Duration latestDuration, Duration totalDuration){
        for (OnUpdate toRun : onUpdates){
            toRun.run(downloaded,totalSize,latestDuration,totalDuration);
        }
    }
    public void onFinished(Object subject){
        onFinisheds.sort(Comparator.comparingInt(o -> o.priority));
        for (OnFinished toRun : onFinisheds){
            toRun.run(subject);
        }
    }
    public void onError(Exception e){
        onErrors.sort(Comparator.comparingInt(o -> o.priority));
        for (OnError toRun : onErrors){
            toRun.run(e);
        }
    }
    public static abstract class OnStart {
        public int priority = 5;
        public OnStart(){

        }
        public OnStart(int priority){
            this.priority = priority;
        }
        public abstract void run(long totalSize);
    }
    public static interface OnUpdate {
        public void run(long downloaded, long totalSize, Duration latestDuration, Duration totalDuration);
    }
    public static abstract class OnFinished {
        public int priority = 5;
        public OnFinished(){

        }
        public OnFinished(int priority){
            this.priority = priority;
        }
        public abstract void run(Object subject);
    }
    public static abstract class OnError {
        public int priority = 5;
        public OnError(){

        }
        public OnError(int priority){
            this.priority = priority;
        }
        public abstract void run(Exception e);
    }
    public void addOnStart(OnStart toRun){
        if (toRun == null){
            return;
        }
        onStarts.add(toRun);
    }
    public void addOnUpdate(OnUpdate toRun){
        if (toRun == null){
            return;
        }
        onUpdates.add(toRun);
    }
    public void addOnFinished(OnFinished toRun){
        if (toRun == null){
            return;
        }
        onFinisheds.add(toRun);
    }
    public void addOnError(OnError toRun){
        if (toRun == null){
            return;
        }
        onErrors.add(toRun);
    }

    public static class Debug extends AdvancedUpdateListener {
        public Debug() {
            super(750);
            addOnAdvancedUpdate((downloaded, totalSize, currentSpeed, averageSpeed, missingTime) -> {
                /*Log.debug("["+downloaded+"/"+totalSize+"] ("+(100*downloaded/totalSize)+"%) "+currentSpeed.toString()+" - "+averageSpeed.toString()+", missing "+missingTime.getQuantity()+" "+missingTime.getUnit().toString());*/
            });
            addOnStart(new OnStart() {
                @Override
                public void run(long totalSize) {
                    /*Log.debugLine();*/
                    /*Log.debug("Starting task, size: " + totalSize);*/
                    /*Log.debugLine();*/
                }
            });
            addOnFinished(new OnFinished() {
                @Override
                public void run(Object subject) {
                    /*Log.debugLine();*/
                    /*Log.debug("Task finished: " + subject.toString());*/
                    /*Log.debugLine();*/
                }
            });
            addOnError(new OnError() {
                @Override
                public void run(Exception e) {
                    /*Log.debugLine();*/
                    /*Log.debug("Task error: "+e.getMessage());*/
                    /*Log.debug(e.getStackTrace()[0].getFileName()+" : "+e.getStackTrace()[0].getLineNumber());*/
                    /*Log.debugLine();*/
                }
            });
        }

    }
}



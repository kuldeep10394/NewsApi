package Deadlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class DeadlockExample {

    AtomicInteger atomicInteger = new AtomicInteger(10);
    AtomicInteger atomicInteger1 = new AtomicInteger(1);
    public synchronized void printEven(){
       while(atomicInteger1.get()<=atomicInteger.get()){
           if(atomicInteger1.get()%2==1){
               try {
                   wait();
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
           else{
               System.out.println("Even thread :"+atomicInteger1.getAndIncrement());
               notifyAll();
           }
       }
    }
    public synchronized void printOdd(){
        while(atomicInteger1.get()<=atomicInteger.get()){
            if(atomicInteger1.get()%2==0){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                System.out.println("Odd thread :"+atomicInteger1.getAndIncrement());
                notifyAll();
            }

        }
    }
    public static void main(String args[]) throws InterruptedException {

        DeadlockExample deadlockExample = new DeadlockExample();
        Thread thread1 = new Thread(deadlockExample::printOdd);
        Thread thread = new Thread(deadlockExample::printEven);
        thread1.start();
        thread.start();

    }
}

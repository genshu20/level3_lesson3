package lesson5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class MainClass {
    public static final int CARS_COUNT = 4 ;
    public static final CountDownLatch START=new CountDownLatch(9);
    public static final Thread[] CARS=new Thread[CARS_COUNT];
    public static void main (String[] args) {

        Race race = new Race( new Road( 60 ), new Tunnel(), new Road( 40 ));
        Car[] cars = new Car[CARS_COUNT];
        for ( int i = 0 ; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + ( int ) (Math.random() * 10 ),START);
        }

        System.out.println( "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!" );
        for ( int i = 0 ; i < cars.length; i++) {
           CARS[i]= new Thread(cars[i]);
           CARS[i].start();
        }
        while (START.getCount()>1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println( "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!" );
        START.countDown();
        for (Thread t:CARS) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println( "ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!" );
    }
}






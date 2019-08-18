package lesson5;

import java.util.concurrent.CountDownLatch;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0 ;
    }
    private Race race;
    private int speed;
    private String name;
    private CountDownLatch cdl;
    public static volatile boolean win;
    public String getName () {
        return name;
    }
    public int getSpeed () {
        return speed;
    }
    public Car (Race race, int speed,CountDownLatch cdl) {
        this .race = race;
        this .speed = speed;
        this.cdl=cdl;
        CARS_COUNT++;
        this .name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run () {
        try {
            System.out.println( this .name + " готовится" );
            cdl.countDown();
            Thread.sleep( 500 + ( int )(Math.random() * 800 ));
            System.out.println( this .name + " готов" );
            cdl.countDown();
            cdl.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for ( int i = 0 ; i < race.getStages().size(); i++) {
            race.getStages().get(i).go( this );
        }
        if(!win){
            win=true;
            System.out.println(this.getName()+" WIN");
        }
    }
}

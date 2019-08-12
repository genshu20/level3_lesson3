package ABC;

public class Main {
    private volatile char currentLater = 'C';

    public static void main(String[] args) {
        Main main = new Main();
        Thread tA=new Thread(()->main.printLetter('C','A'));
        Thread tB=new Thread(()->main.printLetter('A','B'));
        Thread tC=new Thread(()->main.printLetter('B','C'));
        tA.start();
        tB.start();
        tC.start();
    }

    public synchronized void printLetter(char letter1, char letter2) {
        try {
        for (int i = 0; i < 5; i++) {
            while (currentLater != letter1) this.wait();
            System.out.print(letter2);
            currentLater=letter2;
            this.notifyAll();
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}



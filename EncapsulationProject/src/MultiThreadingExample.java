public class MultiThreadingExample {

    public static void main(String args[]){

        Dinner husband = new Dinner("husband",true);
        Dinner wife = new Dinner("wife",true);
        Spoon s = new Spoon(husband);

        new Thread(new Runnable() {
            @Override
            public void run() {
                husband.eatsWith(s,wife);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                wife.eatsWith(s,husband);
            }
        }).start();
    }
}

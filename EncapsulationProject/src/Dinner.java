public class Dinner {
    private String name;
    private boolean isHungry;

    public Dinner(String name, boolean isHungry){
        this.isHungry=isHungry;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHungry() {
        return isHungry;
    }

    public void setHungry(boolean hungry) {
        isHungry = hungry;
    }
    public void eatsWith(Spoon spoon, Dinner spouse){

        while (true){

            if(spoon.getOwner()!=this){
                try{
                   Thread.sleep(1000);
                } catch (InterruptedException e) {
                    continue;
                }continue;
            }
            if(spouse.isHungry){

                System.out.println(String.format("%s you eat first my darling %s!",name,spouse.getName()));
                spoon.setOwner(spouse);
                continue;
            }
            isHungry = false;
            spoon.use();
            System.out.println(String.format("%s i am stuffed darling %s!",name,spouse.getName()));
            spoon.setOwner(spouse);
        }
    }
}

public class Spoon {
    private  Dinner owner;
    Spoon(Dinner dinner){
        owner=dinner;
    }
    public  Dinner getOwner(){
        return owner;
    }
    public synchronized void setOwner(Dinner d){
        owner = d;
    }
    public synchronized void use(){
        System.out.println(String.format("%s has eaten",owner.getName()));
    }
}

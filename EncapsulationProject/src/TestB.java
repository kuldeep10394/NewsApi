import java.util.HashMap;
import java.util.Map;

public class TestB extends TestA{

    String a;
    Integer a1;
    TestB (){

    }
    TestB (String a, Integer a1){
       this.a =a;
       this.a1=a1;
    }
    void sound() {
        System.out.println("Dog barks");
    }
    public static void main(String args[]){

        TestB testA = new TestB();

        testA.sound();
        Map<String , String> map = new HashMap<>();
        map.put("one","ten");
        map.get("one");
    }
}

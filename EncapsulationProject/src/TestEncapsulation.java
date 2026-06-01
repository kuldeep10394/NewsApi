import java.math.BigDecimal;
import java.util.List;

public class TestEncapsulation {

    private BigDecimal balance = BigDecimal.ZERO;

    public void withDraw(BigDecimal amount){

        if(amount.signum()<=0){
            throw new IllegalArgumentException("Invalid Amount");
        }
        if(balance.compareTo(amount)<0){
            throw new IllegalArgumentException("Insufficient Funds");
        }
        balance.subtract(amount);
    }
    public void depositMoney(BigDecimal amount){
        validateAmount(amount);
        balance = balance.add(amount);
    }
    public void validateAmount(BigDecimal amount){
        List<String> list = List.of("A", "B", "C");
        if(amount==null|| amount.signum()<=0){
            throw new IllegalArgumentException("Deposit Amount should be positive");
        }
    }
}

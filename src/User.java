import Utils.Counter;

import java.util.Random;

/**
 * Created by young on 2017/4/29.
 */
public class User implements Runnable{

    private Random random=new Random();
    private Service service;

    public User( Service service) {
        this.service=service;
    }

    @Override
    public void run() {
        //每人抢购数量
        int amount=random.nextInt(10)+1;  //[1.00,11.00)  => [0,10]
        //抢购结果

        boolean success=false;
        try{
            success=service.buy(amount);
        }catch (Exception e){
            if(e.getMessage().startsWith("timeout")){
                Counter.addTimeoutCount(1);
            }
        }
        if(success){
            Counter.addSuccessInventoryCount(amount);
            Counter.addSuccessUsersCount(1);
        }else{
            Counter.addFailUsersCount(1);
        }
    }
}

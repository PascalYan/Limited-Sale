import Utils.Counter;
import Utils.Log;

/**
 * Created by young on 2017/4/29.
 */
public class Service {

    public static final String inventoryKey = "inventory";

    private Redis redisClient;

    /**
     * 业务初始化设置库存
     */
    public void init(Redis redis,int inventory) {
        redisClient=redis;
        /**
         * 设置库存inventory
         */
        Log.info("业务初始化设置库存:"+inventory);
        redisClient.set(inventoryKey, inventory);
    }

    /**
     * @param amount 抢购数量
     * @return 抢购成功或失败
     */
    public boolean buy(int amount) {
        //冻结库存
        int remian = redisClient.decr(inventoryKey, amount);
        Counter.addFreezeInventoryCount(amount);
        Log.info(Thread.currentThread().getName() + " 原子减 decr 冻结库存:" + amount + "  ,剩余库存:" + remian);
        //剩余库存大于0 则抢购成功
        if (remian > 0) {
            Log.info("剩余库存>0 " + Thread.currentThread().getName() + " 抢购成功");
            return true;
        } else {
            //抢购不成功，释放库存
            remian = redisClient.incr(inventoryKey, amount);
            Counter.addUnfreezeUsersCount(amount);
            Log.info("剩余库存<=0 " + Thread.currentThread().getName() + " 抢购失败,原子加 incr 释放冻结库存:" + amount + "  ,剩余库存:" + remian);
            return false;
        }
    }
}

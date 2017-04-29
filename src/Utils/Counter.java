package Utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by young on 2017/4/29.
 */
public class Counter {
    /*
     数据统计
    */
    public static AtomicInteger successInventoryCount = new AtomicInteger();
    public static AtomicInteger failUsersCount = new AtomicInteger();
    public static AtomicInteger successUsersCount = new AtomicInteger();
    public static AtomicInteger freezeInventoryCount = new AtomicInteger();
    public static AtomicInteger timeoutCount = new AtomicInteger();


    public static int addSuccessInventoryCount(int inventory){
        return successInventoryCount.addAndGet(inventory);
    }
    public static int addFailUsersCount(int inventory){
        return failUsersCount.addAndGet(inventory);
    }
    public static int addSuccessUsersCount(int inventory){
        return successUsersCount.addAndGet(inventory);
    }
    public static int addFreezeInventoryCount(int inventory){
        return freezeInventoryCount.addAndGet(inventory);
    }
    public static int addUnfreezeUsersCount(int inventory){
        return freezeInventoryCount.addAndGet(-inventory);
    }
    public static int addTimeoutCount(int nums){
        return timeoutCount.addAndGet(nums);
    }


    public static AtomicInteger getSuccessInventoryCount() {
        return successInventoryCount;
    }

    public static AtomicInteger getFailUsersCount() {
        return failUsersCount;
    }

    public static AtomicInteger getSuccessUsersCount() {
        return successUsersCount;
    }

    public static AtomicInteger getFreezeInventoryCount() {
        return freezeInventoryCount;
    }

    public static AtomicInteger getTimeoutCount() {
        return timeoutCount;
    }
}

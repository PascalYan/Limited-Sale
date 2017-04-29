import Utils.AsyncTimeout;
import Utils.ClientWrapper;
import Utils.Counter;

import java.util.Random;

/**
 * Created by young on 2017/4/29.
 */
public class Test {
    private static Random random = new Random();
    private static final String inventoryKey = "inventory";
    private static final int inventory = 1000;

    public static void main(String[] args) {


        //活动开始时间
        final long startTime = System.currentTimeMillis();
        //活动持续时间millinSeconds
        final long actTime = 10000l;

        Service service = new Service();
        Redis redisClient = new Redis();
        //初始化业务及库存
        service.init(redisClient, inventory);

        AsyncTimeout timeoutProccessor = new AsyncTimeout();

        //成功抢购数量<业务库存 && 活动期间内，持续抢购...
        int i = 0;
        while (Counter.successInventoryCount.get() < inventory && (System.currentTimeMillis() - startTime < actTime)) {
            i++;
            Thread client = new Thread(new User(service), "user-" + i);
            client.start();
            timeoutProccessor.regist(new ClientWrapper(client, 400));
        }

        /*
        count result
         */
        Thread shutdownHook = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("抢购持续时间:" + (System.currentTimeMillis() - startTime) + "ms");
                System.out.println("活动设置商品数量:" + inventory);
                System.out.println("抢购成功商品数量:" + Counter.getSuccessInventoryCount());
                System.out.println("redis活动结束剩余商品数量:" + redisClient.get(inventoryKey));
                System.out.println("活动结束仍冻结商品数量:" + Counter.getFreezeInventoryCount());
                System.out.println("抢购失败用户数量:" + Counter.getFailUsersCount());
                System.out.println("抢购超时失败用户数量:" + Counter.getTimeoutCount());
                System.out.println("抢购成功用户数量:" + Counter.getSuccessUsersCount());
            }
        });
        shutdownHook.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        // Start async timeout thread
        Thread timeoutThread = new Thread(timeoutProccessor, "AsyncTimeout-thread");
//        timeoutThread.setPriority(threadPriority);
        timeoutThread.setDaemon(true);
        timeoutThread.start();
    }
}

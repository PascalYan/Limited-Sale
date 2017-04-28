import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by young on 2017/4/29.
 */
public class Test {
    private static Random random=new Random();
    private static final String inventoryKey = "inventory";
    private static final int inventory = 1000;

    public static void main(String[] args) {
        /*
        数据统计
         */
        AtomicInteger successInventoryCount=new AtomicInteger();
        AtomicInteger failUsersCount=new AtomicInteger();
        AtomicInteger successUsersCount=new AtomicInteger();

        //活动开始时间
        final long startTime=System.currentTimeMillis();
        //活动持续时间millinSeconds
        final long actTime=10000l;

        Service service=new Service();
        Redis redisClient=new Redis();
        //初始化业务及库存
        service.init(redisClient,inventory);

        //成功抢购数量<业务库存 && 活动期间内，持续抢购...
        int i=0;
        while(successInventoryCount.get()<inventory&&(System.currentTimeMillis()-startTime<actTime)){
            i++;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int amount=random.nextInt(10)+1;  //[1.00,11.00)  => [0,10]
                    boolean success=service.buy(amount);
                    if(success){
                        successInventoryCount.addAndGet(amount);
                        successUsersCount.addAndGet(1);
                    }else{
                        failUsersCount.addAndGet(1);
                    }
                }
            },"user-"+i).start();
        }

        Thread shutdownHook=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("抢购持续时间:"+(System.currentTimeMillis()-startTime)+"ms");
                System.out.println("活动设置商品数量:"+inventory);
                System.out.println("抢购成功商品数量:"+successInventoryCount);
                System.out.println("活动结束剩余商品数量:"+redisClient.get(inventoryKey));
                System.out.println("抢购失败用户数量:"+failUsersCount);
                System.out.println("抢购成功用户数量:"+successUsersCount);
            }
        });
        shutdownHook.setDaemon(true);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}

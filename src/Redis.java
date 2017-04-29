import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by young on 2017/4/29.
 */
public class Redis {
    private static final int costMillisconds = 100;



    /**
     * valueMap
     */
    private Map<String, AtomicInteger> valueMap = new HashMap<>();

    /**
     * 原子增
     *
     * @param key
     * @param value
     * @return
     */
    public int incr(String key, int value) {
        redisAndNetCost();
        return this.valueMap.get(key).addAndGet(value);
    }

    /**
     * 原子减
     *
     * @param key
     * @param value
     * @return
     */
    public int decr(String key, int value) {
        redisAndNetCost();
        return this.valueMap.get(key).addAndGet(-value);
    }

    public void set(String key, int value) {
        redisAndNetCost();
        this.valueMap.put(key, new AtomicInteger(value));
    }

    public int get(String key) {
        redisAndNetCost();
        return this.valueMap.get(key).get();
    }

    private void redisAndNetCost() {
        try {
            Thread.sleep(costMillisconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("timeout ...");
        }
    }

}

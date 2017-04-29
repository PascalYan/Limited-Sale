package Utils;

/**
 * Created by young on 2017/4/29.
 */
public class ClientWrapper {
    private Thread client;
    private long access;
    private long timeout;

    public ClientWrapper(Thread client,long timeout) {
        this.client = client;
        this.access = System.currentTimeMillis();
        this.timeout=timeout;
    }

    public void timeout(){
        client.interrupt();
    }

    public long getAccess() {
        return access;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}

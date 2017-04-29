package Utils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Async timeout thread
 */
public class AsyncTimeout implements Runnable {

    private volatile boolean asyncTimeoutRunning = true;

    private Queue<ClientWrapper> waitingRequests = new LinkedList<>();

    /**
     * The background thread that checks async requests and fires the
     * timeout if there has been no activity.
     */
    @Override
    public void run() {
        ClientWrapper client;
        // Loop until we receive a shutdown command
        while (asyncTimeoutRunning) {
            if ((client = waitingRequests.peek()) != null) {
                long access = client.getAccess();
                long now = System.currentTimeMillis();
                if (client.getTimeout() > 0 && (now - access) > client.getTimeout()) {
                    // Prevent multiple timeouts
                    client.setTimeout(-1);
                    //超时
                    client.timeout();
                    waitingRequests.remove(client);
                }
            }
        }
    }


    protected void stop() {
        asyncTimeoutRunning = false;
    }

    public void regist(ClientWrapper clientWrapper) {
        waitingRequests.add(clientWrapper);
    }
}

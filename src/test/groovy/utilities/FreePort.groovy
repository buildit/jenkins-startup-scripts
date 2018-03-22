package utilities;

import java.util.concurrent.ThreadLocalRandom;

class FreePort {
    static int nextFreePort(int from, int to) {
        int port = randPort(from, to)
        while (true) {
            if (isLocalPortFree(port)) {
                println("Found free port: ${port}")
                return port
            } else {
                port = ThreadLocalRandom.current().nextInt(from, to)
            }
        }
    }

    private static int randPort(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }

    private static boolean isLocalPortFree(int port) {
        try {
            new ServerSocket(port).close()
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

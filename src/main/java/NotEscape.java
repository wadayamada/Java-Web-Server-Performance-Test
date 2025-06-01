import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class NotEscape {

    private static final int RUNS       = 1000;     // 計測回数
    private static final int CHUNK_CNT  = 50_000;  // 生成バッファ数
    private static final int CHUNK_SIZE = 1_024;   // 1 バッファのサイズ
    private static final int ARR_LEN   = 128;

    /*----------------- heavy part -----------------*/
    private static String buildLargeResponse() {
        List<byte[]> bufs = new ArrayList<>(CHUNK_CNT);
        for (int i = 0; i < CHUNK_CNT; i++) {
            bufs.add(new byte[CHUNK_SIZE]);        // ヒープ確保
        }
        parse(bufs);                               // ダミー解析
        return "done";
    }

    private static void parse(List<byte[]> _bufs) {
        // 実際は CSV 解析や I/O など
    }

    static class Item {
        long[] data = new long[ARR_LEN];               // 128 * 8 byte
    }

    /*------------ heavy part ----------------------*/
    private static long buildLargeResponseItem() {
        long total = 0;
        for (int i = 0; i < CHUNK_CNT; i++) {
            Item it = new Item();                      // ヒープ確保
            for (int j = 0; j < ARR_LEN; j++) {
                it.data[j] = j;                        // ダミー書き込み
            }
            total += it.data[0];
        }
        return total;
    }

    /*----------------- benchmark ------------------*/
    public static void main(String[] args) {
        double total = 0.0;

        for (int i = 1; i <= RUNS; i++) {
            long start = System.nanoTime();
            long res = buildLargeResponseItem();
            double sec = (System.nanoTime() - start) / 1_000_000_000.0;
            total += sec;
            System.out.printf("Run %3d: %.6f seconds (%s)%n", i, sec, res);
            Runtime runtime = Runtime.getRuntime();
            long totalMem = runtime.totalMemory();
            long freeMem = runtime.freeMemory();
            long usedMem = totalMem - freeMem;
            long maxMem = runtime.maxMemory();
            NumberFormat format = NumberFormat.getInstance();
            System.out.println("start Max Memory:   " + format.format(maxMem / (1024.0 * 1024.0)));
            System.out.println("start Used Memory:  " + format.format(usedMem / (1024.0 * 1024.0)));
        }
        System.out.printf("Average over %d runs: %.6f seconds%n",
                          RUNS, total / RUNS);
    }
}

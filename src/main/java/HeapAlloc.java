import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class HeapAlloc {

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
        return "done";
    }


    /*----------------- benchmark ------------------*/
    public static void main(String[] args) {
        double total = 0.0;

        for (int i = 1; i <= RUNS; i++) {
            long start = System.nanoTime();
            String res = buildLargeResponse();
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

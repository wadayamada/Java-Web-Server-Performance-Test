import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;

public class PerseHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) throws InterruptedException {
        // HTTPのレスポンスを生成
        final FullHttpResponse response = new DefaultFullHttpResponse(
                req.protocolVersion(),
                HttpResponseStatus.OK
        );
        // レスポンスボディを設定
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory();
        long freeMem = runtime.freeMemory();
        long usedMem = totalMem - freeMem;
        long maxMem = runtime.maxMemory();
        NumberFormat format = NumberFormat.getInstance();
        System.out.println("start Max Memory:   " + format.format(maxMem / (1024.0 * 1024.0)));
        System.out.println("start Used Memory:  " + format.format(usedMem / (1024.0 * 1024.0)));

        String bodyText = processLargePayload();

        response.content().writeBytes("aaa\n".getBytes());
        // ヘッダを設定
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH,
                                  response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        // クライアントへ書き込み＆フラッシュ
//        ctx.executor().schedule(() -> {
//            // 実際のレスポンス送出処理
//            // 例えば文字列レスポンスを返すなら
        ctx.writeAndFlush(response);
//        }, 100, TimeUnit.MILLISECONDS);

        runtime = Runtime.getRuntime();
        totalMem = runtime.totalMemory();
        freeMem = runtime.freeMemory();
        usedMem = totalMem - freeMem;
        maxMem = runtime.maxMemory();
        format = NumberFormat.getInstance();
        System.out.println("end Max Memory:   " + format.format(maxMem / (1024.0 * 1024.0)));
        System.out.println("end Used Memory:  " + format.format(usedMem / (1024.0 * 1024.0)));
    }

    // ------------------ heavy part --------------------
    private static String processLargePayload() {
        // 50,000 × 1 KiB のバッファ確保
        List<byte[]> bufs = new ArrayList<>(CHUNK_COUNT);
        for (int i = 0; i < CHUNK_COUNT; i++) {
            bufs.add(new byte[CHUNK_SIZE]);
        }
        System.out.println("perse");

        // 解析フェーズ（ダミー）
        parse(bufs);

        return "done";
    }

    private static void parse(List<byte[]> _bufs) {
        // 実アプリでは CSV パースなどを行う
    }

    private static final int CHUNK_COUNT = 50_000;
    private static final int CHUNK_SIZE  = 1_024;

}

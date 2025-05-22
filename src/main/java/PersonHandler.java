import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;

public class PersonHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        // HTTPのレスポンスを生成
        final FullHttpResponse response = new DefaultFullHttpResponse(
                req.protocolVersion(),
                HttpResponseStatus.OK
        );
        // レスポンスボディを設定
        response.content().writeBytes("Hello from Netty!\n".getBytes());
        // ヘッダを設定
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH,
                                  response.content().readableBytes());

        // クライアントへ書き込み＆フラッシュし、接続を閉じる
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

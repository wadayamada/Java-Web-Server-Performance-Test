import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        // ボスグループ(accept用)とワーカーグループ（リクエスト処理用）を作成
        final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // ServerBootstrapを設定
            final ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class) // NIOサーバーソケット
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) {
                     // パイプラインにハンドラを登録
                     final ChannelPipeline p = ch.pipeline();

                     // HTTPリクエスト/レスポンスのエンコード・デコードを扱うハンドラ
                     p.addLast(new HttpServerCodec());
                     // HTTPリクエストを一つのフルメッセージにまとめる（POSTボディなど扱う場合に便利）
                     p.addLast(new HttpObjectAggregator(65536));

                     // 自作ハンドラ(簡単に"Hello from Netty!"を返す)
                     p.addLast(new PersonHandler());
                 }
             })
             // キューのバックログサイズやチャンネルのオプションを指定する例
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            // ポートを指定してサーバーをバインドして起動
            final ChannelFuture f = b.bind(port).sync();
            System.out.println("Netty HTTP server started on port " + port);

            // サーバーソケットが閉じられるまで待機
            f.channel().closeFuture().sync();
        } finally {
            // シャットダウン処理
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final int port = 8081;
        new Server(port).run();
    }
}

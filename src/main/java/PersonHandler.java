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
        final var person = getPerson();
        response.content().writeBytes((person.name + '\n' + person.age + '\n' + person.description + '\n').getBytes());
        // ヘッダを設定
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH,
                                  response.content().readableBytes());

        // クライアントへ書き込み＆フラッシュし、接続を閉じる
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public static class Person {
        String name;
        int age;
        String description;

        public Person(String name, int age, String description) {
            this.name = name;
            this.age = age;
            this.description = description;
        }
    }

    public static Person getPerson() {
        return new Person(
                "Takamichi Wada",
                26,
                "ぎおんしょうじゃのかねのこえ、しょぎょうむじょうのひびきあり。 " +
                "さらそうじゅのはなのいろ、 じょうしゃひっすいのことわりをあらわす。 " +
                "おごれるひともひさしからず、 ただはるのよのゆめのごとし。 " +
                "たけきものもついにはほろびぬ、 ひとえにかぜのまえのちりにおなじ。" +
                "とおくいちょうをとぶらえば、 しんのちょうこう、かんのおうもう、りょうのしゅうい、とうのろくさん、 "
                +
                "これらはみな、きゅうしゅせんこうのまつりごとにもしたがわず、 たのしみをきわめ、いさめをもおもいいれず、 "
                +
                "てんかのみだれんことをさとらずして、 みんかんのうれうるところをしらざつしかば、 ひさしからずしてぼうじにしものどもなり。"
                +
                "ちかくほんちょうをうかがうに、 じょうへいのまさかど、てんぎょうのすみとも、こうわのぎしん、へいじののぶより、 "
                +
                "これらはおごれるこころも、たけきこともみなとりどりにこそありしかども、まぢかくは、 " +
                "ろくはらのにゅうどう、さきのだいじょうだいじん、たいらのあそんきよもりこうともうししひとのありさま、 "
                +
                "つたえうけたまわるこそ、こころもことばもおよばれね。" +
                "そのせんぞをたずぬればかんむてんのうだいごのおうじ、 いっぽんしきぶきょうかずらはらしんのうくだいのこういん、さぬきのかみまさもりがまご、 "
                +
                "ぎょうぶきょうただもりのあそんのちゃくなんなり。 かのしんのうのみこ、たかみのおう、むかんむいにしてうせたまいぬ。 "
                +
                "そのみこ、たかもちのおうのとき、はじめてへいのしょうをたまわって、 かずさのすけになりたまいしより、たちまちにおうしをいでてじんしんにつらなる。 "
                +
                "そのこちんじゅふのしょうぐんよしもち、のちにはくにかとあらたむ。 くにかよりまさもりにいたるろくだいは、しょこくのずりょうたりしかども、 "
                +
                "てんじょうのせんせきをばいまだゆるされず。"
        );
    }
}

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

public class PersonHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest req) {
        // HTTPのレスポンスを生成
        final FullHttpResponse response = new DefaultFullHttpResponse(
                req.protocolVersion(),
                HttpResponseStatus.OK
        );
        // レスポンスボディを設定
        final Runtime runtime = Runtime.getRuntime();
        final long totalMem = runtime.totalMemory();
        final long freeMem = runtime.freeMemory();
        final long usedMem = totalMem - freeMem;
        final long maxMem = runtime.maxMemory();
        final NumberFormat format = NumberFormat.getInstance();
        System.out.println(" Max Memory:   " + format.format(maxMem / (1024.0 * 1024.0)));
        System.out.println(" Used Memory:  " + format.format(usedMem / (1024.0 * 1024.0)));
        final var person = getPerson();
        response.content().writeBytes(
                (person.name + '\n' + person.age + '\n' + person.description + '\n').getBytes());
        for (var i = 0; i < person.friends.size(); i++) {
            final var friend = person.friends.get(i);
            response.content().writeBytes(
                    (friend.name + '\n' + friend.age + '\n' + friend.description + '\n').getBytes());
        }
        // ヘッダを設定
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH,
                                  response.content().readableBytes());
        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        // クライアントへ書き込み＆フラッシュ
        ctx.writeAndFlush(response);
    }

    public static class Person {
        String name;
        int age;
        String description;
        List<Person> friends;

        public Person(String name, int age, String description, List<Person> friends) {
            this.name = name;
            this.age = age;
            this.description = description;
            this.friends = friends;
        }
    }

    public static Person getPerson() {
        final var frieds = new ArrayList<Person>();
        for (var i = 0; i < 5; i++) {
            frieds.add(new Person(
                    "友達" + i,
                    20 + i,
                    "Hello, my name is Oliver Green, and I'd like to share a bit about myself! I'm a 29-year-old software enthusiast with a passion for designing efficient systems and learning about emerging technologies. I grew up in a suburban neighborhood, surrounded by friendly neighbors and an active local community. As a child, I spent countless hours tinkering with computers, exploring coding challenges, and creating small programs that helped automate tedious tasks. Over time, this curiosity evolved into a full-blown interest in problem-solving, which motivated me to pursue a degree in computer science.\n"
                    + '\n'
                    + "In my spare time, I enjoy hiking through scenic trails, reading thought-provoking books, and cooking experimental dishes in my cozy kitchen. I also love attending local meetups where fellow developers gather to chat about everything from robust backend infrastructures to user-friendly interface design. One of my biggest aspirations is to contribute to open-source projects that empower people from various backgrounds to learn programming skills.\n"
                    + '\n'
                    + "I consider persistence, adaptability, and a sense of humor my strongest assets. If a project hits a roadblock, I relish the challenge of diagnosing the issue and iterating on potential solutions until the bug is squashed. Working in collaborative teams energizes me, and I appreciate the chance to exchange ideas that push our work closer to brilliance. Ultimately, I believe in continuous learning, embracing new experiences, and striving to create tools that can benefit communities both large and small.",
                    null
            ));
        }

        final var person = new Person(
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
                "てんじょうのせんせきをばいまだゆるされず。",
                frieds
        );
        return person;
    }
}

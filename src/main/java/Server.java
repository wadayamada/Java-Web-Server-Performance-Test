import java.io.IOException;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class Server {
    public static void main(String[] args)
            throws Exception {
        final org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(8081);

        // コンテキストハンドラを作成
        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // HelloServletを "/hello" にマッピング
        context.addServlet(new ServletHolder(new PersonServlet()), "/");

        // サーバー開始
        server.start();
        System.out.println("Jetty started on port 8081. Access http://localhost:8081/");

        // メインスレッドをブロックして待機
        server.join();
    }

    // シンプルなServletの例
    public static class PersonServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            final var person = getPerson();
            resp.setContentType("text/plain; charset=UTF-8");
            resp.getWriter().println(person.name + ' ' + person.age + ' ' + person.description);
        }
    }

    public static Person getPerson() {
        return new Person();
    }

    public static class Person {
        String name;
        int age;
        String description;

        public Person() {
            name = "WadaTakamichi";
            age = 26;
            description = "ぎおんしょうじゃのかねのこえ、しょぎょうむじょうのひびきあり。 " +
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
                          "てんじょうのせんせきをばいまだゆるされず。";
        }
    }
}

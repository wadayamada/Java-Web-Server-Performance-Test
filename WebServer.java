import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class WebServer {
    public static void main(String[] args) {

        // ポート8080で待ち受け
        try (ServerSocket serverSocket = new ServerSocket(8081)) {
            System.out.println("Server started on port 8081");

            while (true) {
                // クライアントからの接続を待ち受ける
                final Socket clientSocket = serverSocket.accept();

                // 新しいスレッドでリクエスト処理
                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                // 入出力ストリームを開く
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            // リクエスト行(例: "GET / HTTP/1.1")を読み込み
            final String requestLine = in.readLine();
            System.out.println("Request: " + requestLine);

            // リクエストヘッダを読み飛ばし (空行まで読み、この例では無視)
            String header;
            while ((header = in.readLine()) != null && !header.isEmpty()) {
                System.out.println("Header: " + header);
            }

            final var person = getPerson();
            System.out.println("Person: " + person);

            // 簡単なレスポンスを返却
            final String body = person.name + ' ' + person.age;
            final String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                        "Content-Type: text/html; charset=UTF-8\r\n" +
                                        "Content-Length: " + body.getBytes().length + "\r\n" +
                                        "Connection: close\r\n" +
                                        "\r\n" +
                                        body;

            out.write(httpResponse.getBytes());
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 接続を閉じる
            try {
                clientSocket.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public static Person getPerson() {
        return new Person();
    }

    public static class Person {
        String name;
        int age;

        public Person() {
            name = "John Doe";
            age = 30;
        }
    }
}

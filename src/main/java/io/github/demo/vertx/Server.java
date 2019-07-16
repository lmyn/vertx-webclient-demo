package io.github.demo.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;

/**
 * @author Mr. Luo
 */
public class Server {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.createHttpServer(new HttpServerOptions().setKeyStoreOptions(new JksOptions()
                .setPath("server-keystore.jks")
                .setPassword("wibble"))
                .setSsl(true)
        ).requestHandler(req -> req.response().end(getBigContent())).listen(8443, listenResult -> {
            if (listenResult.failed()) {
                System.out.println("Could not start HTTP server");
                listenResult.cause().printStackTrace();
            } else {
                System.out.println("Server started");
            }
        });
    }

    public static String getBigContent() {
        StringBuilder sb = new StringBuilder(100 * 50000);
        for (int i = 0; i < 50000; i++) {
            sb.append("xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"");
        }
        return sb.toString();
    }
}

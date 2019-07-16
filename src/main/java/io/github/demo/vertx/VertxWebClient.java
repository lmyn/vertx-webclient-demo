package io.github.demo.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author Mr. Luo
 */
public class VertxWebClient {

    WebClient client;

    @Before
    public void setup() {
        Vertx vertx = Vertx.vertx();
        this.client = WebClient.create(vertx,
                new WebClientOptions()
                        .setSsl(true)
                        .setTrustStoreOptions(new JksOptions()
                                .setPath("client-truststore.jks")
                                .setPassword("wibble")));
    }

    @Test
    public void single() throws InterruptedException {
        long start, end;
        start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(1);
        client.get(8443, "localhost", "/")
                .send(ar -> {
                    if (ar.succeeded()) {
                        HttpResponse<Buffer> response = ar.result();
                        String body = response.bodyAsString();
                        latch.countDown();
                        System.out.println("Got HTTP response with status " + response.statusCode());
                    } else {
                        ar.cause().printStackTrace();
                    }
                });
        latch.await();
        end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    @Test
    public void multi() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            single();
        }
    }
}

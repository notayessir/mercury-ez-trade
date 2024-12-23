package com.notayessir.stream.spot.client;

import okhttp3.*;
import okio.ByteString;

import java.util.concurrent.TimeUnit;

public class WebsocketClient extends WebSocketListener {



    private void run() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0,  TimeUnit.MILLISECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://127.0.0.1:38084/stream-service/spot/public-api/v1/quote")
                .build();
        client.newWebSocket(request, this);

        // Trigger shutdown of the dispatcher's executor so this process exits immediately.
        client.dispatcher().executorService().shutdown();
    }


    @Override public void onOpen(WebSocket webSocket, Response response) {
        int i = 10;
        while (i > 0){
            i--;
            webSocket.send("Hello...");
            webSocket.send("...World!");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        webSocket.close(1000, "Goodbye, World!");
    }

    @Override public void onMessage(WebSocket webSocket, String text) {
        System.out.println("MESSAGE: " + text);
    }

    @Override public void onMessage(WebSocket webSocket, ByteString bytes) {
        System.out.println("MESSAGE: " + bytes.hex());
    }

    @Override public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        System.out.println("CLOSE: " + code + " " + reason);
    }

    @Override public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    public static void main(String... args) {
        new WebsocketClient().run();
    }



}

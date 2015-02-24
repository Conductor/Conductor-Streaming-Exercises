package com.springapp.demo;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.reactivex.netty.channel.ObservableConnection;
import io.reactivex.netty.client.ClientMetricsEvent;
import io.reactivex.netty.metrics.MetricEventsListener;
import io.reactivex.netty.protocol.http.UnicastContentSubject;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;
import rx.Subscription;
import rx.subjects.ReplaySubject;

/**
 * Created by tmeehan on 2/22/15.
 */
class MockClient implements HttpClient<ByteBuf, ByteBuf> {

    private final UnicastContentSubject<ByteBuf> content;

    public MockClient(UnicastContentSubject<ByteBuf> content) {
        this.content = content;
    }

    @Override
    public Observable<HttpClientResponse<ByteBuf>> submit(HttpClientRequest<ByteBuf> request) {
        ReplaySubject<HttpClientResponse<ByteBuf>> replaySubject = ReplaySubject.create();
        replaySubject.onNext(new HttpClientResponse<ByteBuf>(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK), content));
        return replaySubject;
    }

    @Override
    public Observable<HttpClientResponse<ByteBuf>> submit(HttpClientRequest<ByteBuf> request, ClientConfig config) {
        ReplaySubject<HttpClientResponse<ByteBuf>> replaySubject = ReplaySubject.create();
        replaySubject.onNext(new HttpClientResponse<ByteBuf>(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK), content));
        return replaySubject;
    }

    /**
     * Creates exactly one new connection for every subscription to the returned observable.
     *
     * @return A new obserbvable which creates a single connection for every connection.
     */
    @Override
    public Observable<ObservableConnection<HttpClientResponse<ByteBuf>, HttpClientRequest<ByteBuf>>> connect() {
        return null; // nop
    }

    /**
     * Shutdown this client.
     */
    @Override
    public void shutdown() {
        //nop
    }

    /**
     * A unique name for this client.
     *
     * @return A unique name for this client.
     */
    @Override
    public String name() {
        return "testingClient";
    }

    @Override
    public Subscription subscribe(MetricEventsListener<? extends ClientMetricsEvent<?>> listener) {
        return null; // nop
    }
}
package com.springapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springapp.demo.model.FoursquarePathBuilder;
import com.springapp.demo.model.generated.Explore;
import io.netty.buffer.ByteBuf;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.pipeline.ssl.DefaultFactories;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by tmeehan on 2/19/15.
 */
public class FoursquareQueryer {
    public static final String FOURSQUARE_HOST = "api.foursquare.com";
    public static final int FOURSQUARE_PORT = 443;

    private final ObjectMapper mapper;
    private final HttpClient<ByteBuf, ByteBuf> rxClient;

    public FoursquareQueryer() {
        this(RxNetty.<ByteBuf, ByteBuf>newHttpClientBuilder(FOURSQUARE_HOST, FOURSQUARE_PORT)
                .withSslEngineFactory(DefaultFactories.trustAll())
                .enableWireLogging(LogLevel.DEBUG)
                .build());
    }

    protected FoursquareQueryer(HttpClient<ByteBuf, ByteBuf> rxClient) {
        this.rxClient = rxClient;
        mapper = new ObjectMapper();
    }

    public Observable<Explore> getStream(final FoursquarePathBuilder builder) {
        return Observable.create(new Observable.OnSubscribe<Explore>() {
            @Override
            public void call(final Subscriber<? super Explore> subscriber) {
                subscriber.onStart();

                Explore firstResult = getPaginatedExploreObject(builder, 0);
                subscriber.onNext(firstResult);

                for (int i = 1; i < firstResult.getResponse().getTotalResults(); i++) {
                    subscriber.onNext(getPaginatedExploreObject(builder, i));
                }

                subscriber.onCompleted();
            }
        });
    }

    protected Explore getPaginatedExploreObject(final FoursquarePathBuilder builder, final int i) {
        return rxClient.submit(HttpClientRequest.createGet(builder.setOffset(i).setLimit(1).build()))
                .flatMap(new Func1<HttpClientResponse<ByteBuf>, Observable<ByteBuf>>() {
                    @Override
                    public Observable<ByteBuf> call(HttpClientResponse<ByteBuf> response) {
                        return response.getContent();
                    }
                })
                .map(new Func1<ByteBuf, Explore>() {
                    @Override
                    public Explore call(ByteBuf data) {
                        try {
                            return mapper.readValue(data.toString(Charset.forName("UTF8")), Explore.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .toBlocking()
                .first();
    }
}

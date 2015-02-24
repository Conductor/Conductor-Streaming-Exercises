package com.springapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.springapp.demo.model.FoursquarePathBuilder;
import com.springapp.demo.model.generated.Explore;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.server.HttpServer;
import io.reactivex.netty.protocol.http.server.HttpServerBuilder;
import io.reactivex.netty.protocol.http.server.HttpServerRequest;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import io.reactivex.netty.protocol.http.server.RequestHandler;
import io.reactivex.netty.server.RxServerThreadFactory;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import rx.Observable;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FoursquareQueryerTest {
    private FoursquareQueryer queryer;

    private static HttpServer<ByteBuf, ByteBuf> server;
    private static int port;
    private static ObjectMapper objectMapper;

    @BeforeClass
    public static void init() {
        objectMapper = new ObjectMapper();
    }

    @Before
    public void setUp() throws Exception {
        // Create an in-memory RxNetty server
        HttpServerBuilder<ByteBuf, ByteBuf> builder
                = new HttpServerBuilder<ByteBuf, ByteBuf>(new ServerBootstrap().group(new NioEventLoopGroup(10, new RxServerThreadFactory())), port, new RequestHandler<ByteBuf, ByteBuf>() {
            private int i = 0;
            @Override
            public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
                i++;
                return response.writeStringAndFlush(getJsonContents(String.format("page%d.json", i)));
            }
        });
        server = builder.enableWireLogging(LogLevel.ERROR).build();
        server.start(); // Start accepting connections
        port = server.getServerPort(); // store the port for safekeeping, so we can use our client against it

        HttpClient<ByteBuf, ByteBuf> client = RxNetty.createHttpClient("localhost", port);
        queryer =  new FoursquareQueryer(client);
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown(); // Kill after each test
    }

    /********************************************************************************************************************
     *                                                                                                                  *
     * Exercises                                                                                                        *
     *                                                                                                                  *
     *******************************************************************************************************************/

    /**
     * Exercise 1: modify the stream so that it only streams recommended places in Manhattan
     */
    @Test
    public void filterStreamOnlyInNewYork() {
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Exercise 2: count the number of recommended places that are in Brooklyn
     */
    @Test
    public void countNumberEntriesInBrooklyn() {
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Exercise 3: modify the stream so that it returns just the places to explore, none of the other information
     * (i.e., strip out the Explore/Response/Group objects and just return the inner Items)
     */
    @Test
    public void getJustTheItemsInExploreEntries() {
        throw new RuntimeException("Not implemented yet");
    }

    /**
     * Exercise 4: each Explore object is streamed out with just one inner Item object.  Aggregate each Explore object into one,
     * combining their inner Items.
     */
    @Test
    public void aggregateStreamItems() {
        throw new RuntimeException("Not implemented yet");
    }


    /********************************************************************************************************************
     *                                                                                                                  *
     * End Exercises                                                                                                    *
     *                                                                                                                  *
     *******************************************************************************************************************/

    @Test
    public void testGetStream() throws Exception {
        Observable<Explore> stream = queryer.getStream(FoursquarePathBuilder.fromExplorerEndpoint().setLatLon(1, 1));

        List<Explore> events = Lists.newArrayList(stream.toBlocking().toIterable());
        assertEquals(10, events.size());

        Explore event1 = events.get(0);
        Explore event2 = events.get(1);
        Explore event3 = events.get(2);

        // Assert what's the same between each response
        assertTrue(event1.getResponse().getHeaderFullLocation().equals(event2.getResponse().getHeaderFullLocation()) && event2.getResponse().getHeaderFullLocation().equals(event3.getResponse().getHeaderFullLocation()));
        assertTrue(event1.getResponse().getHeaderLocation().equals(event2.getResponse().getHeaderLocation()) && event2.getResponse().getHeaderLocation().equals(event3.getResponse().getHeaderLocation()));
        assertTrue(event1.getResponse().getHeaderLocationGranularity().equals(event2.getResponse().getHeaderLocationGranularity()) && event2.getResponse().getHeaderLocationGranularity().equals(event3.getResponse().getHeaderLocationGranularity()));
        assertTrue(event1.getResponse().getSuggestedBounds().equals(event2.getResponse().getSuggestedBounds()) && event2.getResponse().getSuggestedBounds().equals(event3.getResponse().getSuggestedBounds()));
        assertTrue(event1.getResponse().getSuggestedFilters().equals(event2.getResponse().getSuggestedFilters()) && event2.getResponse().getSuggestedFilters().equals(event3.getResponse().getSuggestedFilters()));
        assertTrue(event1.getResponse().getSuggestedRadius().equals(event2.getResponse().getSuggestedRadius()) && event2.getResponse().getSuggestedRadius().equals(event3.getResponse().getSuggestedRadius()));
        assertTrue(event1.getResponse().getTotalResults().equals(event2.getResponse().getTotalResults()) && event2.getResponse().getTotalResults().equals(event3.getResponse().getTotalResults()));
        assertEquals(event1.getResponse().getTotalResults(), Long.valueOf(10));
        assertTrue(event1.getNotifications().equals(event2.getNotifications()) && event2.getNotifications().equals(event3.getNotifications()));

        // Assert the contents match and are in the correct order
        for (int i = 0; i < 10; i++) {
            assertJsonContentsEqual(events.get(i), String.format("page%d.json", i+1));
        }
    }

    /**
     * Given the test input jsonContents, assert that it is identical with the provided explore object
     * @param explore
     * @param jsonContents
     */
    public static void assertJsonContentsEqual(Explore explore, String jsonContents) throws Exception {
        assertEquals(explore, objectMapper.readValue(getJsonContents(jsonContents), Explore.class));
    }

    /**
     * Given a test input filename, return the string of the file's contents
     * @param filename
     * @return
     */
    public static String getJsonContents(String filename) {
        try {
            return IOUtils.toString(FoursquareQueryerTest.class.getResource(filename));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
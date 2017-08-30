package com.springapp.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springapp.demo.model.FoursquarePathBuilder;
import com.springapp.demo.model.generated.Explore;

import java.io.IOException;
import java.util.Spliterators;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility class to query Foursquare endpoints, specifically the explore endpoint.
 * See: https://developer.foursquare.com/docs/venues/explore
 *
 * Created by tmeehan on 2/19/15.
 */
public class FoursquareQueryer {

    private final ObjectMapper mapper;
    private final HTTPRequester requester;

    /**
     * Constructs a FoursquareQueryer with a default http client, which should fulfil non-testing
     * use cases
     */
    public FoursquareQueryer() {
        this(HTTPRequester.fromAjaxCall());
    }

    /**
     * Constructs a FoursquareQueryer with the provided http client, which should fulfil test
     * use cases.
     *
     * @param requester
     */
    protected FoursquareQueryer(HTTPRequester requester) {
        mapper = new ObjectMapper();
        this.requester = requester;
    }

    /**
     * Returns an observable stream of Explore objects from the Foursquare endpoint.
     *
     * @param builder the query to construct
     * @return an observable sequence of Explore objects
     */
    public Stream<Explore> getStream(final FoursquarePathBuilder builder) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<Explore>() {

            private int currentItem = 0;
            private int totalResults = 0;

            @Override
            public boolean hasNext() {
                if (totalResults == 0) {
                    Explore firstResult = getPaginatedExploreObject(builder, 0);
                    totalResults = firstResult.getResponse().getTotalResults().intValue();
                }
                return currentItem < totalResults;
            }

            @Override
            public Explore next() {
                return getPaginatedExploreObject(builder, currentItem++);
            }
        }, 0), false);
    }

    /**
     * The Foursquare explore endpoint provides a method to paginate results from the endpoint.  This method returns
     * one "page" from the endpoint, at index i.
     *
     * @param builder the query to construct
     * @param i the index to fetch
     * @return the Explore object representing the ith page
     */
    protected Explore getPaginatedExploreObject(final FoursquarePathBuilder builder, final int i) {
        try {
            return requester.request(builder, i)
                    .map((data) -> {
                        try {
                            return mapper.readValue(data, Explore.class);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .findFirst()
                    .get();
        } catch (Exception e) {
            return null;
        }
    }
}

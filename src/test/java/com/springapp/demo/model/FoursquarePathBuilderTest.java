package com.springapp.demo.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FoursquarePathBuilderTest {

    @Test(expected = IllegalArgumentException.class)
    public void testLatLonOrNearRequiredFromBaseUrl() {
        FoursquarePathBuilder.fromBaseUrl("/123").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLatLonOrNearRequiredFromExplorerEndpoint() {
        FoursquarePathBuilder.fromExplorerEndpoint().build();
    }

    @Test
    public void testBasicUrl() {
        assertEquals(
                "/v2/venues/explore?near=New+York%2C+NY&v=20150218",
                FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").build()
        );
        assertEquals(
                "/v2/venues/explore?ll=123.0000002%2C456.0000002&v=20150218",
                FoursquarePathBuilder.fromExplorerEndpoint().setLatLon(123,456).build()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLatLonAccuracyMustHaveLatLonSet() {
        FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").setLlAcc(123).build();
    }

    @Test
    public void testLatLonAccuracy() {
        assertEquals(
                "/v2/venues/explore?ll=123.0000002%2C456.0000002&llAcc=123.0000001&v=20150218",
                FoursquarePathBuilder.fromExplorerEndpoint().setLatLon(123,456).setLlAcc(123).build()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAltAccuracyMustHaveAltSet() {
        FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").setAltAcc(123).build();
    }

    @Test
    public void testAltAccuracy() {
        assertEquals(
                "/v2/venues/explore?alt=456.0000001&altAcc=123.0000002&near=New+York%2C+NY&v=20150218",
                FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").setAlt(456).setAltAcc(123).build()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueryWhenSectionProvided() {
        FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").setSection(Section.FOOD).setQuery("food!").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSectionWhenQueryProvided() {
        FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").setQuery("food!").setSection(Section.FOOD).build();
    }

    @Test
    public void testSection() {
        assertEquals(
                "/v2/venues/explore?near=New+York%2C+NY&section=arts&v=20150218",
                FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").setSection(Section.ARTS).build()
        );
    }

    @Test
    public void testQuery() {
        assertEquals(
                "/v2/venues/explore?near=New+York%2C+NY&query=dog+run&v=20150218",
                FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").setQuery("dog run").build()
        );
    }

    @Test
    public void testComplicatedQuery() {
        assertEquals(
                "/v2/venues/explore?alt=1.0000001&altAcc=2.0000002&limit=5&ll=3.0000002%2C4.0000002&llAcc=6.0000001&oauth_token=foo&offset=7&price=2&query=query&v=20150218",
                FoursquarePathBuilder.fromExplorerEndpoint()
                        .setAlt(1)
                        .setAltAcc(2)
                        .setLatLon(3,4)
                        .setLimit(5)
                        .setLlAcc(6)
                        .setOathToken("foo")
                        .setOffset(7)
                        .setPrice(Price.$$)
                        .setQuery("query")
                        .build()
        );
    }
}
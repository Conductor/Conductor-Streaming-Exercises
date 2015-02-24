package com.springapp.demo.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FoursquarePathBuilderTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testBuild() throws Exception {

    }

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
        assertEquals("", FoursquarePathBuilder.fromExplorerEndpoint().setNear("New York, NY").build());
    }
}
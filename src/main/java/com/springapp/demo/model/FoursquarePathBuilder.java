package com.springapp.demo.model;

import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.nio.charset.Charset;
import java.util.TreeMap;

/**
 * Created by tmeehan on 2/18/15.
 */
public class FoursquarePathBuilder {

    public static final String FOURSQUARE_HOST = "api.foursquare.com";
    public static final int FOURSQUARE_PORT = 443;

    private String baseUrl;

    private TreeMap<String, NameValuePair> urlParameters;

    private final String v = "20150218";

    public static final String EXPLORER_ENDPOINT = "/v2/venues/explore";

    private FoursquarePathBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        urlParameters = new TreeMap<String, NameValuePair>();
    }

    public static FoursquarePathBuilder fromBaseUrl(String baseUrl) {
        return new FoursquarePathBuilder(baseUrl);
    }

    public static FoursquarePathBuilder fromExplorerEndpoint() {
        return new FoursquarePathBuilder(EXPLORER_ENDPOINT);
    }

    public FoursquarePathBuilder setOathToken(String oauthToken) {
        addParameter("oauth_token", oauthToken);
        return this;
    }

    /**
     * required unless near is provided. Latitude and longitude of the user's location.
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public FoursquarePathBuilder setLatLon(double latitude, double longitude) {
        String ll = String.format("%f2,%f2", latitude, longitude);
        addParameter("ll", ll);
        return this;
    }

    /**
     * required unless ll is provided. A string naming a place in the world. If the near string is not geocodable,
     * returns a failed_geocode error. Otherwise, searches within the bounds of the geocode and adds a geocode object
     * to the response.
     *
     * @param location
     * @return
     */
    public FoursquarePathBuilder setNear(String location) {
        addParameter("near", location);
        return this;
    }

    /**
     * Accuracy of latitude and longitude, in meters.
     *
     * @param llAcc
     * @return
     */
    public FoursquarePathBuilder setLlAcc(double llAcc) {
        String llAccs = String.format("%f1", llAcc);
        addParameter("llAcc", llAccs);
        return this;
    }

    /**
     * Altitude of the user's location, in meters.
     *
     * @param alt
     * @return
     */
    public FoursquarePathBuilder setAlt(double alt) {
        String alts = String.format("%f1", alt);
        addParameter("alt", alts);
        return this;
    }

    /**
     * Accuracy of the user's altitude, in meters.
     *
     * @param altAcc
     * @return
     */
    public FoursquarePathBuilder setAltAcc(double altAcc) {
        String altAccs = String.format("%f2", altAcc);
        addParameter("altAcc", altAccs);
        return this;
    }

    /**
     * One of food, drinks, coffee, shops, arts, outdoors, sights, trending or specials, nextVenues (venues
     * frequently visited after a given venue), or topPicks (a mix of recommendations generated without a query
     * from the user). Choosing one of these limits results to venues with the specified category or property.
     *
     * @param section
     * @return
     */
    public FoursquarePathBuilder setSection(Section section) {
        String sections = section.getQueryName();
        addParameter("section", sections);
        return this;
    }

    /**
     * A term to be searched against a venue's tips, category, etc. The query parameter has no effect when a
     * section is specified.
     *
     * @param query
     * @return
     */
    public FoursquarePathBuilder setQuery(String query) {
        String querys = query;
        addParameter("query", querys);
        return this;
    }

    /**
     * Number of results to return, up to 50.
     *
     * @param limit
     * @return
     */
    public FoursquarePathBuilder setLimit(Integer limit) {
        String limits = limit.toString();
        addParameter("limit", limits);
        return this;
    }

    /**
     * Used to page through results.
     *
     * @param offset
     * @return
     */
    public FoursquarePathBuilder setOffset(Integer offset) {
        String offsets = offset.toString();
        addParameter("offset", offsets);
        return this;
    }

    /**
     * Comma separated list of price points. Currently the valid range of price points are [1,2,3,4],
     * 1 being the least expensive, 4 being the most expensive. For food venues, in the United States,
     * 1 is < $10 an entree, 2 is $10-$20 an entree, 3 is $20-$30 an entree, 4 is > $30 an entree.
     *
     * @param price
     * @return
     */
    public FoursquarePathBuilder setPrice(Price price) {
        String prices = price.getQueryString();
        addParameter("price", prices);
        return this;
    }

    /**
     * Adds a parameter to the generated query string
     * @param key
     * @param value
     */
    private void addParameter(String key, String value) {
        urlParameters.put(key, new BasicNameValuePair(key, value));
    }

    public String build() {
        StringBuilder builder = new StringBuilder("https://")
                .append(FOURSQUARE_HOST)
                .append(":")
                .append(FOURSQUARE_PORT)
                .append(baseUrl);
        Validate.isTrue(
                (!urlParameters.containsKey("near") && urlParameters.containsKey("ll"))
                        || (urlParameters.containsKey("near") && !urlParameters.containsKey("ll")),
                "Only one of latitude/longitude or near can be set"
        );

        builder.append("?");

        addParameter("v", v);

        if (urlParameters.containsKey("llAcc")) {
            Validate.isTrue(urlParameters.containsKey("ll"), "Latitude/longitude must be set");
        }

        if (urlParameters.containsKey("altAcc")) {
            Validate.isTrue(urlParameters.containsKey("alt"), "Latitude/longitude must be set");
        }

        if (urlParameters.containsKey("query")) {
            Validate.isTrue(!urlParameters.containsKey("section"), "Do not provide when already providing a section");
        }

        if (urlParameters.containsKey("section")) {
            Validate.isTrue(!urlParameters.containsKey("query"), "Do not provide when already providing a query");
        }
        builder.append(URLEncodedUtils.format(urlParameters.values(), Charset.forName("UTF-8")));

        return builder.toString();
    }
}

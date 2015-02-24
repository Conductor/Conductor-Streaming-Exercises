package com.springapp.demo.model;

import org.apache.commons.lang3.Validate;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmeehan on 2/18/15.
 */
public class FoursquarePathBuilder {
    private String baseUrl;
    private String oauthToken;
    private String ll;
    private String near;
    private String llAcc;
    private String alt;
    private String altAcc;
    private String section;
    private String query;
    private String limit;
    private String offset;
    private String price;

    private List<NameValuePair> urlParameters;

    private final String v = "20150218";

    public static final String EXPLORER_ENDPOINT = "/v2/venues/explore";

    private FoursquarePathBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
        urlParameters = new ArrayList<NameValuePair>(15);
    }

    public static FoursquarePathBuilder fromBaseUrl(String baseUrl) {
        return new FoursquarePathBuilder(baseUrl);
    }

    public static FoursquarePathBuilder fromExplorerEndpoint() {
        return new FoursquarePathBuilder(EXPLORER_ENDPOINT);
    }

    public FoursquarePathBuilder setOathToken(String oauthToken) {
        urlParameters.add(new BasicNameValuePair("oauth_token", oauthToken));
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
        urlParameters.add(new BasicNameValuePair("ll", ll));
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
        String near = String.format("near=%s", location);
        urlParameters.add(new BasicNameValuePair("near", near));
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
        urlParameters.add(new BasicNameValuePair("llAcc", llAccs));
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
        urlParameters.add(new BasicNameValuePair("alt", alts));
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
        urlParameters.add(new BasicNameValuePair("altAcc", altAccs));
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
        urlParameters.add(new BasicNameValuePair("section", sections));
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
        urlParameters.add(new BasicNameValuePair("query", querys));
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
        urlParameters.add(new BasicNameValuePair("limit", limits));
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
        urlParameters.add(new BasicNameValuePair("offset", offsets));
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
        urlParameters.add(new BasicNameValuePair("price", prices));
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder(baseUrl);
        Validate.isTrue(
                (near == null && ll != null) || (near != null && ll == null),
                "Only one of latitude/longitude or near can be set"
        );

        builder.append("?");
        if (near != null) {
            builder.append(String.format("&near=%s", near));
        }
        else {
            builder.append(String.format("&ll=%s", ll));
        }

        if (oauthToken != null) {
            builder.append(String.format("&oauth_token=%s", oauthToken));
        }

        if (llAcc != null) {
            Validate.notNull(ll, "Latitude/longitude must be set");
            builder.append(String.format("&llAcc=%s", llAcc));
        }

        if (query != null) {
            Validate.isTrue(section == null, "Do not provide when already providing a section");
            builder.append(String.format("&section=%s", section));
        }

        if (section != null) {
            Validate.isTrue(query == null, "Do not provide when already providing a query");
            builder.append(String.format("&query=%s", query));
        }

        if (alt != null) {
            builder.append(String.format("&alt=%s", alt));
        }

        if (altAcc != null) {
            Validate.notNull(alt, "alt must be provided when setting altAcc");
            builder.append(String.format("&altAcc=%s", altAcc));
        }

        if (limit != null) {
            builder.append(String.format("&limit=%s", limit));
        }

        if (offset != null) {
            builder.append(String.format("&offset=%s", offset));
        }

        if (price != null) {
            builder.append(String.format("&price=%s", price));
        }

        builder.append(String.format("&v=%s", v));

        try {
            return URLEncoder.encode(builder.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}

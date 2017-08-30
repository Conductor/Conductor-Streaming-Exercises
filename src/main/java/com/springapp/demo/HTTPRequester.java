package com.springapp.demo;

import com.springapp.demo.model.FoursquarePathBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.stream.Stream;

/**
 * Created by bshai on 8/30/17.
 */
public abstract class HTTPRequester {

    public abstract Stream<String> request(FoursquarePathBuilder builder, int page) throws Exception;

    public static HTTPRequester fromAjaxCall() {
        return new AjaxHTTPRequester();
    }
    private static class AjaxHTTPRequester extends HTTPRequester {

        @Override
        public Stream<String> request(FoursquarePathBuilder builder, int page) throws Exception {
            URL url = new URL(builder.setOffset(page).setLimit(1).build());
            URLConnection uc = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    uc.getInputStream()));

            return br.lines();
        }
    }
}

package com.springapp.demo;

import com.springapp.demo.model.FoursquarePathBuilder;
import com.springapp.demo.model.Price;
import com.springapp.demo.model.Section;
import com.springapp.demo.model.generated.Explore;
import rx.functions.Action1;

public class MainController {

    private final FoursquareQueryer foursquareQueryer;

    public MainController(FoursquareQueryer foursquareQueryer) {
        this.foursquareQueryer = foursquareQueryer;
    }

    /**
     * Exercise 5: Below you'll find a simple query to the Foursquare API for cheap eats near Brooklyn.
     * Modify this so that we query Foursquare for both Los Angeles and New York for only the highest price and highested
     * rates dining options.  This should be as real time as possible--don't buffer anything in memory, just print it
     * out as soon Foursquare sends us more information.  You may want to use a combination operator to achieve this.
     */
    void home() {
        foursquareQueryer.getStream(
                FoursquarePathBuilder
                        .fromExplorerEndpoint()
                        .setLatLon(40.7, -74)
                        .setSection(Section.FOOD)
                        .setPrice(Price.$)
                        .setOathToken("NRS3KFBCQ1TDUPU3XOSBZN0SSGDSUGYFVJI2J0EJSNMTVEE5")
        )
        .subscribe(new Action1<Explore>() {
            @Override
            public void call(Explore explore) {
                System.out.println("Received: " + explore.toString());
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new MainController(new FoursquareQueryer()).home();
    }
}
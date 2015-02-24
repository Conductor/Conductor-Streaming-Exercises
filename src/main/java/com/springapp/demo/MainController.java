package com.springapp.demo;

import com.springapp.demo.model.FoursquarePathBuilder;
import com.springapp.demo.model.Price;
import com.springapp.demo.model.Section;
import com.springapp.demo.model.generated.Explore;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainController {

    private final FoursquareQueryer foursquareQueryer;

    public MainController(FoursquareQueryer foursquareQueryer) {
        this.foursquareQueryer = foursquareQueryer;
    }

    void home() {
        foursquareQueryer.getStream(
                FoursquarePathBuilder
                        .fromExplorerEndpoint()
                        .setLatLon(40.7, -74)
                        .setSection(Section.FOOD)
                        .setPrice(Price.$$$)
                        .setOathToken("NRS3KFBCQ1TDUPU3XOSBZN0SSGDSUGYFVJI2J0EJSNMTVEE5")
        )
        .filter(new Func1<Explore, Boolean>() {
            @Override
            public Boolean call(Explore explore) {
                return "Hoboken".equals(explore.getResponse().getGroups().get(0).getItems().get(0).getVenue().getLocation().getCity());
            }
        })
        .toBlocking()
        .forEach(new Action1<Explore>() {
            @Override
            public void call(Explore explore) {
                System.out.println(explore);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        new MainController(new FoursquareQueryer()).home();
    }
}
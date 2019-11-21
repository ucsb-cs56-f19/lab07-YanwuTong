
package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Map;
import java.util.HashMap;
import hello.geojson.FeatureCollection;

import com.nimbusds.oauth2.sdk.client.ClientReadRequest;

@Controller
public class WebController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public String getHomepage(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        Map<String, String> urls = new HashMap<>();

        // get around an unfortunate limitation of the API
        @SuppressWarnings("unchecked")
        Iterable<ClientRegistration> iterable = ((Iterable<ClientRegistration>) clientRegistrationRepository);
        iterable.forEach(clientRegistration -> urls.put(clientRegistration.getClientName(),
                "/oauth2/authorization/" + clientRegistration.getRegistrationId()));

        model.addAttribute("urls", urls);
        return "login";
    }

    @GetMapping("/page1")
    public String getPage1(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        return "page1";
    }

    @GetMapping("/page2")
    public String getPage2(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken) {

        return "page2";
    }

    @GetMapping("/earthquakes/search")
    public String getEarthquakesSearch(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
            EqSearch eqSearch) {
        return "earthquakes/search";
    }

    @GetMapping("/earthquakes/results")
    public String getEarthquakesResults(Model model, OAuth2AuthenticationToken oAuth2AuthenticationToken,
            EqSearch eqSearch) {
        EarthquakeQueryService e = new EarthquakeQueryService();
        model.addAttribute("eqSearch", eqSearch);
        String json = e.getJSON(eqSearch.getDistance(), eqSearch.getMinmag());
        model.addAttribute("json", json);
        FeatureCollection featureCollection = FeatureCollection.fromJSON(json);
        model.addAttribute("featureCollection",featureCollection);
        return "earthquakes/results";
    }
}

package org.acme.service;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

@ApplicationScoped
public class CountriesService {
    private static final Logger LOG = Logger.getLogger(CountriesService.class);
    
    @Inject
    @ConfigProperty(name = "restcountries.api.url")
    String apiUrl;

    @CacheResult(cacheName = "country-demonym-cache")
    public String fetchDemonymByCountryCode(String countryCode) { 
        try {
            var client = ClientBuilder.newClient();
            var response = client.target(apiUrl + countryCode)
                    .request(MediaType.APPLICATION_JSON)
                    .get(String.class);


            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                JSONObject countryData = jsonArray.getJSONObject(0);
                return countryData.getJSONObject("demonyms").getJSONObject("eng").getString("m");
            }
        } 
        catch (Exception e) {
            LOG.error("Error fetching demonym for country code: " + countryCode, e);
        }
        
        return "Unknown";
    }
}

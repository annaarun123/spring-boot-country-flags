package com.annaarun.countryflags;

import java.io.FileNotFoundException;
import java.io.IOException;
import io.prometheus.client.Counter;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.simple.parser.ParseException;

import java.util.Scanner;

/**
 * Assumptions and Notes:
 * ======================
 * 
 * 1) Just exposes one REST service "/countries/flags" as mentioned in the requirement.
 * 2) Typically loading countries by continent, or by name or all could be exposed as different route endpoints
 * e.g. "/countries/", "/continents/{continentName}/countries", "/countries/{countryName}"
 * 3) Since requirement mentions to expose one service, it is all combined in one endpoint.
 * 4) Also, to keep the response consistent, even if there is only one country is queried, the response is still an array
 * 5) Pagination and sorting could be added for such a search service as it may be necessary of the dataset is large
 * 6) Metrics are exposed at "/metrics" URI which can be scraped by Prometheus
 */

@RestController
class CountryFlagController {

    CountryFlagController() {

    }

    @Value("classpath:continents.json")
    private Resource continentsJson;

    List<Continent> continents;

    @RequestMapping(value = "/countries/flags", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    String getCountriesFlags(
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String continent
    ) throws FileNotFoundException,
    IOException, ParseException {

        // Make sure the continents json file resource is loaded
        loadContinentsFromJSONFile();
        
        List<Country> countries = null;

        // Based on query parameter, fetch appropriate list
        if(country != null) {
            countries = filterCountriesByName(continents, country);
            MonitoringConfig.countryRequests.labels(country).inc();
        } else if(continent != null) {
            countries = filterCountriesByContinent(continents, continent);
            MonitoringConfig.continentRequests.labels(continent).inc();
        } else {
            countries = getAllCountries(continents);
            MonitoringConfig.allCountriesRequests.inc();
        }

        Gson gson = new Gson();
        String response = gson.toJson(countries);

        
        return response;
    }

    // Private method to load continents json file - Just once
    // May be extended to refresh or cache when there is an external datastore such a DB
    private void loadContinentsFromJSONFile() throws FileNotFoundException, IOException {

        if(continents == null) {
            Scanner scanner = new Scanner(continentsJson.getFile());
            String json = scanner.useDelimiter("\\Z").next();

            continents = new Gson().fromJson(json, new TypeToken<ArrayList<Continent>>(){}.getType());

            scanner.close();
        }

    }

    public List<Country> filterCountriesByName(List<Continent> continents, String countryName) {
        List<Country> countries = new ArrayList<Country>();

        for (Continent continent: continents) {
            List<Country> filteredCountries = continent.getCountries().stream().filter(
                country -> country.getName().equalsIgnoreCase(countryName)).collect(Collectors.toList());

            if(filteredCountries != null && !filteredCountries.isEmpty()) {
                countries = filteredCountries;
                break;
            }
        }
        return countries;
    }

    public List<Country> filterCountriesByContinent(List<Continent> continents, String continentName) {
        List<Country> countries = new ArrayList<Country>();

        List<Continent> filteredContinent = continents.stream().filter(
            continent -> continent.getContinent().equalsIgnoreCase(continentName)).collect(Collectors.toList());

        if(filteredContinent != null && !filteredContinent.isEmpty()) {
            countries = filteredContinent.get(0).getCountries();
        }
        return countries;
    }

    public List<Country> getAllCountries(List<Continent> continents) {
        List<Country> countries = new ArrayList<Country>();
        for (Continent continent: continents) {
            countries.addAll(continent.getCountries());
        }
        return countries;
    }

}

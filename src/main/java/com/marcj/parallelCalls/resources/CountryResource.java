package com.marcj.parallelCalls.resources;

import com.marcj.parallelCalls.Client.CountryClient;
import com.marcj.parallelCalls.Client.Output.Country;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Api(value = "CountryResource")
@RestController
public class CountryResource {

    private final CountryClient countryClient;

    public CountryResource(
            CountryClient countryClient
    ) {
        this.countryClient = countryClient;
    }

    @ApiOperation(httpMethod = "GET", value = "Get all European and French speaking countries", response = String.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Countries not found"),
            @ApiResponse(code = 500, message = "The countries could not be fetched")
    })
    @GetMapping("")
    public List<String> getAllEuropeanFrenchSpeakingCountries() throws Throwable {
        CompletableFuture<List<Country>> countriesByLanguageFuture = countryClient.getCountriesByLanguage("fr");
        CompletableFuture<List<Country>> countriesByRegionFuture = countryClient.getCountriesByRegion("europe");

        List<String> europeanFrenchSpeakingCountries;
        try {
            europeanFrenchSpeakingCountries = new ArrayList<>(countriesByLanguageFuture.get().stream().map(Country::getName).collect(Collectors.toList()));
            europeanFrenchSpeakingCountries.retainAll(countriesByRegionFuture.get().stream().map(Country::getName).collect(Collectors.toList()));
        } catch (Throwable e) {
            throw e.getCause();
        }

        return europeanFrenchSpeakingCountries;
    }
}

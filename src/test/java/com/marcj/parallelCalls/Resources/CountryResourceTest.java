package com.marcj.parallelCalls.Resources;

import com.marcj.parallelCalls.Client.CountryClient;
import com.marcj.parallelCalls.Client.Output.Country;
import com.marcj.parallelCalls.resources.CountryResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CountryResourceTest {
    @InjectMocks
    private CountryResource countryResource;

    private CountryClient countryClient;

    @Before
    public void setup() {
        this.countryClient = mock(CountryClient.class);
        this.countryResource = new CountryResource(countryClient);
    }

    @Test
    public void getAllEuropeanFrenchSpokenCountries() throws Throwable {
        //GIVEN
        Country country = new Country();
        country.setName("France");
        Country country2 = new Country();
        country2.setName("Belgium");
        Country country3 = new Country();
        country3.setName("Germany");
        List<Country> countriesByLanguage = new ArrayList<>();
        countriesByLanguage.add(country);
        countriesByLanguage.add(country2);
        when(countryClient.getCountriesByLanguage(anyString())).thenReturn(CompletableFuture.completedFuture(countriesByLanguage));
        List<Country> countriesByRegion = new ArrayList<>();
        countriesByRegion.add(country);
        countriesByRegion.add(country3);
        when(countryClient.getCountriesByRegion(anyString())).thenReturn(CompletableFuture.completedFuture(countriesByRegion));

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("France");

        //WHEN
        List<String> result = countryResource.getAllEuropeanFrenchSpeakingCountries();

        //THEN
        assertEquals(expectedResult, result);
    }
}

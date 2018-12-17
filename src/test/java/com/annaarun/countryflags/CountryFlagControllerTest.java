package com.annaarun.countryflags;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

public class CountryFlagControllerTest {
	CountryFlagController controller = new CountryFlagController();
    List<Continent> continents;

    @Before
    public void setUp() {
        Country country1 = new Country("Nigeria", "🇳🇬");
        Country country2 = new Country("Ethiopia", "🇳🇪");

        Country country3 = new Country("USA","🇺🇸");
        Country country4 = new Country("Brazil", "🇧🇷");

        List<Country> countries1 = new ArrayList<>();
        countries1.add(country1);
        countries1.add(country2);
        List<Country> countries2 = new ArrayList<>();
        countries2.add(country3);
        countries2.add(country4);
        Continent continent1 = new Continent("Africa", countries1);
        Continent continent2 = new Continent("America", countries2);
        
        continents = new ArrayList<>();
        continents.add(continent1);
        continents.add(continent2);

    }

    @Test
	public void testGetAllCountries() {
        System.out.println("testGetAllCountries");
        List<Country> countries = controller.getAllCountries(continents);
        
        assertEquals(4, countries.size());
        assertEquals("Nigeria", countries.get(0).getName());
        assertEquals("🇳🇪", countries.get(1).getFlag());
	}

    @Test
	public void testFilterCountriesByContinent() {
        System.out.println("testFilterCountriesByContinent");
        List<Country> countries = controller.filterCountriesByContinent(continents, "America");
        
        assertEquals(2, countries.size());
        assertEquals("USA", countries.get(0).getName());
        assertEquals("🇺🇸", countries.get(0).getFlag());
	}
    
    @Test
	public void testFilterCountriesByName() {
        System.out.println("testFilterCountriesByName");
        List<Country> countries = controller.filterCountriesByName(continents, "Brazil");
        
        assertEquals(1, countries.size());
        assertEquals("Brazil", countries.get(0).getName());
        assertEquals("🇧🇷", countries.get(0).getFlag());
	}

    @Test
	public void testFilterCountriesByNameNotExistent() {
        System.out.println("testFilterCountriesByNameNotExistent");
        List<Country> countries = controller.filterCountriesByName(continents, "India");
        
        assertEquals(0, countries.size());
	}

    @Test
	public void testFilterCountriesByContinentNotExistent() {
        System.out.println("testFilterCountriesByContinentNotExistent");
        List<Country> countries = controller.filterCountriesByContinent(continents, "Asia");
        
        assertEquals(0, countries.size());
	}
}
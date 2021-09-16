package pt.ulusofona.deisi.aedProj2020;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestMain {

    @Test
    public void countMoviesMonthYearInvalidMonthlTest() {
        String real = Main.askAmbrosio("COUNT_MOVIES_MONTH_YEAR 25 1999");
        String expected = "0";
        assertEquals(expected, real);
    }

    @Test
    public void topMonthMovieCountTestInvalidYear() {
        String real = Main.askAmbrosio("TOP_MONTH_MOVIE_COUNT 3200");
        String expected = "Invalid query. Try again.";
        assertEquals(expected, real);
    }

    @Test
    public void getActorsByDirectorTestDirectorNotExist() {
        String real = Main.askAmbrosio("GET_ACTORS_BY_DIRECTOR 4 Hericles Semedo");
        String expected = "";
        assertEquals(expected, real);
    }

    @Test
    public void topMoviesWithGenderBiasLimitEqualsZeroTest() {
        String real = Main.askAmbrosio("TOP_MOVIES_WITH_GENDER_BIAS 0 1999");
        String expected = "";
        assertEquals(expected, real);
    }

    @Test
    public void topMoviesWithGenderBiasInvalidYearTest() {
        String real = Main.askAmbrosio("TOP_MOVIES_WITH_GENDER_BIAS 0 199089");
        String expected = "Invalid query. Try again.";
        assertEquals(expected, real);
    }

    @Test
    public void insertDirectorMovieNotExistsTest() {
        String real = Main.askAmbrosio("INSERT_DIRECTOR 786786;Afonso Henriques;6000000");
        String expected = "ERRO";
        assertEquals(expected, real);
    }

    @Test
    public void insertDirectorMovieDirectorExistsTest() {
        String real = Main.askAmbrosio("INSERT_DIRECTOR 786786;Afonso Henriques;150");
        String expected = "ERRO";
        assertEquals(expected, real);
    }

    @Test
    public void getMoviesWithActorContainingInexistentSubStringTest() {
        String real = Main.askAmbrosio("GET_MOVIES_WITH_ACTOR_CONTAINING semedo");
        String expected = "";
        assertEquals(expected, real);
    }

    @Test
    public void top6DirectorsWithinFamilyTestBadYearInterval() {
        String real = Main.askAmbrosio("TOP_6_DIRECTORS_WITHIN_FAMILY 2005 1998");
        String expected = "";
        assertEquals(expected, real);
    }

    @Test
    public void top6DirectorsWithinFamilyTestBadYearFormat() {
        String real = Main.askAmbrosio("TOP_6_DIRECTORS_WITHIN_FAMILY 2005 199gsd8");
        String expected = "Invalid query. Try again.";
        assertEquals(expected, real);
    }

    @Test
    public void top6DirectorsWithinFamilyTestInvalidYear() {
        String real = Main.askAmbrosio("TOP_6_DIRECTORS_WITHIN_FAMILY 2005 1998999");
        String expected = "Invalid query. Try again.";
        assertEquals(expected, real);
    }

}

package pt.ulusofona.deisi.aedProj2020;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;

public class TestMovie {

    @Test
    public void movieToString1() {
        Movie movie = new Movie();
        try {
            movie = new Movie(1234, "The Godfather", 3.30, 500000,
                    new SimpleDateFormat("dd-MM-yyyy").parse("06-07-1987"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("1234 | The Godfather | 1987-07-06 | 0 | 0 | 0 | 0", movie.toString());
    }

    @Test
    public void movieToString2() {
        Movie movie = new Movie();
        try {
            movie = new Movie(603, "The Matrix", 3.30, 500000,
                    new SimpleDateFormat("dd-MM-yyyy").parse("30-03-1999"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals("603 | The Matrix | 1999-03-30 | 0 | 0 | 0 | 0", movie.toString());
    }
}


package pt.ulusofona.deisi.aedProj2020;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.String.valueOf;

public class Main {

    static HashMap<Integer, Movie> allMoviesMap = new HashMap<>();
    static HashMap<Integer, Genre> allGenresMap = new HashMap<>();
    static ArrayList<Movie> allMoviesList = new ArrayList<>();

    static int moviesLinesIgnored = 0;
    static int actorsLinesIgnored = 0;
    static int genresMoviesLinesIgnored = 0;
    static int genresLinesIgnored = 0;
    static int directorsLinesIgnored = 0;
    static int votesLinesIgnored = 0;

    public static void main(String[] args) {
        //parseFiles();

        System.out.println("Benvindo ao DEISI Home Theater");
        System.out.print("> ");

        Scanner in = new Scanner(System.in);
        String line = in.nextLine();

        while (line != null && !line.equals("QUIT")) {
            long start = System.currentTimeMillis();
            String result = askAmbrosio(line);
            long end = System.currentTimeMillis();

            System.out.println(result);
            System.out.println("(demorou " + (end - start) + " ms)");
            System.out.print("> ");
            line = in.nextLine();
        }
    }

    // *************************** MANDATORY **********************************

    public static void parseFiles() {
        allMoviesMap.clear();
        allGenresMap.clear();
        allMoviesList.clear();

        readMovies("deisi_movies.txt");

        readMoviesVotes("deisi_movie_votes.txt");

        readActors("deisi_actors.txt");

        readDirectors("deisi_directors.txt");

        readGenres("deisi_genres.txt");

        readGenresMovies("deisi_genres_movies.txt");
    }

    public static ArrayList<Movie> getMovies() {
        return allMoviesList;
    }

    public static int countIgnoredLines(String fileName) {
        switch (fileName) {
            case "deisi_actors.txt":
                return actorsLinesIgnored;

            case "deisi_directors.txt":
                return directorsLinesIgnored;

            case "deisi_genres.txt":
                return genresLinesIgnored;

            case "deisi_genres_movies.txt":
                return genresMoviesLinesIgnored;

            case "deisi_movies.txt":
                return moviesLinesIgnored;

            case "deisi_movie_votes.txt":
                return votesLinesIgnored;
        }
        return 0;
    }

    public static String askAmbrosio(String query) {
        String[] queryParts = query.split(" ", 2);
        switch (queryParts[0]) {
            case "COUNT_MOVIES_MONTH_YEAR":
                try {
                    String[] subQuery = queryParts[1].split(" ");

                    if (queryParts.length != 2) {
                        break;
                    }

                    int mes = Integer.parseInt(subQuery[0].trim());
                    int ano = Integer.parseInt(subQuery[1].trim());

                    if (ano > 2020) {
                        break;
                    }
                    return countMoviesMonthYear(mes, ano);
                } catch (NumberFormatException ex) {
                    break;
                }

            case "TOP_MONTH_MOVIE_COUNT":
                try {
                    if (queryParts.length != 2) {
                        break;
                    }

                    int ano = Integer.parseInt(queryParts[1].trim());

                    if (ano > 2020) {
                        break;
                    }
                    return topMonthMovieCount(ano);
                } catch (NumberFormatException ex) {
                    break;
                }

            case "GET_ACTORS_BY_DIRECTOR":
                try {

                    String[] subQuery = queryParts[1].split(" ", 2);
                    int numVezes = Integer.parseInt(subQuery[0].trim());
                    String director = subQuery[1];

                    return getActorsByDirector(numVezes, director);
                } catch (NumberFormatException ex) {
                    break;
                }

            case "TOP_MOVIES_WITH_GENDER_BIAS":
                try {
                    String[] subQuery = queryParts[1].split(" ");
                    if (subQuery.length != 2) {
                        break;
                    }

                    int limite = Integer.parseInt(subQuery[0]);
                    int ano = Integer.parseInt(subQuery[1]);

                    if (ano > 2020) {
                        break;
                    }
                    return topMoviesWithGenderBias(limite, ano);
                } catch (NumberFormatException ex) {
                    break;
                }

            case "INSERT_DIRECTOR":
                try {
                    String[] subQuery = queryParts[1].split(";");

                    if (subQuery.length != 3) {
                        break;
                    }

                    int id = Integer.parseInt(subQuery[0].trim());
                    String nome = subQuery[1];
                    int filmeId = Integer.parseInt(subQuery[2].trim());

                    return insertDirector(id, nome, filmeId);

                } catch (NumberFormatException ex) {
                    break;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    return "ERRO";
                }

            case "GET_MOVIES_WITH_ACTOR_CONTAINING":
                return getMoviesWithActorContaining(queryParts[1]);

            case "TOP_6_DIRECTORS_WITHIN_FAMILY":
                try {
                    String[] subQuery = queryParts[1].split(" ");
                    if (subQuery.length != 2) {
                        break;
                    }
                    int[] intervalo = {
                            Integer.parseInt(subQuery[0]),
                            Integer.parseInt(subQuery[1]),
                    };
                    return top6DirectorsWithinFamily(intervalo);
                } catch (NumberFormatException ex) {
                    break;
                }
        }
        return "Invalid query. Try again.";
    }

    public static String getVideoURL() {
        return null;
    }

    // ******************************** PARSER HELPERS *****************************

    public static void readMovies(String fileName) {
        moviesLinesIgnored = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                try {
                    if (numberOfSplits(line) != 5) {
                        moviesLinesIgnored++;
                    } else {
                        String[] lineParts = line.split(",");

                        int id = Integer.parseInt(lineParts[0].trim());
                        String title = lineParts[1];
                        double duration = Double.parseDouble(lineParts[2].trim());
                        long budget = Integer.parseInt(lineParts[3].trim());
                        Date releaseDate = new SimpleDateFormat("dd-MM-yyyy").parse(lineParts[4]);

                        Movie movie = new Movie(id, title, duration, budget, releaseDate);

                        if (allMoviesMap.get(movie.id) == null) {
                            allMoviesList.add(movie);
                        }
                        allMoviesMap.put(movie.id, movie);
                    }
                } catch (NumberFormatException ex) {
                    votesLinesIgnored++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            String mesage = "Erro: o ficheiro " + fileName + " nao foi encontrado. ";
            System.out.println(mesage);
        } catch (IOException | ParseException e) {
            moviesLinesIgnored++;
        }
    }

    public static void readMoviesVotes(String fileName) {
        votesLinesIgnored = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                try {
                    if (numberOfSplits(line) != 3) {
                        votesLinesIgnored++;
                    } else {
                        String[] lineParts = line.split(",");

                        int movieId = Integer.parseInt(lineParts[0].trim());
                        double averageVotes = Double.parseDouble(lineParts[1].trim());
                        int votes = Integer.parseInt(lineParts[2].trim());

                        if (allMoviesMap.containsKey(movieId)) {
                            allMoviesMap.get(movieId).votes = votes;
                            allMoviesMap.get(movieId).averageVotes = averageVotes;
                        }
                    }
                } catch (NumberFormatException ex) {
                    votesLinesIgnored++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            String mesage = "Erro: o ficheiro " + fileName + " nao foi encontrado. ";
            System.out.println(mesage);
        } catch (IOException e) {
            votesLinesIgnored++;
        }
    }

    public static void readActors(String fileName) {
        actorsLinesIgnored = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                try {
                    if (numberOfSplits(line) != 4) {
                        actorsLinesIgnored++;
                    } else {
                        String[] actor = line.split(",");

                        int id = Integer.parseInt(actor[0].trim());
                        String name = actor[1];
                        String genre = actor[2];
                        int movieId = Integer.parseInt(actor[3].trim());

                        Actor a = new Actor(id, name, genre, movieId);

                        if (allMoviesMap.containsKey(movieId)) {
                            allMoviesMap.get(movieId).actors.put(id, a);
                        }
                    }
                } catch (NumberFormatException ex) {
                    actorsLinesIgnored++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            String mesage = "Erro: o ficheiro " + fileName + " nao foi encontrado. ";
            System.out.println(mesage);
        } catch (IOException e) {
            actorsLinesIgnored++;
        }
    }

    public static void readDirectors(String fileName) {
        directorsLinesIgnored = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                try {
                    if (numberOfSplits(line) != 3) {
                        if (numberOfSplits(line) == 4) {
                            String[] lineParts = line.split(",");
                            if (Pattern.matches("[a-zA-Z]+", lineParts[2].trim())
                                    && !Pattern.matches("[a-zA-Z]+", lineParts[3].trim())) {
                                int id = Integer.parseInt(lineParts[0].trim());
                                String nome = lineParts[1] + "," + lineParts[2];
                                int idMovie = Integer.parseInt(lineParts[2].trim());

                                Director director = new Director(id, nome, idMovie);

                                if (allMoviesMap.get(idMovie) != null) {
                                    allMoviesMap.get(idMovie).directors.put(id, director);
                                }
                            }
                        }
                    } else {
                        String[] lineParts = line.split(",");

                        int id = Integer.parseInt(lineParts[0].trim());
                        String nome = lineParts[1];
                        int idMovie = Integer.parseInt(lineParts[2].trim());

                        Director director = new Director(id, nome, idMovie);

                        if (allMoviesMap.get(idMovie) != null) {
                            allMoviesMap.get(idMovie).directors.put(id, director);
                        }
                    }
                } catch (NumberFormatException ex) {
                    directorsLinesIgnored++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            String mensagem = "Erro: o ficheiro " + fileName + " nao foi encontrado. ";
            System.out.println(mensagem);
        } catch (IOException e) {
            directorsLinesIgnored++;
        }
    }

    public static void readGenres(String fileName) {
        genresLinesIgnored = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                try {
                    if (numberOfSplits(line) != 2) {
                        genresLinesIgnored++;
                    } else {
                        String[] lineParts = line.split(",");

                        int id = Integer.parseInt(lineParts[0].trim());
                        String name = lineParts[1];

                        Genre genre = new Genre(id, name);

                        allGenresMap.put(Integer.parseInt(lineParts[0].trim()), genre);
                    }
                } catch (NumberFormatException ex) {
                    genresLinesIgnored++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            String mesage = "Erro: o ficheiro " + fileName + " nao foi encontrado. ";
            System.out.println(mesage);
        } catch (IOException e) {
            genresLinesIgnored++;
        }
    }

    public static void readGenresMovies(String fileName) {
        genresMoviesLinesIgnored = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                try {
                    if (numberOfSplits(line) != 2) {
                        genresMoviesLinesIgnored++;
                    } else {
                        String[] lineParts = line.split(",");

                        int idGenre = Integer.parseInt(lineParts[0].trim());
                        int idMovie = Integer.parseInt(lineParts[1].trim());

                        if (allMoviesMap.containsKey(idMovie)) {
                            allMoviesMap.get(idMovie).genres.add(allGenresMap.get(idGenre));
                        }
                    }
                } catch (NumberFormatException | NullPointerException ex) {
                    genresMoviesLinesIgnored++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException exception) {
            String mesage = "Erro: o ficheiro " + fileName + " nao foi encontrado. ";
            System.out.println(mesage);
        } catch (IOException e) {
            genresMoviesLinesIgnored++;
        }
    }

    public static int numberOfSplits(String line) {
        return line.split(",").length;
    }

    public static String getLastName(String name) {
        String[] surnames = name.split(" ");
        return surnames[surnames.length - 1];
    }

    // *************************** QUERY HANDLERS **********************************

    public static String countMoviesMonthYear(int month, int year) {
        int total = 0;
        if (month > 12 || month < 1) {
            return "0";
        }
        Set<Integer> keys = allMoviesMap.keySet();
        for (Integer key : keys) {
            if (allMoviesMap.get(key) != null) {
                if (allMoviesMap.get(key).getDate("YEAR") == year
                        && allMoviesMap.get(key).getDate("MONTH") == month) {
                    total++;
                }
            }
        }
        return valueOf(total);
    }

    public static String topMonthMovieCount(int year) {
        HashMap<Integer, Integer> months = new HashMap<>();
        months.put(1, 0);
        months.put(2, 0);
        months.put(3, 0);
        months.put(4, 0);
        months.put(5, 0);
        months.put(6, 0);
        months.put(7, 0);
        months.put(8, 0);
        months.put(9, 0);
        months.put(10, 0);
        months.put(11, 0);
        months.put(12, 0);

        Set<Integer> keys = allMoviesMap.keySet();
        for (Integer key : keys) {
            if (allMoviesMap.containsKey(key)) {
                if (allMoviesMap.get(key).getDate("YEAR") == year) {
                    int month = allMoviesMap.get(key).getDate("MONTH");
                    int actual = months.get(month);
                    months.put(month, actual + 1);
                }
            }
        }
        int monthTop = 1, size = 0, month = 1, total = 0;
        while (size < months.size()) {
            if (months.get(month) != null) {
                if (months.get(month) > total) {
                    total = months.get(month);
                    monthTop = month;
                }
            }
            size++;
            month++;
        }
        return monthTop + ":" + total;
    }

    public static String getActorsByDirector(int times, String director) {

        HashMap<String, Integer> actorTimes = new HashMap<>();
        StringBuilder output = new StringBuilder();

        for (Map.Entry<Integer, Movie> movie : allMoviesMap.entrySet()) {
            Movie currentMovie = movie.getValue();
            for (Map.Entry<Integer, Actor> actor : currentMovie.actors.entrySet()) {
                if (currentMovie.hasDirector(director)) {
                    if (actorTimes.containsKey(actor.getValue().name)) {
                        int value = actorTimes.get(actor.getValue().name);
                        actorTimes.put(actor.getValue().name, value + 1);
                    } else {
                        actorTimes.put(actor.getValue().name, 1);
                    }
                }
            }
        }
        for (Map.Entry<String, Integer> actor : actorTimes.entrySet()) {
            if (actor.getValue() >= times) {
                String outputLine = actor.getKey() + ":" + actor.getValue() + '\n';
                output.append(outputLine);
            }
        }
        return output.toString();
    }

    public static String topMoviesWithGenderBias(int limit, int year) {
        HashMap<String, String[]> movieAndBias = new HashMap<>();
        StringBuilder output = new StringBuilder();

        for (Map.Entry<Integer, Movie> movieEntry : allMoviesMap.entrySet()) {
            Movie movie = movieEntry.getValue();
            if (movie.actors.size() > 10 && movie.getDate("YEAR") == year) {
                movieAndBias.put(movie.title, movie.getGenreBias());
            }
        }
        int iteration = 0;
        while (iteration < limit) {
            int bigger = 0;
            String key = "";
            String[] value = new String[2];

            for (Map.Entry<String, String[]> bias : movieAndBias.entrySet()) {
                String[] biasValue = bias.getValue();
                if (Integer.parseInt(biasValue[1]) >= bigger) {
                    bigger = Integer.parseInt(biasValue[1]);
                    key = bias.getKey();
                    value = biasValue;
                }
            }
            if (key.equals("")) {
                break;
            }
            String outputLine = key + ":" + value[0] + ":" + value[1] + '\n';
            output.append(outputLine);
            movieAndBias.remove(key);
            iteration++;
        }
        return output.toString();
    }

    public static String insertDirector(int id, String name, int movieId) {
        for (Map.Entry<Integer, Movie> movieEntry : allMoviesMap.entrySet()) {
            if (movieEntry.getKey() == movieId) {
                if (!(movieEntry.getValue().directors.containsKey(id))) {
                    Director novo = new Director(id, name, movieId);
                    allMoviesMap.get(movieId).directors.put(id, novo);
                    return "OK";
                }
            }
        }
        return "ERRO";
    }

    public static String getMoviesWithActorContaining(String subString) {
        StringBuilder output = new StringBuilder();
        ArrayList<String> titles = new ArrayList<>();

        Set<Integer> keys = allMoviesMap.keySet();
        for (Integer key : keys) {
            if (allMoviesMap.get(key) != null) {
                HashMap<Integer, Actor> actors = allMoviesMap.get(key).actors;
                for (Map.Entry<Integer, Actor> a : actors.entrySet()) {
                    if (a.getValue().name.contains(subString)) {
                        titles.add(allMoviesMap.get(key).title);
                        break;
                    }
                }
            }
        }
        titles.sort(String.CASE_INSENSITIVE_ORDER);

        for (String title : titles) {
            output.append(title).append('\n');
        }
        return output.toString();
    }

    public static String top6DirectorsWithinFamily(int[] interval) {
        if (interval[0] > 2020 || interval[1] > 2020) {
            return "Invalid query. Try again.";
        }

        HashMap<String, Integer> topDirectors = new HashMap<>();
        StringBuilder output = new StringBuilder();

        for (Map.Entry<Integer, Movie> movieEntry : allMoviesMap.entrySet()) {
            int ano = movieEntry.getValue().getDate("YEAR");
            if (ano >= interval[0] && ano <= interval[1] && movieEntry.getValue().directors.size() >= 2) {

                for (Map.Entry<Integer, Director> referenceDirector : movieEntry.getValue().directors.entrySet()) {
                    String referenceLastName = referenceDirector.getValue().getApelido();
                    for (Map.Entry<Integer, Director> currentDirector : movieEntry.getValue().directors.entrySet()) {
                        String currentLastName = currentDirector.getValue().getApelido();
                        if (referenceDirector.getValue().directorId != currentDirector.getValue().directorId) {
                            if (referenceLastName.equals(currentLastName)) {
                                topDirectors.putIfAbsent(referenceDirector.getValue().name, 0);
                                int actualRef = topDirectors.get(referenceDirector.getValue().name);
                                topDirectors.put(referenceDirector.getValue().name, actualRef + 1);
                            }
                        }
                    }
                }
            }
        }

        int times = 0;

        while (times < 3) {
            int bigger = 0;
            String director1 = "";
            String director2 = "";

            for (Map.Entry<String, Integer> entry : topDirectors.entrySet()) {
                if (entry.getValue() >= bigger) {
                    bigger = entry.getValue();
                    director1 = entry.getKey();
                    for (Map.Entry<String, Integer> directorEntry : topDirectors.entrySet()) {
                        if (!directorEntry.getKey().equals(director1) && directorEntry.getValue() == bigger
                                && getLastName(directorEntry.getKey()).equals(getLastName(entry.getKey()))) {
                            director2 = directorEntry.getKey();
                        }
                    }
                }
            }
            if (director1.equals("")) {
                break;
            }
            output.append(director1).append(":").append(bigger).append("\n");
            output.append(director2).append(":").append(bigger).append("\n");
            topDirectors.remove(director1);
            topDirectors.remove(director2);

            times++;
        }
        return output.toString();
    }

    // *****************************************************************************
}
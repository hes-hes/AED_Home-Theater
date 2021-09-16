package pt.ulusofona.deisi.aedProj2020;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Movie {

    int id;
    String title = "";
    Date releaseDate;
    double budget;
    double duration;
    int votes = Integer.MIN_VALUE;
    double averageVotes = Integer.MIN_VALUE;

    HashMap<Integer,Actor> actors = new HashMap<>();
    HashMap<Integer,Director> directors = new HashMap<>();
    ArrayList<Genre> genres = new ArrayList<>();

    public Movie(){

    }

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Movie(int id, String title, double duration, long budget, Date releaseDate){
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.budget = budget;
        this.releaseDate = releaseDate;
    }

    public String[] getGenreBias(){
        int totalMasculino = 0, totalFemenino = 0;

        for(Map.Entry<Integer,Actor> actor : actors.entrySet()){
            if(actor.getValue().genre.equals("F")){
                totalFemenino++;
            }else{
                totalMasculino++;
            }
        }

        double percentagemMasculina = (100.0 * totalMasculino)/actors.size();
        double percentagemFemenina = (100.0 * totalFemenino)/actors.size();

        String[] bias = new String[2];
        if(percentagemFemenina > percentagemMasculina){
            bias[0] = "F";
            bias[1] = "" + (int) Math.round(percentagemFemenina);
            return bias;
        }
        bias[0] = "M";
        bias[1] = "" + (int) Math.round(percentagemMasculina);
        return bias;
    }

    public boolean hasDirector(String name){
        for(Map.Entry<Integer,Director> realizador : directors.entrySet()){
            if(realizador.getValue().name.equals(name)){
                return true;
            }
        }
        return false;
    }

    public int getDate(String key){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = dateFormat.format(releaseDate);
        String[] releaseDateSplitted = data.split("-");

        if("DAY".equals(key)){
            return Integer.parseInt(releaseDateSplitted[2]);
        }
        if("MONTH".equals(key)){
            return Integer.parseInt(releaseDateSplitted[1]);
        }
        if("YEAR".equals(key)){
            return Integer.parseInt(releaseDateSplitted[0]);
        }
        return 0;
    }

    public int getMaleActors(){
        int total = 0;
        for(Map.Entry<Integer,Actor> actor : actors.entrySet()){
            if("M".equals(actor.getValue().genre)){
                total++;
            }
        }
        return total;
    }

    public int getFemaleActors(){
        int total = 0;
        for(Map.Entry<Integer,Actor> actor : actors.entrySet()){
            if("F".equals(actor.getValue().genre)){
                total++;
            }
        }
        return total;
    }

    @Override
    public String toString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(releaseDate);
        return id + " | " + title + " | " + date + " | " + genres.size() + " | "
                + directors.size() + " | " + getMaleActors() + " | " + getFemaleActors();
    }

    public String toString1(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(releaseDate);
        return id + " | " + title + " | " + date;
    }

}

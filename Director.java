package pt.ulusofona.deisi.aedProj2020;

public class Director {

    int directorId;
    String name;
    int movieId;

    public Director(int id, String name, int movieId){
        this.directorId = id;
        this.name = name;
        this.movieId = movieId;
    }

    public String getApelido(){
        String[] surnames = name.split(" ");
        return surnames[surnames.length-1];
    }
}

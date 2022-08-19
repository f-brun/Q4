package org.patarasprod.q4;

public class Question {
    String intitule;
    String[] reponses;
    int difficulte;
    int noBonneReponse;
    String langue;

    public Question(String intitule, String[] reponses) {
        this.intitule = intitule;
        this.reponses = reponses;
        this.difficulte = 0;
        this.noBonneReponse = 0;
        this.langue = "Français";
    }

    public Question(String intitule, String[] reponses, int noBonneReponse) {
        new Question(intitule, reponses);
        this.noBonneReponse = noBonneReponse;
    }
}

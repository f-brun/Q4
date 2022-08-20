package org.patarasprod.q4;

import java.util.concurrent.ThreadLocalRandom;

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

    public void melange_reponses() {
        // Mélange de l'ordre
        int j;
        String temp ;
        for (int i = 3 ; i >= 0 ; i--) {
            j = ThreadLocalRandom.current().nextInt(i+1);
            temp = this.reponses[i];
            this.reponses[i] = this.reponses[j];
            this.reponses[j] = temp;
            // Détermine si la bonne réponse a bougée
            if (i == this.noBonneReponse) {
                this.noBonneReponse = j;
            } else if (j == this.noBonneReponse) this.noBonneReponse = i ;
        }
    }

    public boolean est_correct(int noReponse) {
        return noReponse == this.noBonneReponse;
    }
}

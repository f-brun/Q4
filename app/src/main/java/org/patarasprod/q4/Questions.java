package org.patarasprod.q4;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Questions {
    String nomFichier, nomFichierJSON, nomFichierQDispo;
    ArrayList<Integer> questionsDisponibles;
    int nbQuestionsTotales;
    JSONObject fichierQuestions;
    JSONArray listeQuestions;
    Config cfg;

    public Questions(String nomFichier, Config cfg) {
        this.nomFichier = nomFichier;
        this.nomFichierJSON = nomFichier + ".json";
        this.nomFichierQDispo = nomFichier + "_disponibles.bin";
        this.cfg = cfg;
        try {
            lecture_fichier_questions();
            nbQuestionsTotales = listeQuestions.length();
            lecture_fichier_questions_disponibles();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void lecture_fichier_questions() throws FileNotFoundException {
        String contenu;
        InputStream fichier ;
        try {
            fichier = cfg.manager.open(nomFichierJSON);
            Scanner sc = new Scanner(fichier);
            sc.useDelimiter("\\Z");
            contenu = sc.next();  // Lit le contenu jusqu'au délimiteur
            try {
                fichierQuestions = new JSONObject(contenu);
//                String langue = fichierQuestions.getString("Langue");
                listeQuestions = fichierQuestions.getJSONArray("Questions");
            } catch (JSONException e) {
                System.out.println("Impossible d'analyser le fichier des questions (" +
                        nomFichierJSON + ")");
                e.printStackTrace();
            }
            sc.close();
            try {
                fichier.close();
            } catch (IOException ex) {
                System.out.println("Impossible de fermer le fichier des questions (" +
                        nomFichierJSON + ")");
                ex.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Impossible d'ouvrir le fichier des questions (" +
                    nomFichierJSON + ")");
            e.printStackTrace();
        }
    }

    private void initialiseQuestionsDisponibles() {
        for (int i = 0 ; i < nbQuestionsTotales ; i++) {
            questionsDisponibles.add(i);
        }
        System.out.println("Ré-initialisation des questions disponibles...");
    }

    private void lecture_fichier_questions_disponibles() {
        byte[] contenu;
        int noQuestion;
        questionsDisponibles = new ArrayList<>();
        File rep = cfg.repertoireFichiers;
        try {
            File fichier = new File(rep,nomFichierQDispo); // Récupère le fichier s'il existe
            int tailleFichier = (int) fichier.length();     // Détermine sa taille en octets
            contenu = new byte[tailleFichier];              // Réserve la mémoire pour le stocker
            FileInputStream f_in = new FileInputStream(fichier);
            f_in.read(contenu);                             // Lecture de la totalité du fichier
            f_in.close();
            // On boucle sur le fichier
            for (int i = 0 ; i < tailleFichier ; i += 4) {
                // On reconstitue le numéro de question à partir des 4 octets
                noQuestion = (contenu[i] << 24) + (contenu[i+1] << 16) +
                             (contenu[i+2] << 8) + (contenu[i+3]);
                questionsDisponibles.add(noQuestion);   // Et on l'ajoute aux disponibles
            }
        }
        catch (IOException e) {
            // Erreur : il faut créer un fichier vide
            System.out.println("Création d'un fichier vide pour " + nomFichierQDispo);
            initialiseQuestionsDisponibles();
            enregistre_questions_disponibles();
        }
    }

    /**
     * Choisi au hasard une question parmi celles disponibles (non encore posées) et actualise
     * la liste des questions disponibles en conséquence.
     * @return question prise au hasard parmi les questions disponibles (non encore posées)
     */
    public Question choisiQuestion() {
        // S'il n'y a plus de questions disponibles, on ré-initialise la liste
        if (questionsDisponibles.isEmpty()) {
            initialiseQuestionsDisponibles();
            enregistre_questions_disponibles();
        }
        // Tire le numéro de la question à poser parmi les disponibles
        int index = cfg.alea.nextInt(questionsDisponibles.size());
        int noQuestion = (int) questionsDisponibles.get(index);
        questionsDisponibles.remove(index);                    // La retire des disponibles

        JSONObject question_json;
        try {
            question_json = listeQuestions.getJSONObject(noQuestion);
            String intitule = question_json.getString("Intitulé");
            JSONArray reponses_json = question_json.getJSONArray("Réponses");

            // Conversion du JSONArray en String[]
            String[] reponses = new String[reponses_json.length()];
            for (int i = 0 ; i < reponses_json.length() ; i++) {
                reponses[i] = reponses_json.getString(i);
            }
            // Création de l'objet Question
            return new Question(intitule, reponses);

        } catch (JSONException e) {
            System.out.println("Problème de parsing du fichier json");
            e.printStackTrace();
        }
        return null;
    }

    private void enregistre_questions_disponibles() {
        int noQuestion, nbQuestions ;
        try {
            FileOutputStream fichier = cfg.contexte.openFileOutput(
                    nomFichierQDispo, Context.MODE_PRIVATE);
            nbQuestions = questionsDisponibles.size();
            // On crée un tableau d'octets de 4 fois le nb de questions car on stocke
            // les numéros des questions sur 4 octets (long)
            byte[] numerosDisponibles = new byte[nbQuestions*4];
            for (int i = 0 ; i < nbQuestions ; i++) {
                noQuestion = (int) questionsDisponibles.get(i);
                numerosDisponibles[4*i] = (byte) (noQuestion >>> 24) ;
                numerosDisponibles[4*i+1] = (byte) (noQuestion >>> 16) ;
                numerosDisponibles[4*i+2] = (byte) (noQuestion >>> 8) ;
                numerosDisponibles[4*i+3] = (byte) noQuestion ;
            }
            fichier.write(numerosDisponibles);
            fichier.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Impossible de créer le fichier " + nomFichierQDispo);
            ex.printStackTrace();
        } catch (IOException ioException) {
            System.out.println("I/O error sur le fichier " + nomFichierQDispo);
            ioException.printStackTrace();
        }
    }

    public void close() {
        enregistre_questions_disponibles();
    }
}


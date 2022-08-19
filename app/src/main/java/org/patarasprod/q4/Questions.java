package org.patarasprod.q4;

import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

public class Questions {
    TreeSet questionsDejaPosees;
    TreeSet questionsDisponibles;
    int nbQuestionsTotales;
    int nbQuestionsPosees;
    int nbQuestionsDisponibles;
    JSONObject fichierQuestions;
    JSONArray listeQuestions;

    public Questions(String nomFichier, AssetManager manager) {
        try {
            lecture_fichier_questions(nomFichier, manager);
            lecture_fichier_questions_disponibles(nomFichier, manager);
            questionsDejaPosees = new TreeSet();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void lecture_fichier_questions(String nomFichier, AssetManager manager)
            throws FileNotFoundException {
        String contenu;
        InputStream fichier = null;
        try {
            fichier = manager.open(nomFichier + ".json");
            Scanner sc = null;
            sc = new Scanner(fichier);
            sc.useDelimiter("\\Z");
            contenu = sc.next();  // Lit le contenu jusqu'au délimiteur
            try {
                fichierQuestions = new JSONObject(contenu);
                String langue = fichierQuestions.getString("Langue");
                listeQuestions = fichierQuestions.getJSONArray("Questions");
            } catch (JSONException e) {
                System.out.println("Impossible d'analyser le fichier des questions");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("Impossible d'ouvrir le fichier des questions");
            e.printStackTrace();
        }
    }

    private void lecture_fichier_questions_disponibles(String nomFichier, AssetManager manager) {
        String contenu;
        InputStream fichier = null;
        questionsDisponibles = new TreeSet();
        try {
            fichier = manager.open(nomFichier + "_disponibles.txt");
            Scanner sc = null;
            sc = new Scanner(fichier);
            sc.useDelimiter("\\Z");
            contenu = sc.next();  // Lit le contenu jusqu'au délimiteur
            // Puis ajoute les lignes du fichier à l'ensemble des questions disponibles
            questionsDisponibles.addAll(Arrays.asList(contenu.split("\r\n")));
            nbQuestionsDisponibles = questionsDisponibles.size();
        }
        catch (IOException e) {
            // Erreur : il faut créer un fichier vide
            System.out.println("Création d'un fichier vide pour " + nomFichier);
        }
    }

    public Question choisiQuestion() {
        // Tire le numéro de la question à poser parmi les disponibles
        int noQuestion = ThreadLocalRandom.current().nextInt(nbQuestionsDisponibles);
        questionsDisponibles.remove(noQuestion);  // La retire des disponibles
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

}


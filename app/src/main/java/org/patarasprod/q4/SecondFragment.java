package org.patarasprod.q4;

import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.patarasprod.q4.databinding.FragmentSecondBinding;
import java.util.concurrent.ThreadLocalRandom;

public class SecondFragment extends Fragment implements View.OnClickListener {

    private FragmentSecondBinding binding;
    public Button[] listeBtnReponses  ;
    public int reponseCorrecte;
    public int reponseSelectionnee;
    public Question question;
    private int[] LONGUEUR_TEXTE_BOUTON = {120, 80, 60, 45, 36, 23, 11};
    // Taille du texte sur 1, 2,3,4 ou 5 lignes et +
    private int[][] TAILLE_TEXTE = {{11, 12, 16, 18, 18, 20, 30},
                                    {11, 15, 15, 18, 20, 30, 30},
                                    {11, 15, 18, 20, 20, 30, 30},
                                    {11, 14, 18, 18, 20, 30, 30},
                                    {11, 14, 18, 18, 20, 30, 30}};

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

 */
        // Initialise le tableau des bouttons réponse
        listeBtnReponses = new Button[4];
        listeBtnReponses[0] = binding.btnReponseA;
        listeBtnReponses[1] = binding.btnReponseB;
        listeBtnReponses[2] = binding.btnReponseC;
        listeBtnReponses[3] = binding.btnReponseD;

        for (int i = 0 ; i < 4 ; i++) {
            listeBtnReponses[i].setOnClickListener(this::onClick);
        }
        MainActivity.barreTitre.setTitle("Score : " + MainActivity.score);
        affiche_question();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private int determine_taille_texte(String texte) {
        int longueur = texte.length();
        int nbLignes = 0;  // Nb de lignes possibles dans le texte (parties séparées)
        for (int i = 0 ; i < longueur ; i++) {
            if (texte.charAt(i) == ' ' || texte.charAt(i) == '-') nbLignes++;
        }
        if (nbLignes >= TAILLE_TEXTE.length) nbLignes = TAILLE_TEXTE.length - 1;
        int taille = TAILLE_TEXTE[0][0];
        int i = 0;
        while (i < LONGUEUR_TEXTE_BOUTON.length && longueur < LONGUEUR_TEXTE_BOUTON[i]) {
            taille = TAILLE_TEXTE[nbLignes][i];
            i++;
        }
        System.out.println(texte + " ("+ texte.length() +") -> " + taille);
        return taille;
    }

    public void affiche_question() {
        question = MainActivity.questions.choisiQuestion();
        question.melange_reponses();
        /*
        int[] ordreReponses = new int[4];

        // Initialisation du tableau de l'ordre des réponses
        for (int i = 0 ; i < 4 ; i++) ordreReponses[i] = i ;

        // Mélange de l'ordre
        int j, temp ;
        for (int i = 3 ; i >= 0 ; i--) {
            j = ThreadLocalRandom.current().nextInt(i+1);
            temp = ordreReponses[i];
            ordreReponses[i] = ordreReponses[j];
            ordreReponses[j] = temp;
        }

        // Détermination de la nouvelle position de la bonne réponse (la première initialement)
        for (int i = 0 ; i < 4 ; i++) {
            if (ordreReponses[i] == 0) reponseCorrecte = i;
        }
*/
        // Ecrit l'intitulé de la question dans le TextView
        binding.TextViewIntituleQuestion.setText((CharSequence) question.intitule);

        // Fixe les textes des boutons réponse
        for (int i = 0 ; i < 4 ; i++) {
            listeBtnReponses[i].setText(question.reponses[i]);
            int taille = determine_taille_texte(question.reponses[i]);
            listeBtnReponses[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, taille);
        }
        reponseSelectionnee = -1; // Aucune réponse sélectionnée
    }


    @Override
    public void onClick(View view) {
        switch (view.getId() /*to get clicked view id**/) {
            case R.id.btn_ReponseA:
                reponseSelectionnee = 0;
                break;
            case R.id.btn_ReponseB:
                reponseSelectionnee = 1;
                break;
            case R.id.btn_ReponseC:
                reponseSelectionnee = 2;
                break;
            case R.id.btn_ReponseD:
                reponseSelectionnee = 3;
                break;
            default:
                break;
        }
        if (question.est_correct(reponseSelectionnee)) {
            // Rend le bouton de la réponse vert
            listeBtnReponses[reponseSelectionnee].setBackgroundColor(0xFF00FF00);
            MainActivity.score ++;
            MainActivity.barreTitre.setTitle("Score : " + MainActivity.score);
        } else if (reponseSelectionnee >= 0) {
            listeBtnReponses[reponseSelectionnee].
                    setBackgroundColor(0xFFFF0000);
        } else return ;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                listeBtnReponses[reponseSelectionnee].setBackgroundColor(MainActivity.coulBoutons);
                affiche_question();  // Passe à la question suivante
            }
        }, 1000); //1000 ms = 1.0 seconde avant de lancer le runnable


    }
}
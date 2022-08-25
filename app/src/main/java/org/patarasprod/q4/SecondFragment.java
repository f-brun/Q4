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

import org.patarasprod.q4.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment implements View.OnClickListener {

    Config cfg;
    private FragmentSecondBinding binding;
    public Button[] listeBtnReponses  ;
    public int reponseSelectionnee;
    private boolean reponsePossible ;
    public Question question;
    final private int[] LONGUEUR_TEXTE_BOUTON = {120, 80, 60, 45, 36, 23, 11};
    // Taille du texte sur 1, 2,3,4 ou 5 lignes et +
    final private int[][] TAILLE_TEXTE = {{11, 12, 16, 18, 18, 20, 30},
                                    {11, 15, 15, 18, 20, 30, 30},
                                    {11, 15, 18, 20, 20, 30, 30},
                                    {11, 14, 18, 18, 20, 30, 30},
                                    {11, 14, 18, 18, 20, 30, 30}};

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
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
        this.cfg = ((MainActivity) requireActivity()).recupere_configuration();
        // Initialise le tableau des bouttons réponse
        listeBtnReponses = new Button[4];
        listeBtnReponses[0] = binding.btnReponseA;
        listeBtnReponses[1] = binding.btnReponseB;
        listeBtnReponses[2] = binding.btnReponseC;
        listeBtnReponses[3] = binding.btnReponseD;

        for (int i = 0 ; i < 4 ; i++) {
            listeBtnReponses[i].setOnClickListener(this);
        }
        cfg.barreTitre.setTitle("Score : " + cfg.score);
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
        question = cfg.questions.choisiQuestion();
        question.melange_reponses();

        // Ecrit l'intitulé de la question dans le TextView
        binding.TextViewIntituleQuestion.setText((CharSequence) question.intitule);

        // Fixe les textes des boutons réponse (valeur, taille et couleur du fond)
        for (int i = 0 ; i < 4 ; i++) {
            listeBtnReponses[i].setText(question.reponses[i]);
            int taille = determine_taille_texte(question.reponses[i]);
            listeBtnReponses[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, taille);
            listeBtnReponses[i].setBackgroundColor(cfg.coulBoutons);
        }
        reponseSelectionnee = -1; // Aucune réponse sélectionnée
        reponsePossible = true;   // Autorise les réponses
    }


    @Override
    public void onClick(View view) {
        // On détermine le bouton cliqué
        int[] idBoutons = {R.id.btn_ReponseA,R.id.btn_ReponseB,R.id.btn_ReponseC,R.id.btn_ReponseD};
        int idObjetClique = view.getId();
        for (int i = 0 ; i < idBoutons.length ; i++) {
            if (idBoutons[i] == idObjetClique) reponseSelectionnee = i;
        }
        if (reponsePossible && question.est_correct(reponseSelectionnee)) {
            // Si la réponse est correcte on met un fond vert au bouton de la réponse
            listeBtnReponses[reponseSelectionnee].setBackgroundColor(0xFF00FF00);
            cfg.score ++;
            cfg.barreTitre.setTitle("Score : " + cfg.score);
            reponsePossible = false;    // Il n'est plus possible de répondre à cette question
        } else if (reponsePossible && reponseSelectionnee >= 0) {
            // Sinon si c'est une réponse fausse on met un fond rouge au bouton
            listeBtnReponses[reponseSelectionnee].setBackgroundColor(0xFFFF0000);
            reponsePossible = false;    // Il n'est plus possible de répondre à cette question
        } else return ;

        Handler handler = new Handler();
        // Passe à la question suivante
        handler.postDelayed(this::affiche_question, 1000); //1000 ms = 1.0 seconde avant de lancer le runnable
    }
}
package org.patarasprod.q4;

import android.content.Context;
import android.content.res.AssetManager;
import androidx.appcompat.app.ActionBar;
import java.io.File;
import java.util.Random;

public class Config {
    protected Context contexte;
    protected String NOM_FICHIER_QUESTIONS = "Questions";
    protected AssetManager manager;
    protected File repertoire;
    protected ActionBar barreTitre;
    protected int score ;
    protected int coulBoutons;
    protected File repertoireFichiers;
    protected Questions questions; // Objet permettant l'interrogation de la base de questions
    protected Random alea;

}

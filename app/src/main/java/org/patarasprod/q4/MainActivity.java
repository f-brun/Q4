package org.patarasprod.q4;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONException;
import org.json.JSONObject;
import org.patarasprod.q4.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

// Bibliothèque pour les fichiers json
import org.json.JSONArray;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {

    protected static Context context;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    static private String NOM_FICHIER_QUESTIONS = "Questions";
    static AssetManager manager;
    static File repertoire;
    static ActionBar barreTitre;
    static int score ;
    static int coulBoutons;
    static File repertoireFichiers;
    static Questions questions; // Objet permettant l'interrogation de la base de questions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        initialisations();
    }

    private void initialisations() {
        repertoire = getFilesDir();
        manager = getAssets();
        MainActivity.context = getApplicationContext();
        barreTitre = getSupportActionBar();
        coulBoutons = ((ColorDrawable) binding.toolbar.getBackground()).getColor();
        repertoireFichiers = getFilesDir();
        score = 0;
        // Initialise le système de questions
        questions = new Questions(NOM_FICHIER_QUESTIONS, manager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*
    public static void lecture_fichier_questions(String nom_fichier) throws FileNotFoundException {
        String contenu;
        InputStream fichier = null;
        try {
            fichier = manager.open(NOM_FICHIER_QUESTIONS);
            Scanner sc = null;
            sc = new Scanner(fichier);
            // we just need to use \\Z as delimiter
            sc.useDelimiter("\\Z");
            contenu = sc.next();
            try {
                MainActivity.fichierQuestions = new JSONObject(contenu);
                String langue = MainActivity.fichierQuestions.getString("Langue");
                MainActivity.listeQuestions = MainActivity.fichierQuestions.getJSONArray("Questions");
            } catch (JSONException e) {
                System.out.println("Impossible d'analyser le fichier des questions");
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("Impossible d'ouvrir le fichier des questions");
            e.printStackTrace();
        }
    }

     */
}
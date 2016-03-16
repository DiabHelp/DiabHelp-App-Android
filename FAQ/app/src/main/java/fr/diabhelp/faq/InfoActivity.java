package fr.diabhelp.faq;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

/**
 * Created by naqued on 05/03/16.
 */
public class InfoActivity extends AppCompatActivity {

    private String Title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getExtras();
         Title = extra.getString("title");

        if (Title.compareTo("Savoir Réagir face à un malaise") == 0)
            setContentView(R.layout.activity_howtoreact);
        if (Title.compareTo("Cigarette/Alcool/Autres.. Que se passe t'il ?") == 0)
            setContentView(R.layout.activity_cig_alco_dro);
        else if (Title.compareTo("Diabète égale interdiction ?\nSavoir gérer son repas") == 0)
            setContentView(R.layout.activity_interdictionrepas);
        else if (Title.compareTo("Aide financiere lié au Diabète") == 0)
            setContentView(R.layout.activity_finance_help);
        else if (Title.compareTo("Sexualité / Grossesse / Contraception") == 0)
            setContentView(R.layout.activity_sgc);
        else if (Title.compareTo("Diabète de Type 1") == 0)
            setContentView(R.layout.activity_diabetetype1);
        else if (Title.compareTo("Diabète de Type 2") == 0)
            setContentView(R.layout.activity_diabetetype2);
        else if (Title.compareTo("Diabète gestationelle") == 0)
            setContentView(R.layout.activity_diabetegestationelle);
        else if (Title.compareTo("Apprivoiser la maladie") == 0)
            setContentView(R.layout.activity_apprivoiser);
        else if (Title.compareTo("Trucs et Astuces pour ne pas se faire surprendre") == 0)
            setContentView(R.layout.activity_apprivoiser);
        else
            Log.e("error", "no title found : " + Title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // Redirige sur le site de l'ajd
    public void launch_more(View v)
    {
        Uri uri = null;

        if (Title.compareTo("Aide financiere Diabete") == 0)
            uri = Uri.parse("http://www.ajd-diabete.fr/le-diabete/vivre-avec-le-diabete/les-aides-sociales/#Prestation_de_compensation_du_handicap_PCH");
        else if (Title.compareTo("Diabète de Type 1") == 0 || Title.compareTo("Diabète de Type 2") == 0)
            uri = Uri.parse("http://www.afd.asso.fr/diabete");
        else if (Title.compareTo("Diabète gestationelle") == 0)
            uri = Uri.parse("http://www.afd.asso.fr/diabete/gestationnel");
        else if (Title.compareTo("Apprivoiser la maladie") == 0)
            uri = Uri.parse("http://diabete.fr/adultes/mon-diabete/apprivoiser-la-maladie");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}

package cat.udl.gtidic.course2223.teacher.thehangman.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cat.udl.gtidic.course2223.teacher.thehangman.R;
import cat.udl.gtidic.course2223.teacher.thehangman.models.Game;
import cat.udl.gtidic.course2223.teacher.thehangman.views.IniciActivity;

public class MainActivity extends AppCompatActivity {

    Button btnNewLetter;
    TextView visibleWord;
    TextView lettersChosen;
    EditText etNewLetter;
    ImageView ivState;
    Game game;
    String name;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = getIntent().getStringExtra("DATA_NAME");


//        here is a good place to implement MVVM if someone is interested

//        initializing views
        btnNewLetter = findViewById(R.id.btnNewLetter);
        btnNewLetter.setOnClickListener(v -> newLetter());
        visibleWord = findViewById(R.id.tvVisibleWord);
        lettersChosen = findViewById(R.id.tvLettersChosen);
        etNewLetter = findViewById(R.id.etNewLetter);
        ivState = findViewById(R.id.ivState);
        userName = findViewById(R.id.userName);

        userName.setText("User: " + name);

//        starting game mechanics
        //System.out.println("My name is: " + name);
        startGame();
    }

    /**
     * Retorna el Drawable segons l'estat correcte
     */
    private Drawable getDrawableFromState(int state){
        int r_desired = -1;

        switch (state){
            case 0: r_desired = R.drawable.round_0; break;
            case 1: r_desired = R.drawable.round_1; break;
            case 2: r_desired = R.drawable.round_2; break;
            case 3: r_desired = R.drawable.round_3; break;
            case 4: r_desired = R.drawable.round_4; break;
            case 5: r_desired = R.drawable.round_5; break;
            case 6: r_desired = R.drawable.round_6; break;
            case 7: r_desired = R.drawable.round_7; break;
        }
        return ContextCompat.getDrawable(this, r_desired);
    }

    /**
     * Actualitza les views de la pantalla
     */
    private void refreshWords(){
        visibleWord.setText(game.visibleWord());
        lettersChosen.setText(game.lettersChosen());
        ivState.setImageDrawable(getDrawableFromState(game.getCurrentRound()));
    }

    /**
     * Afegeix la lletra al joc
     */
    private void newLetter(){
        String novaLletra = etNewLetter.getText().toString().toUpperCase();
        etNewLetter.setText("");

        int validLetter = game.addLetter(novaLletra);
        if (validLetter != Game.LETTER_VALIDATION_OK){
            Log.d(Game.TAG, "Lletra no vàlida");
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;
            if (validLetter == Game.LETTER_VALIDATION_NO_VALID_BECAUSE_ALREADY_SELECTED){
                Toast toast1 = Toast.makeText(context, "Ja has seleccionat aquest caràcter!", duration);
                toast1.show();
            } else {
                Toast toast1 = Toast.makeText(context, "Has de ficar un caràcter!", duration);
                toast1.show();            }
        }
        Log.d(Game.TAG, "Estat actual: " + game.getCurrentRound());

        refreshWords();
        hideKeyboard();
        checkGameOver();
    }

    /**
     * Revisa si el joc ha acabat i informa via Log (de moment)
     */
    private void checkGameOver(){
        if (game.isPlayerTheWinner()){
            Log.d(Game.TAG, "El jugador ha guanyat!");
            finishActivityGame();
        }

        if (game.isGameOver()){
            Log.d(Game.TAG, "El Joc ha acabat");
            btnNewLetter.setEnabled(false);
            etNewLetter.setEnabled(false);
            finishActivityGame();
        }
    }

    /**
     * Métode per anar a l'activity IniciActivity
     */

    public void finishActivityGame(){
        //Intent i = new Intent(this, IniciActivity.class);
        //startActivity(i);
        finish();
    }

    /**
     * Inicia el joc i actualitza l'activitat
     */
    private void startGame(){
        game = new Game();
        refreshWords();
    }

    /* -------- METODES AUXILIARS --------- */

    /**
     * Amaga el teclat virtual de la pantalla
     */
    private void hideKeyboard(){
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
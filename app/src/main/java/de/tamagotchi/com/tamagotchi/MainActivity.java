package de.tamagotchi.com.tamagotchi;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, IGameActions {
    private static final int PROGRESS = 0x1;

    private CountDownTimer mCountDownTimerRest;
    private CountDownTimer mCountDownTimerHappiness;

    private ProgressBar happinessProgress;
    private ImageButton happinessButton;

    private ProgressBar restProgress;
    private ImageButton restButton;

    private ProgressBar foodProgress;
    private ImageButton foodButton;

    private ProgressBar healtProgress;
    private ImageButton healtButton;

    private HappyBehaviour happyB;
    private RestBehaviour restB;

    private FoodBehaviour foodB;
    private HealthBehaviour healtB;

    private TextView happinessText;
    private int mhappinessProgressStatus = 100;
    private int mrestProgressStatus= 100;
    private Button newstartButton ;
    private ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        happinessProgress = (ProgressBar) findViewById(R.id.happinesslevel);
        happinessButton = (ImageButton) findViewById(R.id.happinessButton);
        happinessButton.setOnClickListener(this);

        restProgress = (ProgressBar) findViewById(R.id.restlevel);
        restButton = (ImageButton) findViewById(R.id.restButton);
        restButton.setOnClickListener(this);

        foodProgress = (ProgressBar) findViewById(R.id.Foodlevel);
        foodButton = (ImageButton) findViewById(R.id.foodbutton);
        foodButton.setOnClickListener(this);

        healtProgress = (ProgressBar) findViewById(R.id.healthlevel);
        healtButton = (ImageButton) findViewById(R.id.healtButton);
        healtButton.setOnClickListener(this);

        newstartButton = (Button) findViewById(R.id.newstart);
        newstartButton.setOnClickListener(this);

        startGame();

        img = (ImageView) findViewById(R.id.tamagotchi);
    }

    public void startGame(){
        happyB  = new HappyBehaviour(happinessProgress,this);
        restB   = new RestBehaviour(restProgress,this);
        foodB   = new FoodBehaviour(foodProgress,this);
        healtB  = new HealthBehaviour(healtProgress,this);
        happyB.start();
        restB.start();
        foodB.start();
        healtB.start();

        happinessButton.setEnabled(true);
        restButton.setEnabled(true);
        foodButton.setEnabled(true);
        healtButton.setEnabled(true);

        newstartButton.setVisibility(View.INVISIBLE);
    }
    public void stopGame(){
        happyB.cancel();
        restB.cancel();
        foodB.cancel();
        healtB.cancel();

        happinessButton.setEnabled(false);
        restButton.setEnabled(false);
        healtButton.setEnabled(false);
        foodButton.setEnabled(false);

        newstartButton.setVisibility(View.VISIBLE);
        img.setImageResource(R.drawable.dead);
    }

    public void restartGame(){
        stopGame();
        startGame();
    }

    class Behaviour extends CountDownTimer {

        private long countdownPeriod;
        private ProgressBar progressBar;

        private int warningLevel;
        private IGameActions gameActions;
        private int value ;
        private static final int TOTAL_TIME = 100000;
        private static final int TICK_TIME = 1000;

        public Behaviour(long countdownPeriod,ProgressBar progressBar,int warningLevel,IGameActions gameActions){
            super(TOTAL_TIME,TICK_TIME);
            this.countdownPeriod = countdownPeriod;
            this.progressBar = progressBar;
            this.warningLevel = warningLevel;
            this.gameActions = gameActions;
            this.value = 100;
            this.progressBar.setProgress(this.value);
            this.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }

        @Override
        public void onTick(long timeRemaining) {
            this.value -=10;
            this.progressBar.setProgress(this.value);
            if(this.value < this.warningLevel) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            }
            if(this.value > this.warningLevel) {
                progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            }
            if(isDead()) {
                gameActions.stopGame();
            }
        }

        @Override
        public void onFinish() {
            this.value = 0;
        }

        public void increase(){
            this.value +=1;
            this.progressBar.setProgress(this.value);
        }

        public ProgressBar getProgressBar(){
            return progressBar;
        }

        public int getValue(){
            return this.value;
        }

        public boolean isDead() {
            return this.value< (warningLevel/2);
        }
    }

    class HappyBehaviour extends Behaviour {
        public HappyBehaviour(ProgressBar progressBar,IGameActions gameActions){
            super(10000,progressBar,50,gameActions);
        }
    }

    class FoodBehaviour extends Behaviour {
        public FoodBehaviour(ProgressBar progressBar,IGameActions gameActions){
            super(10000,progressBar,30,gameActions);
        }
    }

    class RestBehaviour extends Behaviour {
        public RestBehaviour(ProgressBar progressBar,IGameActions gameActions){
            super(10000,progressBar,10,gameActions);
        }
    }

    class HealthBehaviour extends Behaviour {
        public HealthBehaviour(ProgressBar progressBar,IGameActions gameActions){
            super(10000,progressBar,20,gameActions);
        }
    }
    

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.happinessButton:{
                happyB.increase();
                if(happyB.isDead()){
                    happyB.cancel();
                }
                break;
            }
            case R.id.restButton:{
                restB.increase();
                if(restB.isDead()){
                    restB.cancel();
                }
                break;
            }
            case R.id.foodbutton:{
                foodB.increase();
                if(foodB.isDead()){
                    foodB.cancel();
                }
                break;
            }
            case R.id.healtButton: {
                healtB.increase();
                if (healtB.isDead()) {
                    healtB.cancel();
                }
                break;
            }
            case R.id.newstart: {
                restartGame();
            }
        }
    }
}

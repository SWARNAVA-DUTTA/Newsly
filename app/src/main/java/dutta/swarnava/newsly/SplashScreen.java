package dutta.swarnava.newsly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
//    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//        imgView = findViewById(R.id.imgView);
//        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation_fade);
//        imgView.startAnimation(animation);

//        Thread thread=new Thread(){
//
//            @Override
//            public void run() {
//                try {
//                    sleep(4000);
//                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
//                    startActivity(i);
//                    finish();
//                    super.run();
//                } catch (InterruptedException e) {
//                    // e.printStackTrace();
//                }
//            }
//        };
//        thread.start();
//
//    }
//}
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 4000);
    }
}

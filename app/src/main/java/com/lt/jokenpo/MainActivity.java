package com.lt.jokenpo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Publicidade publicidade;
    private TextView oi;
    private AdView adView;

    private TextView txtProcurandoOponente;
    private Handler handler = new Handler();
    private Button btnIniciarJogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Aqui você pode verificar se a splash screen já foi exibida e usar a transição
        setContentView(R.layout.activity_main); // O layout da activity principal será exibido após a Splash

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        publicidade = new Publicidade(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                // Anúncio inicializado
            }
        });

        btnIniciarJogo = findViewById(R.id.btn_iniciar_jogo);
        txtProcurandoOponente = findViewById(R.id.txt_procurando_oponente); // Adiciona a referência ao TextView

        btnIniciarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Desativa o botão para evitar múltiplos cliques
                btnIniciarJogo.setEnabled(false);

                // Mostra o texto "Procurando oponente..."
                txtProcurandoOponente.setVisibility(View.VISIBLE);

                // Carregar a animação
                Animation scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_animation);

                // Adicionar um listener para saber quando a animação termina
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // Pode ser usado para executar algo no início da animação, se necessário
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Gera um tempo de espera aleatório entre 3 e 5 segundos
                        Random random = new Random();
                        int delay = 3000 + random.nextInt(2000); // 3000 ms + [0, 2000] ms

                        // Aguarda o tempo aleatório e vai para a próxima Activity
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Iniciar a nova atividade após o atraso
                                Intent intent = new Intent(MainActivity.this, JogoActivity.class);
                                startActivity(intent);
                                finish(); // Finaliza a Activity atual, se necessário
                            }
                        }, delay);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // Pode ser usado para executar algo se a animação repetir, se necessário
                    }
                });

                // Iniciar a animação
                btnIniciarJogo.startAnimation(scaleAnimation);


            }
        });
    }
}

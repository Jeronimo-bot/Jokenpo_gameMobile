package com.lt.jokenpo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class JogoActivity extends AppCompatActivity {

    private Button btnPedra, btnPapel, btnTesoura, btnProximoOponente;
    private TextView txtResultado, txtOponente, txtScore, txtCronometro;
    private String[] escolhas = {"Rock", "Paper", "Scissors"};
    private int vitoriasJogador = 0, vitoriasOponente = 0;
    private boolean escolhaFeita = false; // Flag para verificar se o jogador escolheu
    private boolean oponenteEscolheu = false; // Flag para verificar se o oponente fez uma escolha
    private Handler handler = new Handler();
    private CountDownTimer countDownTimer;

    private Publicidade publicidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        publicidade = new Publicidade(this);

        btnPedra = findViewById(R.id.btn_pedra);
        btnPapel = findViewById(R.id.btn_papel);
        btnTesoura = findViewById(R.id.btn_tesoura);
        btnProximoOponente = findViewById(R.id.btn_proximo_oponente);
        txtResultado = findViewById(R.id.tv_result);
        txtOponente = findViewById(R.id.tv_oponente);
        txtScore = findViewById(R.id.tv_score);
        txtCronometro = findViewById(R.id.tv_cronometro);

        btnProximoOponente.setVisibility(View.GONE);

        iniciarCronometro(); // Iniciar o cronômetro ao começar o jogo

        btnPedra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolhaFeita = true;
                pararCronometro();  // Parar o cronômetro após a escolha
                jogar("Rock");
            }
        });

        btnPapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolhaFeita = true;
                pararCronometro();  // Parar o cronômetro após a escolha
                jogar("Paper");
            }
        });

        btnTesoura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolhaFeita = true;
                pararCronometro();  // Parar o cronômetro após a escolha
                jogar("Scissors");
            }
        });

        btnProximoOponente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publicidade.tryToShowRewardedAd();
                procurarNovoOponente();
            }
        });
    }

    private void iniciarCronometro() {
        // Cancelar o cronômetro anterior se já estiver em execução
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        escolhaFeita = false; // Resetar a escolha feita para a nova rodada
        oponenteEscolheu = false; // Resetar a escolha do oponente para a nova rodada

        // Iniciar um cronômetro de 10 segundos
        countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                txtCronometro.setText(millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                txtCronometro.setText("");

                // Se o jogador não fez uma escolha, o jogador perde
                if (!escolhaFeita) {
                    txtResultado.setText("You lost! Time's up.");
                    vitoriasOponente++;
                } else {
                    // Se o oponente não fez uma escolha, o oponente perde
                    if (!oponenteEscolheu) {
                        txtResultado.setText("You won! Opponent didn't make a choice.");
                        vitoriasJogador++;
                    }
                }

                // Atualizar a pontuação
                txtScore.setText("Player: " + vitoriasJogador + " | Opponent: " + vitoriasOponente);

                // Verificar se a rodada deve ser finalizada
                if (vitoriasJogador == 3 || vitoriasOponente == 3) {
                    finalizarRodada();
                } else {
                    setBotoesAtivos(true); // Reativar os botões
                    iniciarCronometro();  // Reiniciar o cronômetro para a próxima jogada
                }
            }
        }.start();
    }

    private void pararCronometro() {
        // Parar o cronômetro se estiver rodando
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void jogar(final String escolhaJogador) {
        setBotoesAtivos(false); // Desativar botões após a escolha

        int delayOponente = new Random().nextInt(2000) + 1000; // 1 a 3 segundos de delay para oponente

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                oponenteEscolheu = true; // Oponente fez uma escolha
                String escolhaOponente = escolhas[new Random().nextInt(escolhas.length)];
                determinarVencedor(escolhaJogador, escolhaOponente);
            }
        }, delayOponente);
    }

    private void determinarVencedor(String escolhaJogador, String escolhaOponente) {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancelar o cronômetro ao determinar o vencedor
        }

        if (escolhaOponente.equals("Nenhuma")) {
            // O oponente não fez uma escolha
            txtResultado.setText("You won! Opponent didn't make a choice.");
            vitoriasJogador++;
        } else if (escolhaJogador.equals(escolhaOponente)) {
            txtResultado.setText("It's a tie! Opponent chose " + escolhaOponente);
        } else if ((escolhaJogador.equals("Rock") && escolhaOponente.equals("Tesoura")) ||
                (escolhaJogador.equals("Paper") && escolhaOponente.equals("Rock")) ||
                (escolhaJogador.equals("Scissors") && escolhaOponente.equals("Paper"))) {
            vitoriasJogador++;
            txtResultado.setText("You won! Opponent chose " + escolhaOponente);
        } else {
            vitoriasOponente++;
            txtResultado.setText("You won! Opponent chose " + escolhaOponente);
        }

        txtScore.setText("Player: " + vitoriasJogador + " | Opponent: " + vitoriasOponente);

        if (vitoriasJogador == 3 || vitoriasOponente == 3) {
            finalizarRodada();
        } else {
            setBotoesAtivos(true); // Reativar os botões
            iniciarCronometro();  // Reiniciar o cronômetro para a próxima jogada
        }
    }

    private void finalizarRodada() {
        if (vitoriasJogador == 3) {
            txtResultado.setText("You won the round!");
        } else {
            txtResultado.setText("Opponent won the round!");
        }

        btnProximoOponente.setVisibility(View.VISIBLE);
        setBotoesAtivos(false);
        txtCronometro.setVisibility(View.GONE); // Ocultar o cronômetro
    }

    private void procurarNovoOponente() {
        txtResultado.setText("Searching for Opponent...");
        btnProximoOponente.setVisibility(View.GONE);
        setBotoesAtivos(false);
        txtCronometro.setVisibility(View.GONE); // Ocultar o cronômetro durante a procura

        int delayProximoOponente = new Random().nextInt(9000) + 2000; // 2 a 10 segundos de delay

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtResultado.setText("");
                txtCronometro.setVisibility(View.VISIBLE); // Mostrar o cronômetro novamente
                vitoriasJogador = 0;
                vitoriasOponente = 0;
                txtScore.setText("Player: " + vitoriasJogador + " | Opponent: " + vitoriasOponente);
                setBotoesAtivos(true);
                iniciarCronometro(); // Reiniciar o cronômetro ao encontrar novo oponente
            }
        }, delayProximoOponente);
    }

    private void setBotoesAtivos(boolean ativo) {
        btnPedra.setEnabled(ativo);
        btnPapel.setEnabled(ativo);
        btnTesoura.setEnabled(ativo);
    }
}

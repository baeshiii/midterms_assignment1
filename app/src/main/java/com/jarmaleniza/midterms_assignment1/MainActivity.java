package com.jarmaleniza.midterms_assignment1;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private int currentPosition = 0;
    int customColor = Color.rgb(144, 238, 144);
    private MediaPlayer mediaPlayer;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = findViewById(R.id.tableLayout);
        final TextView statusTextView = findViewById(R.id.statusTextView);
        Button playButton = findViewById(R.id.playButton);
        Button resetButton = findViewById(R.id.resetButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == 60) {
                    Toast.makeText(MainActivity.this, "The game is already finished!", Toast.LENGTH_LONG).show();
                    return;
                }

                Random random = new Random();
                int diceRoll = random.nextInt(6) + 1;

                int drawableResourceId = getResources().getIdentifier("dice_" + diceRoll, "drawable", getPackageName());
                Drawable drawable = getResources().getDrawable(drawableResourceId, null);

                int newWidth = (int) getResources().getDimension(R.dimen.dice_width);
                int newHeight = (int) getResources().getDimension(R.dimen.dice_height);
                drawable.setBounds(0, 0, newWidth, newHeight);

                statusTextView.setCompoundDrawables(drawable, null, null, null);
                statusTextView.setText("");

                unhighlightPosition(currentPosition);
                currentPosition += diceRoll;

                if (currentPosition > 60) {
                    int excess = currentPosition - 60;
                    currentPosition = 60 - excess;
                }

                highlightPosition(currentPosition);

                if (currentPosition == 60) {
                    playVictorySound();
                    Toast.makeText(MainActivity.this, "You Win! Press the reset button to play again", Toast.LENGTH_LONG).show();
                    playButton.setEnabled(false);
                }
            }
        });


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTextView.setCompoundDrawables(null, null, null, null);
                unhighlightPosition(currentPosition);
                currentPosition = 0;
                playButton.setEnabled(true);
            }
        });


        int number = 60;
        boolean isDescending = true;

        while (number > 0) {
            TableRow tableRow = new TableRow(this);

            if (isDescending) {
                int startNumber = number;
                for (int i = 0; i < 5 && number > 0; i++) {
                    TextView textView = new TextView(this);
                    if (startNumber - i == 60) {
                        textView.setText("Finish");
                    } else if (number - i == 1) {
                        textView.setText("Start");
                    } else {
                        textView.setText(String.valueOf(startNumber - i));
                    }
                    textView.setPadding(25, 25, 25, 25);
                    textView.setId(startNumber - i);
                    tableRow.addView(textView, new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
                }
                number -= 5;
            } else {
                int startNumber = number - 4;
                for (int i = 0; i < 5 && startNumber <= number; i++) {
                    TextView textView = new TextView(this);
                    if (startNumber + i == 60) {
                        textView.setText("Finish");
                    } else if (startNumber + i == 1) {
                        textView.setText("Start");
                    } else {
                        textView.setText(String.valueOf(startNumber + i));
                    }
                    textView.setPadding(25, 25, 25, 25);
                    textView.setId(startNumber + i);
                    tableRow.addView(textView, new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
                }
                number -= 5;
            }

            isDescending = !isDescending;
            tableLayout.addView(tableRow);
        }
    }


    private void createGameBoard() {
        int number = 60;
        boolean isDescending = true;

        while (number > 0) {
            TableRow tableRow = new TableRow(this);

            if (isDescending) {
                int startNumber = number;
                for (int i = 0; i < 5 && number > 0; i++) {
                    TextView textView = createGameCell(startNumber - i);
                    tableRow.addView(textView, new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
                }
                number -= 5;
            } else {
                int startNumber = number - 4;
                for (int i = 0; i < 5 && startNumber <= number; i++) {
                    TextView textView = createGameCell(startNumber + i);
                    tableRow.addView(textView, new TableRow.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));
                }
                number -= 5;
            }

            isDescending = !isDescending;
            tableLayout.addView(tableRow);
        }
    }


    private TextView createGameCell(int cellNumber) {
        TextView textView = new TextView(this);
        if (cellNumber == 60) {
            textView.setText("Finish");
        } else if (cellNumber == 1) {
            textView.setText("Start");
        } else {
            textView.setText(String.valueOf(cellNumber));
        }
        textView.setPadding(16, 16, 16, 16);
        textView.setId(cellNumber);
        return textView;
    }


    private void highlightPosition(int position) {
        TextView cell = findViewById(position);
        if (cell != null) {
            cell.setBackgroundColor(customColor);

            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.roll);
            mediaPlayer.start();
        }
    }

    private void unhighlightPosition(int position) {
        TextView cell = findViewById(position);
        if (cell != null) {
            cell.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    private void playVictorySound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.finish);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
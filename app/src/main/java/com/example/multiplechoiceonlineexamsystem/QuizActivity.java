package com.example.multiplechoiceonlineexamsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    public RadioGroup radio_group;
    public RadioButton option1_radio_button;
    public RadioButton option2_radio_button;
    public RadioButton option3_radio_button;
    public ImageView next_button;
    public ImageView check_score_button;
    public ImageView score_image;
    public ImageView fail_pass_image;
    public ImageView time_out_image;
    private Handler handler;
    private Handler timeHandler;
    public TextView text_view_question;
    public TextView text_view_question_count;
    public TextView text_view_countdown;
    public TextView  score_text;
    public TextView  score_title_text;
    public String score;
    CountDownTimer countDownTimer;
    public SwipeRefreshLayout swipeRefreshLayout;

    public static final int SERVERPORT = 3003;
    public static final String SERVER_IP = "172.20.10.2";
    private QuizActivity.ClientThread clientThread;
    private Thread thread;

    Vibrator vibrator;

    public String[] data;
    ArrayList<String> answerDataArrayList;

    int questionsCount=1;


    long time=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);

        score=null;

        handler = new Handler();//A Handler allows you to send and process Message and Runnable objects associated with a thread's MessageQueue.
        timeHandler = new Handler();//A Handler allows you to send and process Message and Runnable objects associated with a thread's MessageQueue.
        clientThread = new QuizActivity.ClientThread();
        thread = new Thread(clientThread);
        thread.start();


        data=new String[21];
        answerDataArrayList=new ArrayList<>();

        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        option1_radio_button = (RadioButton) findViewById(R.id.option1_radio_button);
        option2_radio_button = (RadioButton) findViewById(R.id.option2_radio_button);
        option3_radio_button = (RadioButton) findViewById(R.id.option3_radio_button);
        text_view_question = (TextView) findViewById(R.id.text_view_question);
        text_view_question_count = (TextView) findViewById(R.id.text_view_question_count);
        text_view_countdown = (TextView) findViewById(R.id.text_view_countdown);
        score_text = (TextView) findViewById(R.id.score_text);
        score_title_text = (TextView) findViewById(R.id.score_title_text);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);


        next_button = (ImageView) findViewById(R.id.next_button);
        score_image = (ImageView) findViewById(R.id.score_image);
        check_score_button = (ImageView) findViewById(R.id.check_score_button);
        fail_pass_image = (ImageView) findViewById(R.id.fail_pass_image);
        time_out_image = (ImageView) findViewById(R.id.time_out_image);



        //confirm_button.setVisibility(View.INVISIBLE);

        enableQuestionsViews();
        disableScoreView();
        time_out_image.setVisibility(View.INVISIBLE);

        next_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

              if(data[20]!=null) {

                  if (checkRadioButtons()) {

                          questionsCount++;


                      if (questionsCount == 2) {

                          setTexts(data, 2);
                          text_view_question_count.setText("Question: " + questionsCount + "/5");

                      }


                      if (questionsCount == 3) {

                          setTexts(data, 3);
                          text_view_question_count.setText("Question: " + questionsCount + "/5");
                      }


                      if (questionsCount == 4) {

                          setTexts(data, 4);
                          text_view_question_count.setText("Question: " + questionsCount + "/5");
                      }


                      if (questionsCount == 5) {


                          setTexts(data, 5);
                          text_view_question_count.setText("Question: " + questionsCount + "/5");
                          next_button.setImageResource(R.drawable.confirm);
                      }



                      if (questionsCount == 6) {

                          check_score_button.setVisibility(View.VISIBLE);



                          clientThread.sendMessage(answerDataArrayList);
                          Toast.makeText(getBaseContext(), "Answers submitted 😇😇", Toast.LENGTH_LONG).show();


                      }


                  } else {
                      Toast.makeText(getBaseContext(), "Please Make sure you've answered 🤬🤬", Toast.LENGTH_LONG).show();

                  }

              }

              else {
                  Toast.makeText(getBaseContext(), "Please Wait Until The Questions Are Sent😏😏", Toast.LENGTH_LONG).show();

            }}
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {


                disableQuestionsViews();
                enableScoreView();
                check_score_button.setVisibility(View.INVISIBLE);

                try {
                    if(Integer.parseInt(score)<=2)
                    {

                        score_text.setTextColor(Color.RED);
                        fail_pass_image.setImageResource(R.drawable.fail);
                    }

                    else
                    {

                        score_text.setTextColor(Color.GREEN);
                        fail_pass_image.setImageResource(R.drawable.passed);
                    }
                    score_text.setText(score + "/5");
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getBaseContext(),"Your Answers are not checked yet🥱🥱",Toast.LENGTH_LONG).show();
                    fail_pass_image.setVisibility(View.INVISIBLE);

                }

                swipeRefreshLayout.setRefreshing(false);

            }
        });


        check_score_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                disableQuestionsViews();
                enableScoreView();
                check_score_button.setVisibility(View.INVISIBLE);

                try {

                    if(Integer.parseInt(score)<=2)
                    {

                        score_text.setTextColor(Color.RED);
                        fail_pass_image.setImageResource(R.drawable.fail);
                        vibrator.vibrate(400);
                    }

                    else
                    {

                        score_text.setTextColor(Color.GREEN);
                        fail_pass_image.setImageResource(R.drawable.passed);
                    }
                    score_text.setText(score + "/5");
                }
                catch (NumberFormatException e)
                {
                    Toast.makeText(getBaseContext(),"Your Answers are not checked yet🥱🥱",Toast.LENGTH_LONG).show();
                    fail_pass_image.setVisibility(View.INVISIBLE);

                }





            }
        });



    }



    public void showToastMessage(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {

                Toast.makeText(getBaseContext(),message,Toast.LENGTH_LONG).show();

            }
        });
    }


    public void setTexts(String[] data,int questionNumber) {
        handler.post(new Runnable() {
            @Override
            public void run() {



                if(questionNumber==1)
                {
                    text_view_question.setText(data[0]);
                    option1_radio_button.setText(data[1]);
                    option2_radio_button.setText(data[2]);
                    option3_radio_button.setText(data[3]);
                }


                if(questionNumber==2)
                {
                    text_view_question.setText(data[4]);
                    option1_radio_button.setText(data[5]);
                    option2_radio_button.setText(data[6]);
                    option3_radio_button.setText(data[7]);
                }


                if(questionNumber==3)
                {
                    text_view_question.setText(data[8]);
                    option1_radio_button.setText(data[9]);
                    option2_radio_button.setText(data[10]);
                    option3_radio_button.setText(data[11]);
                }



                if(questionNumber==4)
                {
                    text_view_question.setText(data[12]);
                    option1_radio_button.setText(data[13]);
                    option2_radio_button.setText(data[14]);
                    option3_radio_button.setText(data[15]);
                }


                if(questionNumber==5)
                {
                    text_view_question.setText(data[16]);
                    option1_radio_button.setText(data[17]);
                    option2_radio_button.setText(data[18]);
                    option3_radio_button.setText(data[19]);
                }




            }
        });
    }



    public void setTimer() {
        timeHandler.post(new Runnable() {
            @Override
            public void run() {

                CountDownTimer();

            }
        });
    }


    class ClientThread implements Runnable {



        private Socket socket;
        private BufferedReader input;
        private BufferedReader inputScore;
        private BufferedReader inputServerStatus;


        @Override
        public void run() {


            try {

                while (!Thread.currentThread().isInterrupted()) {
                    InetAddress serverAddr =InetAddress.getByName(SERVER_IP);
                    socket = new Socket(serverAddr, SERVERPORT);

                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    this.inputScore = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    this.inputServerStatus = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                    for(int i=0;i<data.length;i++)
                    {
                            data[i]=input.readLine();/**RECEIVING EXAM FROM SERVER*********************************************************/
                    }

                    setTexts(data,1);
                    setTimer();
                    score=inputScore.readLine();/**RECEIVING SCORE FROM SERVER*********************************************************/

                    if(score!=null)
                    {
                        startNotificationService();
                    }


                    /*if(inputServerStatus.readLine()=="disconnect")
                    {
                        showToastMessage("Server  Disconnected");
                        Thread.interrupted();
                        break;

                    }*/



                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        void sendMessage(final ArrayList<String >answerMessage) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (null != socket) {
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);
                            for(int i=0;i<answerMessage.size();i++)
                            {
                                out.println(answerMessage.get(i));/**SENDING ANSWERS TO SERVER*********************************************************/
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            //clientThread.sendMessage("Disconnect");
            clientThread = null;
        }
        stopNotificationService();
    }


    public boolean checkRadioButtons()
    {



            if(option1_radio_button.isChecked())
            {
                answerDataArrayList.add(option1_radio_button.getText().toString());
                return  true;

            }

            else if(option2_radio_button.isChecked())
            {
                answerDataArrayList.add(option2_radio_button.getText().toString());
                return  true;
            }


           else if(option3_radio_button.isChecked())
            {
                answerDataArrayList.add(option3_radio_button.getText().toString());
                return  true;
            }

           else
            {            return  false;
            }



    }


    public void disableQuestionsViews()
    {
        text_view_question.setVisibility(View.INVISIBLE);
        text_view_countdown.setVisibility(View.INVISIBLE);
        text_view_question_count.setVisibility(View.INVISIBLE);
        radio_group.setVisibility(View.INVISIBLE);
        option1_radio_button.setVisibility(View.INVISIBLE);
        option2_radio_button.setVisibility(View.INVISIBLE);
        option3_radio_button.setVisibility(View.INVISIBLE);
        next_button.setVisibility(View.INVISIBLE);
    }


    public void enableQuestionsViews()
    {
        text_view_question.setVisibility(View.VISIBLE);
        text_view_question_count.setVisibility(View.VISIBLE);
        text_view_countdown.setVisibility(View.VISIBLE);
        radio_group.setVisibility(View.VISIBLE);
        option1_radio_button.setVisibility(View.VISIBLE);
        option2_radio_button.setVisibility(View.VISIBLE);
        option3_radio_button.setVisibility(View.VISIBLE);
        next_button.setVisibility(View.VISIBLE);
    }



    public void enableScoreView()
    {
        score_title_text.setVisibility(View.VISIBLE);
        score_text.setVisibility(View.VISIBLE);
        score_image.setVisibility(View.VISIBLE);
        fail_pass_image.setVisibility(View.VISIBLE);

    }


    public void disableScoreView()
    {
        score_title_text.setVisibility(View.INVISIBLE);
        score_text.setVisibility(View.INVISIBLE);
        check_score_button.setVisibility(View.INVISIBLE);
        score_image.setVisibility(View.INVISIBLE);
        fail_pass_image.setVisibility(View.INVISIBLE);

    }

    public void CountDownTimer()
    {
        countDownTimer=new CountDownTimer(59000, 1000) {

            public void onTick(long millisUntilFinished) {

                if((millisUntilFinished/1000)<=9) { text_view_countdown.setText("01:0" +  millisUntilFinished / 1000);}

                else {text_view_countdown.setText("01:" + millisUntilFinished / 1000);}

                if((millisUntilFinished/1000)==0)
                {
                    new CountDownTimer(59000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            time=millisUntilFinished;

                            if((millisUntilFinished/1000)<=9)
                            {
                                text_view_countdown.setText("00:0"+millisUntilFinished / 1000);
                                text_view_countdown.setTextColor(Color.RED);
                            }
                            else{   text_view_countdown.setText("00:" + millisUntilFinished / 1000);}

                        }

                        public void onFinish() {
                            text_view_countdown.setText("Time Out!!!🥵");

                            new CountDownTimer(2000, 1000) {
                                public void onTick(long millisUntilFinished) {}
                                public void onFinish() {
                                    disableScoreView();
                                    disableQuestionsViews();
                                    time_out_image.setVisibility(View.VISIBLE);
                                }
                            }.start();

                        }

                    }.start();
                }


            }

            public void onFinish() {

            }

        }.start();

    }

    private boolean isNotificationServiceRunning()
    {
        ActivityManager activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager!=null)
        {
            for(ActivityManager.RunningServiceInfo service:activityManager.getRunningServices(Integer.MAX_VALUE))
            {
                if(NotificationService.class.getName().equals(service.service.getClassName()))
                {
                    if(service.foreground)
                    {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startNotificationService()
    {
        if(!isNotificationServiceRunning())
        {
            Intent intent=new Intent(getApplicationContext(), NotificationService.class);
            intent.setAction(Constants.ACTION_START_NOTIFICATION_SERVICE);
            startService(intent);


        }
    }


    private void stopNotificationService()
    {
        if(isNotificationServiceRunning())
        {
            Intent intent=new Intent(getApplicationContext(), NotificationService.class);
            intent.setAction(Constants.ACTION_STOP_NOTIFICATION_SERVICE);
            startService(intent);


        }
    }




}
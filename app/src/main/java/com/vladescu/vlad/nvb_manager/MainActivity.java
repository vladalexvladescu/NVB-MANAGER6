package com.vladescu.vlad.nvb_manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.PushService;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    String verifyUsename;
    String verifyParola;
    ProgressBar progress;
    Button buttonLogin;
    EditText eMail;
    EditText parolaP;
    TextView inregistrare;
    public boolean internetStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "BsKogsuw4T7rATkkl7eN3yP4YO99QnH5UAYs44zV", "ADd1e2kJX5vJuCDifsz4P4RVGdTtPnGxO9K5R6po");
        ParseInstallation.getCurrentInstallation().saveInBackground();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        eMail = (EditText) findViewById(R.id.editTextEmail);
        parolaP = (EditText) findViewById(R.id.editTextParola);

        inregistrare = (TextView) findViewById(R.id.textViewInregistrare);
        inregistrare.setOnClickListener(this);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.INVISIBLE);



        checkInternetConnection();

    }


    protected void checkInternetConnection(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){

            Context context = getApplicationContext();
            CharSequence text = getString(R.string.connectedToInternet);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            internetStatus=true;



        }else{
            Context context = getApplicationContext();
            CharSequence text = getString(R.string.noInternet);
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            internetStatus=false;

        }
    }
    private  String reqestData(String uri) {

        MyTask task = new MyTask();
        task.execute(uri);


        return uri;
    }


    @Override
    public void onClick(View view) {

        if (internetStatus) {
                switch (view.getId()) {

                    case R.id.buttonLogin: {
                        progress.setVisibility(View.VISIBLE);

                        String username = String.valueOf(eMail.getText());
                        String parola = String.valueOf(parolaP.getText());


                        ParseQuery<ParseObject> queryUser = new ParseQuery<ParseObject>("Useri");//verivicarea pentru datele ce nu ar trebui sa fie duplicat
                        queryUser.whereEqualTo("username", String.valueOf(eMail.getText()));
                        queryUser.whereEqualTo("parola", String.valueOf(parolaP.getText()));
                        queryUser.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> markersUser, ParseException e) {
                                if (e == null) {

                                    if (markersUser.size() == 0) {
                                        Toast.makeText(getBaseContext(), "Nume utilizator inexistent!",
                                                Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.INVISIBLE);
                                    } else {


                                        Toast.makeText(getBaseContext(), "Se logeaza...",
                                                Toast.LENGTH_SHORT).show();
                                        progress.setVisibility(View.INVISIBLE);


                                    }

                                } else {
                                    // handle Parse Exception here
                                }
                            }
                        });


                        break;
                    }
                    case R.id.textViewInregistrare: {

                        inregistrare.setTextColor(Color.BLACK);
                        Context context = getApplicationContext();

                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,"ura", duration);
                        toast.show();
                        Intent intent = new Intent(this,RegisterActiv.class);
                        startActivity(intent);


                        break;
                    }

                    default: {

                        break;

                }


            }

        }

    }

    public class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            String content = ClientReqest.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            progress.setVisibility(View.INVISIBLE);

            Context context = getApplicationContext();

            int duration = Toast.LENGTH_LONG;

            Toast toastAfisareInterogare = Toast.makeText(context,result, duration);
            toastAfisareInterogare.show();


        }
    }

}

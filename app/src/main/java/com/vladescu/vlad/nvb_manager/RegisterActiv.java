package com.vladescu.vlad.nvb_manager;

 import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

 import com.parse.FindCallback;
 import com.parse.ParseException;
 import com.parse.ParseObject;
 import com.parse.ParseQuery;
 import com.parse.ParseUser;
 import com.parse.SignUpCallback;

 import java.util.List;

public class RegisterActiv extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progress1;
    Button buttonAccepta;
    Button buttonRefuza;
    EditText nume;
    EditText prenume;
    EditText mail;
    EditText parola;
    EditText telefon;



    public boolean accept ;
    public boolean internetStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg);


        buttonAccepta = (Button) findViewById(R.id.buttonAccepta);
        buttonAccepta.setOnClickListener(this);
        buttonRefuza = (Button) findViewById(R.id.buttonRefuza);
        buttonRefuza.setOnClickListener(this);

        nume = ( EditText) findViewById(R.id.editTextNume);
        prenume = ( EditText) findViewById(R.id.editTextPrenume);
        mail = (EditText) findViewById(R.id.editTextEmail);
        parola = ( EditText) findViewById(R.id.editTextParola);
        telefon = ( EditText) findViewById(R.id.editTextTelefon);
        progress1 =(ProgressBar)findViewById(R.id.progressBarRegister);
        progress1.setVisibility(View.INVISIBLE);


    }
    private  String reqestDataReg(String uri) {

        MyTaskReg task1 = new MyTaskReg();
        task1.execute(uri);


        return uri;
    }
    public boolean checkFields(String nume,String prenume,String mail,String parola,String telefon){
        if(nume.length()<=1){
            Toast.makeText(getBaseContext(),"Numele trebuie sa contina minim 2 caractere",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(prenume.length()<=1){
            Toast.makeText(getBaseContext(),"Prenumele trebuie sa contina minim 2 caractere",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(mail.length()<=1||!(mail.contains("@"))||!(mail.contains("."))){
            Toast.makeText(getBaseContext(),"Adresa de e-mail invalida",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(parola.length()<5){
            Toast.makeText(getBaseContext(),"Parola trebuie sa contina minim 5 caractere",
                    Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }


    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.buttonAccepta: {



                String username = String.valueOf(mail.getText());
                String name = String.valueOf(nume.getText());
                String pren = String.valueOf(prenume.getText());
                String pass = String.valueOf(parola.getText());
                String tel = String.valueOf(telefon.getText());

                if(checkFields(name,pren,username,pass,tel)==true) {//verificarea iterna a validitatii campurilor introduse

                    ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Useri");//verivicarea pentru datele ce nu ar trebui sa fie duplicat
                    query.whereEqualTo("username", String.valueOf(mail.getText()));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> markers, ParseException e) {
                            if (e == null) {
                                if (markers.size()==0) {
                                    ParseObject useri = new ParseObject("Useri");
                                    useri.put("nume", String.valueOf(nume.getText()));
                                    useri.put("prenume",String.valueOf(prenume.getText()));
                                    useri.put("username", String.valueOf(mail.getText()));
                                    useri.put("parola",String.valueOf(parola.getText()));
                                    useri.put("phone", String.valueOf(telefon.getText()));

                                    useri.saveInBackground();

                                    Toast.makeText(getBaseContext(), "Cont creat cu success!",
                                            Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getBaseContext(), "Adresa de e-mail detine deja un cont!Folositi o alta adresa de e-mail",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                // handle Parse Exception here
                            }
                        }
                    });



                }

                break;
            }
            case R.id.buttonRefuza: {





                break;
            }

            default: {

                break;

            }


        }

    }
    public class MyTaskReg extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progress1.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            String content = ClientReqest.getData(params[0]);
            return content;
        }

        @Override
        protected void onPostExecute(String result) {
            progress1.setVisibility(View.INVISIBLE);

            Context context = getApplicationContext();

            int duration = Toast.LENGTH_LONG;

            Toast toastAfisareInterogare = Toast.makeText(context,result, duration);
            toastAfisareInterogare.show();


        }
    }

}

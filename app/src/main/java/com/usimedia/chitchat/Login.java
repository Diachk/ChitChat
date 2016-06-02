package com.usimedia.chitchat;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.usimedia.chitchat.model.LoginModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private ProgressBar spinner;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinner = (ProgressBar) findViewById(R.id.login_activity_spinner);
        loginButton = (Button) findViewById(R.id.login_activity_login_button);
        final EditText emailText = (EditText) findViewById(R.id.login_activity_email_text);
        final EditText passwordText = (EditText) findViewById(R.id.login_activity_password_text);

        spinner.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginModel model = new LoginModel();
                model.setUsername(emailText.getText().toString());
                model.setPassword(passwordText.getText().toString());
                showSpinner();
                new LoginTask().execute(model);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private final void showSpinner() {
        loginButton.setVisibility(View.GONE);
        spinner.setVisibility(View.VISIBLE);
    }

    private final void hideSpinner() {
        loginButton.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.GONE);
    }


    private class LoginTask extends AsyncTask<LoginModel, Void, LoginModel.Result> {

        protected LoginModel.Result doInBackground(LoginModel... userDetail) {

            final LoginModel model = userDetail[0];

            final String result = login(model.getUsername(), model.getPassword());

            Log.d("Login", "Login Attempt username = "+ model.getUsername());

            final LoginModel.Result loginResult = new LoginModel.Result();

            try {
                if(null != result) {
                    final JSONObject jsonResult = new JSONObject(result);
                    loginResult.setName(jsonResult.getString("user"));
                    loginResult.setSuccess(jsonResult.getBoolean("success"));
                }
                else {
                    loginResult.setSuccess(false);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return  loginResult;
        }


        protected void onPostExecute(LoginModel.Result result) {
            hideSpinner();
            final String message = result.isSuccess() ? "Welcome ".concat(result.getName()) : "Login Failed";
            Toast.makeText(Login.this, message, Toast.LENGTH_LONG).show();

            if(result.isSuccess()) {
                final Intent toContactsActivity = new Intent(Login.this , Contacts.class);
                startActivity(toContactsActivity);
            }
        }
    }

    public String login(String username, String password) {

        String url = "http:192.168.1.3:8000/auth/login";

        RequestBody body = new FormBody.Builder()
                .add("email", username)
                .add("password", password)
                .build();

        String result = null;

        try {
            result = post(url, body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    String post(String url, RequestBody body) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

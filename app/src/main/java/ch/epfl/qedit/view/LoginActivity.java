package ch.epfl.qedit.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ch.epfl.qedit.R;
import ch.epfl.qedit.backend.auth.AuthenticationFactory;
import ch.epfl.qedit.backend.auth.AuthenticationService;
import ch.epfl.qedit.model.User;
import ch.epfl.qedit.util.Callback;
import ch.epfl.qedit.util.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String USER = "ch.epfl.qedit.view.USER";

    private EditText tokenText;
    private ProgressBar progressBar;

    private AuthenticationService authService;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenText = findViewById(R.id.login_token);
        progressBar = findViewById(R.id.login_progress_bar);

        authService = AuthenticationFactory.getInstance();
        handler = new Handler();
    }

    public void handleLogin(View view) {
        String token = tokenText.getText().toString();
        if(token.isEmpty()){
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.empty_token_message,
                            Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        authService.sendRequest(
                token,
                new Callback<Response<User>>() {
                    @Override
                    public void onReceive(final Response<User> response) {
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        if (response.successful())
                                            onLoginSuccessful(response.getData());
                                        else onLoginFailed(response.getError());
                                    }
                                });
                    }
                });
    }

    public void onLoginSuccessful(User user) {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(USER, user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void onLoginFailed(int error) {
        int stringId = 0;
        switch (error) {
            case AuthenticationService.CONNECTION_ERROR:
                stringId = R.string.connection_error_message;
                break;
            case AuthenticationService.WRONG_TOKEN:
                stringId = R.string.wrong_token_message;
                break;
            default:
                break;
        }

        Toast toast =
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(stringId),
                        Toast.LENGTH_SHORT);
        toast.show();
    }
}

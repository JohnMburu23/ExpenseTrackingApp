package com.example.expensetrackingapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class LoginActivity extends AppCompatActivity {
    private EditText loginUsername;
    private EditText loginPhoneNumber;
    private EditText loginPassword;
    private Button buttonLogin;
    private Button buttonRegister;
    private Button buttonReset;
    private TextView registerTextView;
    private DBHelper dbHelper;

    private DrawerLayout drawerLayout;
    private Drawable eyeIcon;
    private boolean isPasswordVisible = false;

    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        loginUsername = findViewById(R.id.editTextUsername);
        loginPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        loginPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonReset =findViewById(R.id.buttonReset);
        registerTextView = findViewById(R.id.clickRegisterTextView);
        dbHelper = new DBHelper(this);


        eyeIcon = ContextCompat.getDrawable(this, R.drawable.ic_eye);

        // Add the eye icon to the right of the EditText
        loginPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeIcon, null);

        // Set onTouchListener to the EditText to detect touch on the eye icon
        loginPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Check if touch is on the drawableright (eye icon)
                if (event.getAction() == MotionEvent.ACTION_UP &&
                        event.getRawX() >= (loginPassword.getRight() -
                                loginPassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
                return false;
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
           String username = loginUsername.getText().toString().trim();
            String phoneNo = loginPhoneNumber.getText().toString().trim();
           String password = loginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(phoneNo)) {
                Toast.makeText(LoginActivity.this, "Login failed. Please fill in all fields.", Toast.LENGTH_SHORT).show();
            }
            else if (!isPasswordValid(password)) {
                Toast.makeText(LoginActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                return;
            }else {
                if (phoneNo.length() != 10 || !phoneNo.matches("\\d+")) {// \\d+ is a regular expression for checking if a string consists of numbers only
                    Toast.makeText(LoginActivity.this, "Invalid phone number!", Toast.LENGTH_SHORT).show();
                    loginPhoneNumber.setText("");
                    return; // Stop login if phone number is invalid
                }

                else if (dbHelper.checkUser(username,phoneNo, password)) {
                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    EditText userNameEditText = findViewById(R.id.editTextUsername);
                    EditText phoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
                    String enteredUsername = userNameEditText.getText().toString();
                    String enteredPhoneNumber = phoneNumberEditText.getText().toString();


                    // Navigate to HomeActivity
                    Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                    intent.putExtra("username",enteredUsername);
                    intent.putExtra("phoneNumber",enteredPhoneNumber);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Login failed. Register first.", Toast.LENGTH_SHORT).show();
                }
            }
        }

    });

        buttonRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            if(!loginUsername.getText().toString().isEmpty()||!loginPassword.getText().toString().isEmpty()||!loginPhoneNumber.getText().toString().isEmpty()){
                loginUsername.setText("");
                loginPhoneNumber.setText("");
                loginPassword.setText("");
                Toast.makeText(LoginActivity.this,"Field(s) cleared!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LoginActivity.this,"No content to clear!",Toast.LENGTH_SHORT).show();
            }
        }
    });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
}
    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // If password is visible, hide it
            loginPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            isPasswordVisible = false;
        } else {
            // If password is hidden, show it
            loginPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            isPasswordVisible = true;
        }

        // Move the cursor to the end of the text
        loginPassword.setSelection(loginPassword.getText().length());

        // Update the eye icon based on password visibility
        updateEyeIcon();
    }
    private void updateEyeIcon() {
        // Set the eye icon to the same drawable for both open and closed states
        eyeIcon = ContextCompat.getDrawable(this, R.drawable.ic_eye);

        // Add the updated eye icon to the right of the EditText
        loginPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeIcon, null);
    }

}
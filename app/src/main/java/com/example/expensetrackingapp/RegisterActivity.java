package com.example.expensetrackingapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class RegisterActivity extends AppCompatActivity {
    private EditText registerNewUserName;
    private EditText registerNewPhoneNumber;
    private EditText registerNewPassword;
    private EditText registerConfirmNewPassword;
    private Button btnRegisterNewUser;
    private Button btnNewRegisterReset;
    private DBHelper dbHelper;
    private Drawable eyeIcon;
    private boolean isPasswordVisible1 = false;
    private boolean isPasswordVisible2 = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegisterNewUser = findViewById(R.id.btnRegisterNewUser);
        registerNewUserName = findViewById(R.id.editTextNewUsername);
        registerNewPhoneNumber = findViewById(R.id.editTextNewPhoneNumber);
        registerNewPassword = findViewById(R.id.editTextNewPassword);
        registerConfirmNewPassword = findViewById(R.id.editTextConfirmNewPassword);
        btnNewRegisterReset = findViewById(R.id.btnNewRegisterReset);
        dbHelper = new DBHelper(this);


        eyeIcon = ContextCompat.getDrawable(this, R.drawable.ic_eye);

        // Add the eye icon to the right of the EditTexts
        registerNewPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeIcon, null);
        registerConfirmNewPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeIcon, null);

        // Set onTouchListener to the EditTexts to detect touch on the eye icon
        registerNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleEyeIconTouch(registerNewPassword, event);
                return false;
            }
        });

        registerConfirmNewPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleEyeIconTouch(registerConfirmNewPassword, event);
                return false;
            }
        });

        btnRegisterNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = registerNewUserName.getText().toString().trim();
                String regPhoneNumber = registerNewPhoneNumber.getText().toString().trim();
                String password = registerNewPassword.getText().toString().trim();
                String regConfirmPassword = registerConfirmNewPassword.getText().toString().trim();


                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(regConfirmPassword)||TextUtils.isEmpty(regPhoneNumber)) {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please fill in all fields.", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordValid(password)) {
                    Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                } else {
                    if (regPhoneNumber.length() != 10 || !regPhoneNumber.matches("\\d+")) {
                        Toast.makeText(RegisterActivity.this, "Invalid phone number. Please enter a 10-digit phone number.", Toast.LENGTH_SHORT).show();
                        registerNewPhoneNumber.setText("");
                        return; // Stop registration if phone number is invalid
                    }
                    if (!password.equals(regConfirmPassword)) {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        registerNewPassword.setText("");
                        registerConfirmNewPassword.setText("");
                    } else if (userExists(username)) {
                        Toast.makeText(RegisterActivity.this, "User already exists. Please choose a different username.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dbHelper.addUser(username, password, regPhoneNumber)) {
                            Toast.makeText(RegisterActivity.this, "Registration successful!You can now log in.", Toast.LENGTH_SHORT).show();
                            // Navigate back to LoginActivity
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        btnNewRegisterReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!registerNewUserName.getText().toString().isEmpty() ||
                        !registerNewPassword.getText().toString().isEmpty() ||
                        !registerConfirmNewPassword.getText().toString().isEmpty() ||
                        !registerNewPhoneNumber.getText().toString().isEmpty()){
                    registerNewUserName.setText("");
                    registerNewPhoneNumber.setText("");
                    registerNewPassword.setText("");
                    registerConfirmNewPassword.setText("");
                    Toast.makeText(RegisterActivity.this,"Field(s) cleared!",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this,"No content to clear!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnNewRegisterReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!registerNewUserName.getText().toString().isEmpty()||!registerNewPassword.getText().toString().isEmpty()||!registerConfirmNewPassword.getText().toString().isEmpty()||!registerNewPhoneNumber.getText().toString().isEmpty()){
                    registerNewUserName.setText("");
                    registerNewPhoneNumber.setText("");
                    registerNewPassword.setText("");
                    registerConfirmNewPassword.setText("");
                    Toast.makeText(RegisterActivity.this,"Field(s) cleared!",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this,"No content to clear!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }
    private boolean userExists(String username) {
        return dbHelper.checkUserExists(username);
    }

    private void handleEyeIconTouch(EditText editText, MotionEvent event) {
        // Check if touch is on the drawableright (eye icon)
        if (event.getAction() == MotionEvent.ACTION_UP &&
                event.getRawX() >= (editText.getRight() - editText.getCompoundDrawables()[2].getBounds().width())) {
            togglePasswordVisibility(editText);
        }
    }

    private void togglePasswordVisibility(EditText editText) {
        boolean isPasswordVisible = (editText == registerNewPassword) ? isPasswordVisible1 : isPasswordVisible2;

        if (isPasswordVisible) {
            // If password is visible, hide it
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if (editText == registerNewPassword) {
                isPasswordVisible1 = false;
            } else if (editText == registerConfirmNewPassword) {
                isPasswordVisible2 = false;
            }
        } else {
            // If password is hidden, show it
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            if (editText == registerNewPassword) {
                isPasswordVisible1 = true;
            } else if (editText == registerConfirmNewPassword) {
                isPasswordVisible2 = true;
            }
        }

        // Move the cursor to the end of the text
        editText.setSelection(editText.getText().length());

        // Update the eye icon based on password visibility
        updateEyeIcon(editText);
    }

    private void updateEyeIcon(EditText editText) {
        // Set the eye icon to the same drawable for both open and closed states
        eyeIcon = ContextCompat.getDrawable(this, R.drawable.ic_eye);

        // Add the updated eye icon to the right of the EditText
        editText.setCompoundDrawablesWithIntrinsicBounds(null, null, eyeIcon, null);
    }
}
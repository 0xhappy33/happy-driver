package com.happycity.project.happydriver.ui.Login;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.happycity.project.happydriver.R;
import com.happycity.project.happydriver.models.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference users;

    MaterialEditText  edtEmail, edtPassword, edtPhone, edtName;
    @BindView(R.id.rootRegisterLayout)
    RelativeLayout rootRegisterLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initial firebase database
        initFirebase();
        // bind view to on create
        ButterKnife.bind(this);
        // add event for sign up button
        clickRegisterButton();
        // add event for sign in button
        clickSignInButton();
    }

    private void clickSignInButton() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInDialog();
            }
        });
    }

    private void showSignInDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN IN");
        dialog.setMessage("Please use email to sign in");

        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View login_view = inflater.inflate(R.layout.layout_login, null);

        edtEmail = login_view.findViewById(R.id.edtEmail);
        edtPassword = login_view.findViewById(R.id.edtPassword);

        dialog.setView(login_view);

        dialog.setPositiveButton("SIGN IN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                //
                checkValidationInputFromUserInRegister();

                // sign in for user
                signInToDatabase();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void signInToDatabase() {

    }

    private void clickRegisterButton() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterDialog();
            }
        });
    }

    private void showRegisterDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("SIGN UP");
        dialog.setMessage("Please use email to sign up");

        LayoutInflater inflater = LayoutInflater.from(this);
        @SuppressLint("InflateParams") View register_view = inflater.inflate(R.layout.layout_register, null);

        edtEmail = register_view.findViewById(R.id.edtEmail);
        edtName = register_view.findViewById(R.id.edtName);
        edtPassword = register_view.findViewById(R.id.edtPassword);
        edtPhone = register_view.findViewById(R.id.edtPhone);

        dialog.setView(register_view);

        dialog.setPositiveButton("SIGN UP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                checkValidationInputFromUserInRegister();

                // register new user
                registerNewUserToFirebase();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void registerNewUserToFirebase() {
        firebaseAuth.createUserWithEmailAndPassword(edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim())
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        saveUserToDatabase();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackBarToReportForUser("Failed" + e.getMessage());
                    }
                })
        ;
    }

    private void saveUserToDatabase() {
        User user = new User();
        user.setEmail(edtEmail.getText().toString().trim());
        user.setPassword(edtPassword.getText().toString().trim());
        user.setPhone(edtPhone.getText().toString().trim());
        user.setName(edtName.getText().toString().trim());

        // set user to key : key here is email
        users.child(user.getEmail())
                .setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showSnackBarToReportForUser("Sign up successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackBarToReportForUser("Sign up failed" + e.getMessage());
                    }
                })
        ;
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("Users");
    }

    private void checkValidationInputFromUserInRegister(){
        if(TextUtils.isEmpty(edtEmail.getText().toString())){
            showSnackBarToReportForUser("Please enter email address");
        }
        if(TextUtils.isEmpty(edtName.getText().toString())){
            showSnackBarToReportForUser("Please enter your name");
        }
        if(TextUtils.isEmpty(edtPhone.getText().toString())){
            showSnackBarToReportForUser("Please enter your phone number");
        }
        if(edtPassword.getText().toString().length() < 6){
            showSnackBarToReportForUser("Password is too short");
        }
    }

    private void showSnackBarToReportForUser(String message){
        Snackbar.make(rootRegisterLayout, message, Snackbar.LENGTH_SHORT)
                .show();
    }
}

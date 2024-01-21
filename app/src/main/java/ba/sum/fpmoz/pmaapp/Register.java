package ba.sum.fpmoz.pmaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import ba.sum.fpmoz.pmaapp.models.User;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText editFirstName, editLastName, editEmail, editPassword, editPhone;
    Button registerBtn;
    TextView loginTxtView;
    RadioGroup rgRole;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_form);

        mAuth = FirebaseAuth.getInstance();
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        editPhone = findViewById(R.id.editPhone);
        rgRole = findViewById(R.id.rgRole);
        registerBtn = findViewById(R.id.addSubjectBtn);
        loginTxtView = findViewById(R.id.loginTxtView);


        DatabaseReference usersDbRef = this.mDatabase.getReference("users");



        loginTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FirstName;
                String LastName;
                String Email;
                String Password;
                String Phone;
                int roleId;

                FirstName = editFirstName.getText().toString();
                LastName = editLastName.getText().toString();
                Email = editEmail.getText().toString();
                Password = editPassword.getText().toString();
                Phone = editPhone.getText().toString();
                roleId = rgRole.getCheckedRadioButtonId();
                RadioButton checkedRadioButton = findViewById(roleId);
                String checkedText = checkedRadioButton.getText().toString();
                Log.d("taga 88-",checkedText);



                if (TextUtils.isEmpty(FirstName) || TextUtils.isEmpty(LastName) || TextUtils.isEmpty(Email)
                        || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Phone)) {
                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Password.length() < 6) {
                    Toast.makeText(Register.this, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                String finalRole = checkedText;


                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    User u = new User(FirstName, LastName, Email, Password, Phone, finalRole );
                                    usersDbRef.child(userId).setValue(u);
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    startActivity(intent);
                                    Toast.makeText(Register.this, "Account Created", Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Log.e("Registerare", "Registration failed. " + task.getException().getMessage());
                                    Toast.makeText(Register.this, "Registration failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
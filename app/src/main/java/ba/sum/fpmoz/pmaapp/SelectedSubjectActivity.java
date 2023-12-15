package ba.sum.fpmoz.pmaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import ba.sum.fpmoz.pmaapp.models.Subject;

public class SelectedSubjectActivity extends AppCompatActivity {

    TextView emailProff, subjectName, subjectEtcsValue, subjectClassesValue, subjectExercisesValue,subjectPracticalValue,
    subjectSeminarsValue,subjectDepartmentValue,subjectStudiesValue;
    Button backBtn;
    FirebaseAuth auth;

    FirebaseUser user;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_subject);

        emailProff = findViewById(R.id.emailProff);
        subjectName = findViewById(R.id.subjectName);
        subjectClassesValue = findViewById(R.id.subjectClassesValue);
        subjectDepartmentValue = findViewById(R.id.subjectDepartmentValue);
        subjectEtcsValue = findViewById(R.id.subjectEtcsValue);
        subjectExercisesValue = findViewById(R.id.subjectExercisesValue);
        subjectSeminarsValue = findViewById(R.id.subjectSeminarsValue);
        subjectPracticalValue = findViewById(R.id.subjectPracticalValue);
        subjectStudiesValue = findViewById(R.id.subjectStudiesValue);
        backBtn = findViewById(R.id.logout2);
        DatabaseReference usersDbRef = this.mDatabase.getReference("users");
        DatabaseReference selectedSDbRef = this.mDatabase.getReference("selected_subject");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        // Fetch additional user data from the database
        usersDbRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String FirstName = snapshot.child("firstName").getValue(String.class);
                    String LastName = snapshot.child("lastName").getValue(String.class);

                    String userDetailsText = FirstName + " " + LastName;
                    emailProff.setText(userDetailsText);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //error
            }
        });

        selectedSDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Subject item = snapshot.child(user.getUid()).getValue(Subject.class);
                subjectName.setText(item.getName().toString());
                subjectClassesValue.setText(item.getClasses().toString());
                subjectEtcsValue.setText(item.getEcts().toString());
                subjectExercisesValue.setText(item.getExercises().toString());
                subjectPracticalValue.setText(item.getPractical().toString());
                subjectDepartmentValue.setText(item.getDepartment().toString());
                subjectSeminarsValue.setText(item.getSeminars().toString());
                subjectStudiesValue.setText(item.getStudies().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfessorActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
}
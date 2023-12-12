package ba.sum.fpmoz.pmaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.pmaapp.models.Subject;
import ba.sum.fpmoz.pmaapp.models.User;

public class AddSubjectActivity extends AppCompatActivity {

    Button logout, addSubjectBtn;
    EditText editName, editClasses,editEcts,editSemester,editPractical,editSeminars,editExercises,editDepartment,editStudies;
    TextView emailProff;
    FirebaseUser user;
    FirebaseAuth auth;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), ProfessorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        logout = findViewById(R.id.logout2);
        addSubjectBtn = findViewById(R.id.addSubjectBtn);
        editName = findViewById(R.id.editName);
        editClasses = findViewById(R.id.editClasses);
        editPractical = findViewById(R.id.editPractical);
        editEcts = findViewById(R.id.editEcts);
        editExercises = findViewById(R.id.editExercises);
        editDepartment = findViewById(R.id.editDepartment);
        editSemester= findViewById(R.id.editSemester);
        editStudies = findViewById(R.id.editStudies);
        emailProff = findViewById(R.id.emailProff);
        editSeminars = findViewById(R.id.editSeminars);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        DatabaseReference subjectsDbRef = this.mDatabase.getReference("subjects");
        DatabaseReference usersDbRef = this.mDatabase.getReference("users");

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


        addSubjectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SubjectName;
                String Ects;
                String Exercises;
                String Semester;
                String Seminars;
                String Practical;
                String Classes;
                String Department;
                String Studies;

                SubjectName = editName.getText().toString();
                Ects = editEcts.getText().toString();
                Exercises = editExercises.getText().toString();
                Seminars = editSeminars.getText().toString();
                Semester = editSemester.getText().toString();
                Practical = editPractical.getText().toString();
                Classes = editClasses.getText().toString();

                Department = editDepartment.getText().toString();
                Studies = editStudies.getText().toString();

                if (TextUtils.isEmpty(SubjectName) || TextUtils.isEmpty(Ects) || TextUtils.isEmpty(Exercises)
                        || TextUtils.isEmpty(Practical) || TextUtils.isEmpty(Semester) || TextUtils.isEmpty(Seminars) ||
                        TextUtils.isEmpty(Classes) || TextUtils.isEmpty(Department) || TextUtils.isEmpty(Studies)){
                    Toast.makeText(AddSubjectActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid().toString();

                Subject s = new Subject(SubjectName, Classes, Exercises, Seminars, Semester, Practical, Ects, userId, Studies, Department);
                Log.d("AddSubjectActivitysa", "Before setting value in database");
                subjectsDbRef.child(SubjectName).setValue(s);
                Log.d("AddSubjectActivitysa", "After setting value in database");

                Toast.makeText(AddSubjectActivity.this, "Subject added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ProfessorActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });




    }
}
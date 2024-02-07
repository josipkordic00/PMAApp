package ba.sum.fpmoz.pmaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ba.sum.fpmoz.pmaapp.models.Subject;

public class InfoActivity extends AppCompatActivity {
    TextView subjectName, department, studies, semester, seminars, exercises, practical, ects;
    FirebaseAuth auth;
    FirebaseUser user;
    Button backBtn;

    ImageView logout;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        subjectName = findViewById(R.id.emailProff2);
        department = findViewById(R.id.tvTextView2);
        studies = findViewById(R.id.tvTextView3);
        semester = findViewById(R.id.tvTextView4);
        seminars = findViewById(R.id.tvTextView5);
        exercises = findViewById(R.id.tvTextView6);
        practical = findViewById(R.id.tvTextView7);
        ects = findViewById(R.id.tvTextView10);
        logout = findViewById(R.id.logout2);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
        backBtn = findViewById(R.id.button2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        DatabaseReference selectedSDbRef = this.mDatabase.getReference("selected_subject");


        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        selectedSDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Subject item = snapshot.child(user.getUid()).getValue(Subject.class);
                subjectName.setText(item.getName().toString());
                studies.setText(item.getStudies().toString());
                semester.setText(item.getSemester().toString());
                practical.setText(item.getPractical().toString());
                exercises.setText(item.getExercises().toString());
                ects.setText(item.getEcts().toString());
                department.setText(item.getDepartment().toString());
                seminars.setText(item.getSeminars().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectedSubjectActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
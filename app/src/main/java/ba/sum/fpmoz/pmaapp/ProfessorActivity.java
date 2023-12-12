package ba.sum.fpmoz.pmaapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ba.sum.fpmoz.pmaapp.adapters.SubjectAdapter;
import ba.sum.fpmoz.pmaapp.models.Subject;

public class ProfessorActivity extends AppCompatActivity implements SubjectAdapter.OnItemClickListener {
    FirebaseAuth auth;
    Button logout, addSubject;
    TextView emailProff;
    RecyclerView subjectsView;
    ArrayList<Subject> list;
    FirebaseUser user;
    DatabaseReference databaseUsersReference, databaseSubjectsReference;

    SubjectAdapter adapter;

    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_view);

        auth = FirebaseAuth.getInstance();
        subjectsView = findViewById(R.id.subjectsView);
        logout = findViewById(R.id.logout2);
        addSubject = findViewById(R.id.addSubject);
        emailProff = findViewById(R.id.emailProff);
        user = auth.getCurrentUser();
        DatabaseReference subjectsDbRef = this.mDatabase.getReference("subjects");
        DatabaseReference usersDbRef = this.mDatabase.getReference("users");


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
        list = new ArrayList<>();
        subjectsView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubjectAdapter(this, list, this);
        subjectsView.setAdapter(adapter);
        subjectsDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Subject subject =dataSnapshot.getValue(Subject.class);
                    if (subject.getUserId().toString().equals(user.getUid().toString())) {
                        list.add(subject);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

        addSubject.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSubjectActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
                finish();
            }
        }));


    }
    @Override
    public void onItemClick(Subject subject) {
        DatabaseReference subjectsDbRef = this.mDatabase.getReference("subjects");
        DatabaseReference usersDbRef = this.mDatabase.getReference("users");





        //Intent intent = new Intent(getApplicationContext(), SelectedSubjectActivity.class);
        //startActivity(intent);
        //finish();
        Log.d("ItemClicked", "Subject: " + subject);
    }
}

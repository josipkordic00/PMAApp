package ba.sum.fpmoz.pmaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

import ba.sum.fpmoz.pmaapp.adapters.EvidenceAdapter;
import ba.sum.fpmoz.pmaapp.adapters.SubjectAdapter;
import ba.sum.fpmoz.pmaapp.models.Subject;

public class Evidence_Activity extends AppCompatActivity implements EvidenceAdapter.OnItemClickListener{
    ArrayList<String> list;
    RecyclerView evidenceView;
    EvidenceAdapter adapter;
    TextView subjectName;
    Button backBtn;
    FirebaseAuth auth;
    FirebaseUser user;

    ImageView logout;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidence);

        logout = findViewById(R.id.logout2);


        evidenceView = findViewById(R.id.evidenceView);
        backBtn = findViewById(R.id.button2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SelectedSubjectActivity.class);
                startActivity(intent);
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
        subjectName = findViewById(R.id.emailProff2);
        DatabaseReference selectedSDbRef = this.mDatabase.getReference("selected_subject");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        list = new ArrayList<>();
        evidenceView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EvidenceAdapter(this, list, this);
        evidenceView.setAdapter(adapter);

        DatabaseReference prevDbRef = this.mDatabase.getReference("prev_evidentions");


        selectedSDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Subject item = snapshot.child(user.getUid()).getValue(Subject.class);
                subjectName.setText(item.getName().toString());

                prevDbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                               if(userSnapshot.getKey().equals(user.getUid())) {
                                   for (DataSnapshot subjectSnapshot : userSnapshot.getChildren()) {
                                       if(subjectSnapshot.getKey().equals(subjectName.getText().toString())){
                                           for (DataSnapshot dateSnapshot : subjectSnapshot.getChildren()) {
                                               list.add(dateSnapshot.getKey());
                                           }
                                       }

                                   }

                               }
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    @Override
    public void onItemClick(String evidence) {
        DatabaseReference selectedEvidenceDbRef = this.mDatabase.getReference("selected_evidence");
        selectedEvidenceDbRef.child(user.getUid()).setValue(evidence);
        Intent intent = new Intent(getApplicationContext(), SingleEvidentionActivity.class);
        startActivity(intent);
        finish();
    }
}
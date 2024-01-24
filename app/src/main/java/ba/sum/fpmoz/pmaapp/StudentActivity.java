package ba.sum.fpmoz.pmaapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.content.pm.ActivityInfo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ba.sum.fpmoz.pmaapp.adapters.StudentAdapter;
import ba.sum.fpmoz.pmaapp.adapters.UserAdapter;
import ba.sum.fpmoz.pmaapp.models.User;

public class StudentActivity extends AppCompatActivity implements StudentAdapter.OnItemClickListener {

    FirebaseAuth auth;
    TextView emailProff, subjectName2;
    RecyclerView studentsView;
    ImageView logout;
    FirebaseUser user;

    ArrayList<String> list;
    StudentAdapter adapter;

    DatabaseReference databaseUsersReference, databaseSubjectsReference;
    String subjectName, subjectUserId;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");
    String subject;


    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_view);

        auth = FirebaseAuth.getInstance();
        studentsView = findViewById(R.id.recyclerView);
        logout = findViewById(R.id.logout2);
        subjectName2 = findViewById(R.id.textView2);
        emailProff = findViewById(R.id.emailProff2);
        user = auth.getCurrentUser();


        DatabaseReference usersDbRef = this.mDatabase.getReference("users");
        DatabaseReference evdDbRef = this.mDatabase.getReference("evidentions");

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        list = new ArrayList<>();
        studentsView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, list,this , this.subject );
        studentsView.setAdapter(adapter);

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
        scanCode();

        evdDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        if(userSnapshot.getKey().equals(subjectUserId)){
                            for (DataSnapshot subjectSnapshot : userSnapshot.getChildren()) {
                                if (subjectSnapshot.getKey().toString().equals(subjectName)) {
                                    for (DataSnapshot emailSnapshot : subjectSnapshot.getChildren()) {
                                        String email = emailSnapshot.getValue(String.class);
                                        list.add(removeAfterAtSymbol(email));

                                    }
                                }else{
                                    Log.d("mssa", "sw- "+ subjectSnapshot.toString());
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

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);

    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            DatabaseReference evdDbRef = this.mDatabase.getReference("evidentions");
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
            builder.setTitle("SCANNED");
            builder.setMessage("Kolegij skeniran. Dodano u evidenciju");
            getQRcodeSubjectContents(result.getContents().toString());
            evdDbRef.child(this.subjectUserId).child(this.subjectName).child(user.getUid()).setValue(user.getEmail());
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    });

    private void getQRcodeSubjectContents(String cnt) {
        String[] rijeci = cnt.split("\\s+");

        String zadnjaRijec = rijeci[rijeci.length - 1];
        String srednjaRijec = rijeci[rijeci.length - 2];
        this.subjectName = srednjaRijec.replaceAll("_", " ");
        this.subjectUserId = zadnjaRijec;
        subjectName2.setText(subjectName);
    }

    @Override
    public void onItemClick(User user) {

    }
    private static String removeAfterAtSymbol(String email) {
        // Define a regex pattern to match everything after the '@' symbol
        Pattern pattern = Pattern.compile("@.*");

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Check if the pattern is found
        if (matcher.find()) {
            // Replace everything after '@' with an empty string
            return email.replace(matcher.group(), "");
        }

        // Return the original email if the pattern is not found
        return email;
    }
}





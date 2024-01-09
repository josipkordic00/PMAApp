package ba.sum.fpmoz.pmaapp;

import android.app.AlertDialog;
import android.app.Instrumentation;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class StudentActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logout, addSubject;
    TextView emailProff;
    RecyclerView subjectsView;

    FirebaseUser user;
    DatabaseReference databaseUsersReference, databaseSubjectsReference;
    String subjectName, subjectUserId;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_view);

        auth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);

        emailProff = findViewById(R.id.emailProff);
        user = auth.getCurrentUser();
        DatabaseReference usersDbRef = this.mDatabase.getReference("users");
        DatabaseReference evdDbRef = this.mDatabase.getReference("evidentions");

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
        scanCode();

        //evdDbRef.child(this.subjectUserId).child(this.subjectName).setValue(user.getUid());


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
            evdDbRef.child(this.subjectUserId).child(this.subjectName).child(user.getUid().toString()).setValue(user.getEmail());

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
    }
}





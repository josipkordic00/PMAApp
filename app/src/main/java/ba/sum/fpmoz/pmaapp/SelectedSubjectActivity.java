package ba.sum.fpmoz.pmaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;


import ba.sum.fpmoz.pmaapp.adapters.SubjectAdapter;
import ba.sum.fpmoz.pmaapp.adapters.UserAdapter;
import ba.sum.fpmoz.pmaapp.models.Subject;
import ba.sum.fpmoz.pmaapp.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SelectedSubjectActivity extends AppCompatActivity implements UserAdapter.OnItemClickListener {

    TextView subjectName;
    FirebaseAuth auth;
    RecyclerView usersView;
    ArrayList<User> list;
    UserAdapter adapter;

    ImageView emailIcon22, backBtn;

    FirebaseUser user;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_subject);

        subjectName = findViewById(R.id.subjectName);
        backBtn = findViewById(R.id.imageView6);
        usersView = findViewById(R.id.usersView);
        emailIcon22 = findViewById(R.id.emailIcon22);
        DatabaseReference usersDbRef = this.mDatabase.getReference("users");
        DatabaseReference selectedSDbRef = this.mDatabase.getReference("selected_subject");
        DatabaseReference evidentionDbRef = this.mDatabase.getReference("evidentions");

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        list = new ArrayList<>();
        usersView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, list, this);
        usersView.setAdapter(adapter);
        evidentionDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot parentSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot childSnapshot : parentSnapshot.getChildren()) {
                        Log.d("wa","sa");
                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        usersDbRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String FirstName = snapshot.child("name").getValue(String.class);

                    subjectName.setText(FirstName);
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
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                Log.d("a22",formatCurrentDateTime() + " " + item.getName().toString());

                try {
                    String replacedString = item.getName().toString().replaceAll("\\s", "_");
                    BitMatrix bitMatrix = multiFormatWriter.encode(formatCurrentDateTime() + " " + replacedString + " "+ item.getUserId().toString(), BarcodeFormat.QR_CODE,500,500);

                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                    emailIcon22.setImageBitmap(bitmap);

                }catch (WriterException e){
                    throw  new RuntimeException(e);
                }

                subjectName.setText(item.getName().toString());

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
    public static String formatCurrentDateTime() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH-dd-MM-yyyy", Locale.getDefault());
        String formattedDateTime = dateFormat.format(currentDate);

        return formattedDateTime;
    }

    @Override
    public void onItemClick(User user) {
        Log.d("ItemClicked", "Subject: " + user.toString());
    }
}
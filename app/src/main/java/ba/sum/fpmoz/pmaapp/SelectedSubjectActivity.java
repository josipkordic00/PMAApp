package ba.sum.fpmoz.pmaapp;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    TextView subjectName, textView, textView12, textView22, textView3, textView4, textView5;
    FirebaseAuth auth;
    Button backBtn;
    View divider;
    RecyclerView usersView;
    ArrayList<String> list;
    UserAdapter adapter;

    Boolean slide = false;

    ImageView emailIcon22, imageView7, logout;
    String subject;
    FirebaseUser user;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_subject);

        subjectName = findViewById(R.id.emailProff2);
        backBtn = findViewById(R.id.button32);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfessorActivity.class);
                startActivity(intent);
                finish();
            }
        });
        usersView = findViewById(R.id.usersView);
        emailIcon22 = findViewById(R.id.emailIcon22);
        textView = findViewById(R.id.textView);
        textView12 = findViewById(R.id.textView12);
        textView22 = findViewById(R.id.textView22);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        divider = findViewById(R.id.divider2);
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
        DatabaseReference subDbRef = this.mDatabase.getReference("subjects");

        DatabaseReference prevDbRef = this.mDatabase.getReference("prev_evidentions");
        DatabaseReference selectedSDbRef = this.mDatabase.getReference("selected_subject");
        DatabaseReference evidentionDbRef = this.mDatabase.getReference("evidentions");
        imageView7 = findViewById(R.id.imageView7);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        textView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Evidence_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subDbRef.child(user.getUid()).child(subjectName.getText().toString()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // The user has been successfully deleted
                                // Handle success, if needed
                                Intent intent = new Intent(getApplicationContext(), ProfessorActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(SelectedSubjectActivity.this, "Delete Success", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure
                                // Log the error or take appropriate action
                                Toast.makeText(SelectedSubjectActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(slide.equals(true)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Set visibility to VISIBLE

                            String hexColorCode = "#2b2a2a";
                            int color = Color.parseColor(hexColorCode);
                            imageView7.setBackgroundColor(color);
                            textView.setVisibility(View.INVISIBLE);
                            textView5.setVisibility(View.INVISIBLE);
                        }
                    }, 300);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Set visibility to VISIBLE
                            textView12.setVisibility(View.INVISIBLE);
                            textView4.setVisibility(View.INVISIBLE);
                        }
                    }, 200);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Set visibility to VISIBLE
                            divider.setVisibility(View.INVISIBLE);
                            textView22.setVisibility(View.INVISIBLE);
                            textView3.setVisibility(View.INVISIBLE);
                        }
                    }, 100);
                    slide=false;
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Set visibility to VISIBLE
                            textView.setVisibility(View.VISIBLE);

                            textView5.setText(subjectName.getText().toString());
                            textView5.setVisibility(View.VISIBLE);
                            imageView7.setBackgroundColor(Color.BLACK);
                        }
                    }, 100);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Set visibility to VISIBLE
                            textView4.setVisibility(View.VISIBLE);
                            textView4.setText(subjectName.getText().toString());
                            textView12.setVisibility(View.VISIBLE);
                        }
                    }, 200);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Set visibility to VISIBLE
                            textView3.setVisibility(View.VISIBLE);
                            divider.setVisibility(View.VISIBLE);
                            textView3.setText(subjectName.getText().toString());
                            textView22.setVisibility(View.VISIBLE);
                        }
                    }, 300);
                    slide=true;

                }

            }
        });
        list = new ArrayList<>();
        usersView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, list,this , this.subject );
        usersView.setAdapter(adapter);

        evidentionDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                    if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            if(userSnapshot.getKey().equals(user.getUid())){
                                for (DataSnapshot subjectSnapshot : userSnapshot.getChildren()) {
                                     if (subjectSnapshot.getKey().toString().equals(subjectName.getText().toString())) {

                                        for (DataSnapshot emailSnapshot : subjectSnapshot.getChildren()) {
                                            String email = emailSnapshot.getValue(String.class);
                                            list.add(email);

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

        emailIcon22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Date currentDate = new Date();
               SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy", Locale.getDefault());
               String e = dateFormat.format(currentDate);

               android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SelectedSubjectActivity.this);
                builder.setTitle("Alert")
                        .setMessage("Do you want to finish subject and add data to evidention?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                prevDbRef.child(user.getUid()).child(subjectName.getText().toString()).child(e).push().setValue(list);
                                evidentionDbRef.child(user.getUid()).child(subjectName.getText().toString()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // The user has been successfully deleted
                                                // Handle success, if needed
                                                Toast.makeText(SelectedSubjectActivity.this, "Data added to previous evidentions successefully", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failure
                                                // Log the error or take appropriate action
                                                Toast.makeText(SelectedSubjectActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                list.clear();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();


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
package ba.sum.fpmoz.pmaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import javax.sql.DataSource;

import ba.sum.fpmoz.pmaapp.adapters.StudentAdapter;
import ba.sum.fpmoz.pmaapp.models.Subject;
import ba.sum.fpmoz.pmaapp.models.User;

public class SingleEvidentionActivity extends AppCompatActivity implements StudentAdapter.OnItemClickListener {

    RecyclerView studentsView;
    ArrayList<String> list;
    StudentAdapter adapter;

    String subject;

    Button btnExport, backBtn;

    TextView evdName;
    String subjectName;


    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance("https://pmaapp-8b913-default-rtdb.europe-west1.firebasedatabase.app/");


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_evidention);
        studentsView = findViewById(R.id.recyclerView);
        evdName = findViewById(R.id.emailProff2);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        backBtn = findViewById(R.id.button);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Evidence_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        btnExport = findViewById(R.id.btnExport);


        list = new ArrayList<>();
        studentsView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(this, list,this , this.subject );
        studentsView.setAdapter(adapter);

        DatabaseReference prevDbRef = this.mDatabase.getReference("prev_evidentions");
        DatabaseReference selectedSDbRef = this.mDatabase.getReference("selected_subject");
        DatabaseReference selectedEDbRef = this.mDatabase.getReference("selected_evidence");

        selectedEDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                for(DataSnapshot user2snapshot : snapshot2.getChildren()){

                    Log.d("m,m", "k-" + user2snapshot.getKey().toString()+ " m- "+user.getUid());
                    if(user2snapshot.getKey().toString().equals(user.getUid())){
                        evdName.setText(user2snapshot.getValue().toString());
                        break;
                    }else{
                        evdName.setText("nooo");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csvFileBtn(list,subjectName.toString()+evdName.getText().toString());
            }
        });

        selectedSDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Subject item = snapshot.child(user.getUid()).getValue(Subject.class);
                subjectName = item.getName().toString();

                prevDbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("m,,m","d-"+dataSnapshot.toString());

                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {

                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                if(userSnapshot.getKey().equals(user.getUid())) {
                                    for (DataSnapshot subjectSnapshot : userSnapshot.getChildren()) {
                                        if(subjectSnapshot.getKey().equals(subjectName)){
                                            for (DataSnapshot dateSnapshot : subjectSnapshot.getChildren()) {
                                                if(dateSnapshot.getKey().toString().equals(evdName.getText().toString())) {
                                                    for (DataSnapshot user3Snapshot : dateSnapshot.getChildren()) {
                                                        for(DataSnapshot user4Snapshot : user3Snapshot.getChildren()){
                                                            Log.d("m,,m", "e- "+user4Snapshot.getValue().toString());
                                                            list.add(user4Snapshot.getValue().toString());
                                                        }

                                                    }
                                                }
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
    public void onItemClick(User user) {

    }

    public void csvFileBtn(ArrayList<String> list, String name) {
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Sheet");


        for (int i = 0; i < list.size(); i++) {
            HSSFRow hssfRow = hssfSheet.createRow(i);
            HSSFCell hssfCell = hssfRow.createCell(0);
            hssfCell.setCellValue(list.get(i).toString());
        }

        saveWorkbook(hssfWorkbook, name);
    }

    private void saveWorkbook(HSSFWorkbook hssfWorkbook, String name){
        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);//internal storage
        File fileOutput = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            fileOutput = new File(storageVolume.getDirectory().getPath()+ "/Download/"+name+".xlsx");
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileOutput);
            hssfWorkbook.write(fileOutputStream);
            fileOutputStream.close();
            hssfWorkbook.close();
            Toast.makeText(SingleEvidentionActivity.this, "File created successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(SingleEvidentionActivity.this, "File creation failed!", Toast.LENGTH_SHORT).show();

            Log.d("m,m",e.toString());

        }

    }




}
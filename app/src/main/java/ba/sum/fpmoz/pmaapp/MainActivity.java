package ba.sum.fpmoz.pmaapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button logout;
    TextView userDetails;
    FirebaseUser user;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        userDetails = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            // Fetch additional user data from the database
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String email = snapshot.child("email").getValue(String.class);
                        String firstName = snapshot.child("firstName").getValue(String.class);
                        String lastName = snapshot.child("lastName").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String role = snapshot.child("role").getValue(String.class);

                        // Display the user details in the TextView
                        String userDetailsText = "Email: " + email + "\n"
                                + "First Name: " + firstName + "\n"
                                + "Last Name: " + lastName + "\n"
                                + "Phone: " + phone + "\n"
                                + "Role: " + role;
                        userDetails.setText(userDetailsText);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //error
                }
            });
        }

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

package ba.sum.fpmoz.pmaapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ba.sum.fpmoz.pmaapp.R;
import ba.sum.fpmoz.pmaapp.models.*;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    private final Context context;
    private final ArrayList<String> list; // Corrected field name
    private final OnItemClickListener listener;

    private final String subject;

    public StudentAdapter(Context context, ArrayList<String> list, OnItemClickListener listener, String subject) {
        this.context = context;
        this.list = list; // Corrected field name
        this.listener = listener;
        this.subject = subject;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.student_entry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String user = list.get(position);
        holder.user.setText(user);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.subjectName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("rara","user clicked");
                }
            });
        }
    }
}

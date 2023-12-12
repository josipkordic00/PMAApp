package ba.sum.fpmoz.pmaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ba.sum.fpmoz.pmaapp.R;
import ba.sum.fpmoz.pmaapp.models.Subject;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Subject subject);
    }

    private final Context context;
    private final ArrayList<Subject> list; // Corrected field name
    private final OnItemClickListener listener;

    public SubjectAdapter(Context context, ArrayList<Subject> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list; // Corrected field name
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.subject_entry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Subject subject = list.get(position);
        holder.subject.setText(subject.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView subject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subjectName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(list.get(position)); // Corrected field name
                    }
                }
            });
        }
    }
}

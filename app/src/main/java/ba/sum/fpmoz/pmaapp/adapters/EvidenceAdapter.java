package ba.sum.fpmoz.pmaapp.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ba.sum.fpmoz.pmaapp.R;
import ba.sum.fpmoz.pmaapp.models.Subject;

public class EvidenceAdapter extends RecyclerView.Adapter<EvidenceAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(String evidence);
    }

    private final Context context;
    private final ArrayList<String> list; // Corrected field name
    private final OnItemClickListener listener;

    public EvidenceAdapter(Context context, ArrayList<String> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list; // Corrected field name
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.evidention_entry, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String evidence = list.get(position);
        holder.evidence.setText(evidence);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView evidence;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            evidence = itemView.findViewById(R.id.subjectName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(list.get(position));

                    }
                }
            });
        }
    }
}

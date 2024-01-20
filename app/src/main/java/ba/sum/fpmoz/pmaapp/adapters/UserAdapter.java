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

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    private final Context context;
    private final ArrayList<String> list; // Corrected field name
    private final OnItemClickListener listener;

    public UserAdapter(Context context, ArrayList<String> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list; // Corrected field name
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_entry, parent, false);
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

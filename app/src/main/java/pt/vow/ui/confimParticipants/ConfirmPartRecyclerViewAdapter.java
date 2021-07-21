package pt.vow.ui.confimParticipants;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;

public class ConfirmPartRecyclerViewAdapter extends RecyclerView.Adapter<ConfirmPartRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<String> participantsList;
    List<String> participantsConfirmedList;


    public ConfirmPartRecyclerViewAdapter(Context context, List<String> participantsList, List<String> participantsConfirmedList) {
        this.context = context;
        this.participantsList = participantsList;
        this.participantsConfirmedList = participantsConfirmedList;
    }

    @NonNull
    @Override
    public ConfirmPartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_confirm_participants_row, parent, false);
        return new ConfirmPartRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmPartRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textViewPartName.setText(participantsList.get(position));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBox.isChecked())
                    participantsConfirmedList.add(participantsList.get(position));
                else
                    participantsConfirmedList.remove(participantsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return participantsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPartName;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPartName = itemView.findViewById(R.id.textViewPartName);
            checkBox = itemView.findViewById(R.id.checkboxPart);
        }
    }
}

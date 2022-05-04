package com.example.usmile.user.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.usmile.R;
import com.example.usmile.user.models.InstructionItem;

import java.util.List;

public class InstructionItemAdapter extends RecyclerView.Adapter<InstructionItemAdapter.InstructionItemViewHolder> {

    List<InstructionItem> instructionItems;

    public InstructionItemAdapter(List<InstructionItem> instructionItems) {
        this.instructionItems = instructionItems;
    }

    @NonNull
    @Override
    public InstructionItemAdapter.InstructionItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_instruction_item_layout, parent, false);

        return new InstructionItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionItemAdapter.InstructionItemViewHolder holder, int position) {

        InstructionItem item = instructionItems.get(position);

        holder.answerTextView.setText(item.getAnswer());
        holder.questionTextView.setText(item.getQuestion());

        boolean isExpandable = item.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (instructionItems == null)
            return 0;
        return instructionItems.size();
    }

    public class InstructionItemViewHolder extends RecyclerView.ViewHolder{

        TextView questionTextView, answerTextView;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;



        public InstructionItemViewHolder(@NonNull View itemView) {
            super(itemView);

            questionTextView = (TextView) itemView.findViewById(R.id.questionTextView);
            answerTextView = (TextView) itemView.findViewById(R.id.answerTextView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            expandableLayout = (RelativeLayout) itemView.findViewById(R.id.expandableLayout);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InstructionItem item = instructionItems.get(getAbsoluteAdapterPosition());
                    item.setExpandable(!item.isExpandable());
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });
        }
    }
}

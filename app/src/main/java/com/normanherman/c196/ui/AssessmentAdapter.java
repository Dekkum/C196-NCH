package com.normanherman.c196.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.normanherman.c196.AssessmentDetailsActivity;
import com.normanherman.c196.AssessmentEditActivity;
import com.normanherman.c196.R;
import com.normanherman.c196.models.Assessment;
import com.normanherman.c196.utilities.TextFormatting;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.normanherman.c196.utilities.Constants.ASSESSMENT_ID_KEY;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {

    private final List<Assessment> mAssessments;
    private final Context mContext;
    private final RecyclerContext rContext;
    private AssessmentSelectedListener assessmentSelectedListener;

    public AssessmentAdapter(List<Assessment> mAssessments, Context mContext, RecyclerContext rContext, AssessmentSelectedListener assessmentSelectedListener) {
        this.mAssessments = mAssessments;
        this.mContext = mContext;
        this.rContext = rContext;
        this.assessmentSelectedListener = assessmentSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.assessment_list_cardview, parent, false);
        return new ViewHolder(view, assessmentSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.ViewHolder holder, int position) {
        final Assessment assessment = mAssessments.get(position);
        holder.tvTitle.setText(assessment.getTitle());
        holder.tvDate.setText(TextFormatting.cardDateFormat.format(assessment.getDate()));

        switch(rContext) {
            case MAIN:
                holder.assFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_edit));
                holder.assImageBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, AssessmentDetailsActivity.class);
                    intent.putExtra(ASSESSMENT_ID_KEY, assessment.getId());
                    mContext.startActivity(intent);
                });

                holder.assFab.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, AssessmentEditActivity.class);
                    intent.putExtra(ASSESSMENT_ID_KEY, assessment.getId());
                    mContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.assFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delete));
                holder.assFab.setOnClickListener(v -> {
                    if(assessmentSelectedListener != null) {
                        assessmentSelectedListener.onAssessmentSelected(position, assessment);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.card_ass_title)
        TextView tvTitle;
        @BindView(R.id.card_ass_fab)
        FloatingActionButton assFab;
        @BindView(R.id.card_ass_date)
        TextView tvDate;
        @BindView(R.id.btn_ass_details)
        ImageButton assImageBtn;
        AssessmentSelectedListener assessmentSelectedListener;

        public ViewHolder(View itemView, AssessmentSelectedListener assessmentSelectedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.assessmentSelectedListener = assessmentSelectedListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            assessmentSelectedListener.onAssessmentSelected(getAdapterPosition(), mAssessments.get(getAdapterPosition()));
        }
    }

    public interface AssessmentSelectedListener {
        void onAssessmentSelected(int position, Assessment assessment);
    }
}

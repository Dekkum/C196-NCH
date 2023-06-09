package com.normanherman.c196;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.normanherman.c196.models.Mentor;
import com.normanherman.c196.ui.MentorAdapter;
import com.normanherman.c196.ui.RecyclerContext;
import com.normanherman.c196.viewmodel.MentorViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MentorActivity extends AppCompatActivity implements MentorAdapter.MentorSelectedListener {
    @BindView(R.id.mentor_recycler_view)
    RecyclerView mMentorRecyclerView;

    @OnClick(R.id.mentor_fab)
    void fabClickHandler() {
        Intent intent = new Intent(this, MentorEditActivity.class);
        startActivity(intent);
    }

    private List<Mentor> mentorData = new ArrayList<>();
    private MentorAdapter mMentorAdapter;
    private MentorViewModel mMentorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_main);
        Toolbar toolbar = findViewById(R.id.mentor_toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        mMentorRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMentorRecyclerView.setLayoutManager(layoutManager);
    }

    private void initViewModel() {
        final Observer<List<Mentor>> mentorObserver =
            mentorEntities -> {
                mentorData.clear();
                mentorData.addAll(mentorEntities);

                if(mMentorAdapter == null) {
                    mMentorAdapter = new MentorAdapter(mentorData, MentorActivity.this, RecyclerContext.MAIN, this);
                    mMentorRecyclerView.setAdapter(mMentorAdapter);
                } else {
                    mMentorAdapter.notifyDataSetChanged();
                }
            };
        mMentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mMentorViewModel.mMentors.observe(this, mentorObserver);
    }

    @Override
    public void onMentorSelected(int position, Mentor mentor) {

    }
}

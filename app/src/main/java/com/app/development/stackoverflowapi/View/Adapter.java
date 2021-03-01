package com.app.development.stackoverflowapi.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.app.development.stackoverflowapi.Model.EachQuestion;
import com.app.development.stackoverflowapi.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

    Context context;
    Holder holder1;
    ArrayList<EachQuestion> questionsListExample; //testing
    private List<EachQuestion> questionsList = new ArrayList<EachQuestion>();
    RecyclerView recyclerView;

    public Adapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_user, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        holder1 = holder;
        final EachQuestion eachQuestion = questionsList.get(position);


        holder.textViewTitle.setText(eachQuestion.getTitle());
        holder.textViewLikes.setText(eachQuestion.getScore().toString());
        holder.textViewComments.setText(eachQuestion.getAnswer_count().toString());

        Date date = new java.util.Date(Integer.parseInt(eachQuestion.getCreation_date())*1000L);
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT-4"));
        String formattedDate = sdf.format(date);
        holder.textViewTime.setText("Created on "+formattedDate);



        List<String> tags = eachQuestion.getTags();
        int size = tags.size();
        if (size == 1) {
            holder.textViewTag1.setText("  " + tags.get(0).toString() + "  ");
        } else if (size == 2) {
            holder.textViewTag1.setText("  " + tags.get(0).toString() + "  ");
            holder.textViewTag2.setText("  " + tags.get(1).toString() + "  ");
        } else if (size == 3) {
            holder.textViewTag1.setText("  " + tags.get(0).toString() + "  ");
            holder.textViewTag2.setText("  " + tags.get(1).toString() + "  ");
            holder.textViewTag3.setText("  " + tags.get(2).toString() + "  ");
        } else {
            holder.textViewTag1.setText("  " + tags.get(0).toString() + "  ");
            holder.textViewTag2.setText("  " + tags.get(1).toString() + "  ");
            holder.textViewTag3.setText("  " + tags.get(2).toString() + "  ");
        }
        holder.view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("URL", eachQuestion.getLink());
                context.startActivity(intent);
            }
        });

        HashMap<String, String> map = eachQuestion.getOwner();
        //holder.textViewReputation.setText(map.get("accept_rate"));

    }


    public int getItemCount() {
        return questionsList.size();
    }

    public void updateList(List<EachQuestion> questionsList) {
        questionsListExample = new ArrayList<>(questionsList); //testing
        this.questionsList = questionsList;
        notifyDataSetChanged();
    }

    public Filter getFilter(){

        return searchFilter;
    }
    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<EachQuestion> filteredQuestionList = new ArrayList<EachQuestion>();
            if(charSequence.length()==0||charSequence==null){
                filteredQuestionList.addAll(questionsListExample);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(EachQuestion eachQuestion : questionsListExample){
                    if (eachQuestion.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredQuestionList.add(eachQuestion);
                    }

                }
            }
            FilterResults res = new FilterResults();
            res.values = filteredQuestionList;
            return res;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            questionsList.clear();
            try {
                questionsList.addAll((ArrayList)filterResults.values);
            }catch (Exception ignored){
                Toast.makeText(context, "Your List of questions is empty, so we can't perform search operation", Toast.LENGTH_SHORT).show();
            }

            notifyDataSetChanged();

        }
    };


    static class Holder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewTag1, textViewTag2, textViewTag3, textViewTime, textViewLikes, textViewComments;
        View view2;

        Holder(@NonNull View itemView) {
            super(itemView);
            view2 = itemView;
            textViewTitle = itemView.findViewById(R.id.title);
            textViewTag1 = itemView.findViewById(R.id.tag_1);
            textViewTag2 = itemView.findViewById(R.id.tag_2);
            textViewTag3 = itemView.findViewById(R.id.tag_3);
            textViewTime = itemView.findViewById(R.id.time_of_question);
            textViewLikes = itemView.findViewById(R.id.no_of_up_votes);
            textViewComments = itemView.findViewById(R.id.no_of_answers);

        }


    }


}

package se.kth.martsten.lab_1;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.kth.martsten.lab_1.db.Result;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private final ArrayList<Result> resultsList;
    private final Context context;

    public CustomAdapter(List<Result> resultsList, Context context) {
        this.resultsList = new ArrayList<>(resultsList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.results_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cardTitleView.setText(context.getString(R.string.title, resultsList.get(position).getStimuli(), resultsList.get(position).getN()));
        holder.cardSubtitleView.setText(context.getString(R.string.result_info,
                resultsList.get(position).getNumberOfEvents(), (float)resultsList.get(position).getTimeBetweenEvents() / 1000f, resultsList.get(position).getScore(), resultsList.get(position).getPerfectScore()));
        holder.cardPercentView.setText(context.getString(R.string.percent,
                resultsList.get(position).getPerfectScore() == 0 ? 100f : (float)resultsList.get(position).getScore()/(float)resultsList.get(position).getPerfectScore() * 100f));
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cardTitleView, cardSubtitleView, cardPercentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardTitleView = itemView.findViewById(R.id.result_title);
            cardSubtitleView = itemView.findViewById(R.id.result_subtitle);
            cardPercentView = itemView.findViewById(R.id.result_percent);
        }
    }
}

package com.example.moviedatabase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviedatabase.Films;
import com.example.moviedatabase.R;
import com.example.moviedatabase.model.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.FilmsViewHolder> {


    Context context;
    private ArrayList<Result> dataList = new ArrayList<>();
    private final ListItemClickListerner<Result> mOnclickListener;
    private String textView = "";

    public interface ListItemClickListerner<T> {
        void onListItemClicked(Result t);
    }

    public FilmsAdapter(Context context, ListItemClickListerner onClickListener) {
        this.context = context;
        this.mOnclickListener = onClickListener;
    }

    public void setMovieVMList (ArrayList<Result> movieVMList) {
        dataList = new ArrayList<>();
        dataList = movieVMList;
        notifyDataSetChanged();
    }

    @Override
    public FilmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.image_layout, parent, false);
        return new FilmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmsViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnclickListener.onListItemClicked(dataList.get(position));
            }
        });
        holder.title.setId(position);
        int i = holder.title.getId();
        holder.title.setText(dataList.get(i).getTitle());


        Glide.with(context).load("https://image.tmdb.org/t/p/w185/"+dataList.get(position).getPosterPath()).into(holder.image);
        textView = dataList.get(position).getTitle();
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class FilmsViewHolder extends RecyclerView.ViewHolder {
        private TextView title, year, description, review;
        private ImageView image;
        private View itemView;

        public FilmsViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            //title = (TextView) itemView.findViewById(R.id.titledetail);
            //year = (TextView) itemView.findViewById(R.id.yeardetail);
            //description = (TextView) itemView.findViewById(R.id.descriptiondetail);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.titlecard);
            // review = (TextView) itemView.findViewById(R.id.reviewtextview);
        }
    }
}
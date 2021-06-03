package com.techbull.InterviewTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Context context;
    private List<SearchDto> searchDtoList;

    public SearchAdapter(Context context, List<SearchDto> searchDtoList) {
        this.context = context;
        this.searchDtoList = searchDtoList;
    }

    @Override
    public int getItemViewType(int position) {
        return searchDtoList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_obj, parent, false);
            return new SearchAdapter.MyViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new SearchAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchDto searchDto = searchDtoList.get(position);
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Glide.with(context)
                    .load(searchDto.getPoster())
                    .error(R.drawable.ic_baseline_broken_image)
                    .into(myViewHolder.imageView);
            myViewHolder.tvMovieName.setText(searchDto.getTitle()+" ("+searchDto.getYear()+")");
        }else{

        }
    }

    @Override
    public int getItemCount() {
        return searchDtoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView tvMovieName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tv_movie_name);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

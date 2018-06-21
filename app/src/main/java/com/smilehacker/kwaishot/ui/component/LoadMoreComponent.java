package com.smilehacker.kwaishot.ui.component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.smilehacker.kwaishot.R;
import com.smilehacker.lego.LegoComponent;
import com.smilehacker.lego.annotation.LegoIndex;

/**
 * Created by quan.zhou on 2017/12/11.
 */

public class LoadMoreComponent extends LegoComponent<LoadMoreComponent.ViewHolder, LoadMoreComponent.Model> {


    @Override
    public ViewHolder getViewHolder(@NonNull ViewGroup viewGroup) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_component_loadmore, viewGroup, false));
    }

    @Override
    public void onBindData(@NonNull ViewHolder viewHolder, @NonNull Model model) {
//        viewHolder.mProgressBar.getIndeterminateDrawable()
//                .setColorFilter(ResourceUtils.getColor(R.color.common_base_color), PorterDuff.Mode.MULTIPLY);
    }

    public static class Model {
        @LegoIndex
        public String id = "loadmore" + this.hashCode();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            mProgressBar = itemView.findViewById(R.id.progress);
        }
    }
}

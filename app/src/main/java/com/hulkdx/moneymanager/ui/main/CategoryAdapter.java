/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/20/2017.
 */
package com.hulkdx.moneymanager.ui.main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.injection.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private List<Category> mCategories;

    @Inject
    public CategoryAdapter(@ApplicationContext Context context) {
        mCategories = new ArrayList<>();
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_category, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.dividerVerticalView.setVisibility((position % 2 == 0) ? View.GONE : View.VISIBLE);
        holder.nameTV.setText(mCategories.get(position).getName());
        holder.hexColorView.setBackgroundColor(mCategories.get(position).getHexColor());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_textview) TextView nameTV;
        @BindView(R.id.view_hex_color) View hexColorView;
        @BindView(R.id.divider_vertical) View dividerVerticalView;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

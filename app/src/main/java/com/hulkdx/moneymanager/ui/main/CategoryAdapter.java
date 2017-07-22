/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/20/2017.
 */
package com.hulkdx.moneymanager.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hulkdx.moneymanager.R;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.injection.ApplicationContext;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {

    private List<Category> mCategories;
    private Callback mCallback;
    private ImageView currentSelectedImage = null;

    @Inject
    public CategoryAdapter(@ApplicationContext Context context) {
        mCategories = new ArrayList<>();
    }

    public void setCategories(List<Category> categories) {
        mCategories = categories;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_category, parent, false);
        return new CategoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.setCategory(mCategories.get(position));
        holder.dividerVerticalView.setVisibility((position % 2 == 0) ? View.GONE : View.VISIBLE);
        holder.nameTV.setText(mCategories.get(position).getName());
        holder.hexColorImageView.setBackgroundColor(mCategories.get(position).getHexColor());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_textview) TextView nameTV;
        @BindView(R.id.view_hex_color) ImageView hexColorImageView;
        @BindView(R.id.divider_vertical) View dividerVerticalView;

        public Category category;

        public CategoryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        void OnItemClicked() {
            if (mCallback != null) {
                mCallback.onCategoryClicked(category.getId());
                hexColorImageView.setImageResource(R.drawable.ic_category_selected);
                // Remove the previous selected ImageView drawable.
                if (currentSelectedImage != null) currentSelectedImage.setImageResource(0);
                currentSelectedImage = hexColorImageView;
            }
        }

        public void setCategory(Category category) {
            this.category = category;
        }
    }

    interface Callback {
        void onCategoryClicked(long categoryId);
    }
}

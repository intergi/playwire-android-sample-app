package com.example.demo_java.ad_types;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo_java.R;

public class AdTypesAdapter extends ListAdapter<Pair<String, Class<? extends Activity>>, AdTypesAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Pair<String, Class<? extends Activity>> adUnit);
    }

    public AdTypesAdapter(OnItemClickListener onItemClickListener) {
        super(DIFF_CALLBACK);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ad_unit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<String, Class<? extends Activity>> adUnit = getItem(position);
        holder.bind(adUnit);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(adUnit));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView adUnitAliasTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adUnitAliasTextView = itemView.findViewById(R.id.ad_unit_name);
        }

        public void bind(Pair<String, Class<? extends Activity>> adUnit) {
            adUnitAliasTextView.setText(adUnit.first);
        }
    }

    private static final DiffUtil.ItemCallback<Pair<String, Class<? extends Activity>>> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Pair<String, Class<? extends Activity>> oldItem, @NonNull Pair<String, Class<? extends Activity>> newItem) {
                    return oldItem.second.equals(newItem.second);
                }

                @Override
                public boolean areContentsTheSame(@NonNull Pair<String, Class<? extends Activity>> oldItem, @NonNull Pair<String, Class<? extends Activity>> newItem) {
                    return oldItem.equals(newItem);
                }
            };
}

package com.example.demo_java.ad_types;

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
import com.intergi.playwiresdk.PWAdMode;

public class AdTypesAdapter extends ListAdapter<Pair<PWAdMode, String>, AdTypesAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Pair<PWAdMode, String> adUnit);
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
        Pair<PWAdMode, String> adUnit = getItem(position);
        holder.bind(adUnit);
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(adUnit));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView adUnitAliasTextView;
        private final TextView adUnitModeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adUnitAliasTextView = itemView.findViewById(R.id.ad_unit_name);
            adUnitModeTextView = itemView.findViewById(R.id.ad_unit_mode);
        }

        public void bind(Pair<PWAdMode, String> adUnit) {
            adUnitAliasTextView.setText(adUnit.second);
            adUnitModeTextView.setText(adUnit.first.name());
        }
    }

    private static final DiffUtil.ItemCallback<Pair<PWAdMode, String>> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull Pair<PWAdMode, String> oldItem, @NonNull Pair<PWAdMode, String> newItem) {
                    return oldItem.second.equals(newItem.second);
                }

                @Override
                public boolean areContentsTheSame(@NonNull Pair<PWAdMode, String> oldItem, @NonNull Pair<PWAdMode, String> newItem) {
                    return oldItem.equals(newItem);
                }
            };
}

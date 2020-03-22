package ch.epfl.qedit.view.util;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;
import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;
import static androidx.recyclerview.widget.ItemTouchHelper.DOWN;
import static androidx.recyclerview.widget.ItemTouchHelper.UP;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.qedit.R;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListEditView extends RecyclerView {

    public interface GetItemText<T> {
        String getText(T item);
    }

    private ListEditAdapter adapter;
    private int selectedQuestion = NO_POSITION;

    private final class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView text;
        private final LinearLayout overlayButtons;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
            overlayButtons = itemView.findViewById(R.id.overlay_buttons);
            itemView.setOnClickListener(this);
        }

        void onTextChanged(String newText) {
            text.setText(newText);
        }

        void onSelected(boolean selected) {
            overlayButtons.setVisibility(selected ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View v) {
            int previousQuestion = selectedQuestion;
            selectedQuestion = getLayoutPosition();

            if (previousQuestion == selectedQuestion) {
                selectedQuestion = NO_POSITION;
                adapter.notifyItemChanged(previousQuestion);
            } else {
                adapter.notifyItemChanged(previousQuestion);
                adapter.notifyItemChanged(selectedQuestion);
            }
        }
    }

    public static class ListEditAdapter<T> extends RecyclerView.Adapter<ItemHolder> {

        private final List<T> items;
        private final GetItemText<T> getText;
        private ListEditView listEditView;

        private class ItemTouchCallback extends ItemTouchHelper.Callback {
            @Override
            public int getMovementFlags(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ACTION_STATE_IDLE, UP | DOWN)
                        | makeFlag(ACTION_STATE_DRAG, UP | DOWN);
            }

            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        }

        public ListEditAdapter(List<T> items, GetItemText<T> getText) {
            this.items = Objects.requireNonNull(items);
            this.getText = Objects.requireNonNull(getText);
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return listEditView
            .new ItemHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.list_edit_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            holder.onTextChanged(getText.getText(items.get(position)));
            holder.onSelected(position == listEditView.selectedQuestion);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private void moveItem(int from, int to) {
            if (from == to) return;

            if (from < to) {
                for (int i = from; i < to; i++) Collections.swap(items, i, i + 1);
            } else {
                for (int i = from; i > to; i--) Collections.swap(items, i, i - 1);
            }

            if (from == listEditView.selectedQuestion) listEditView.selectedQuestion = to;

            notifyItemMoved(from, to);
        }

        private void setListEditView(ListEditView listEditView) {
            this.listEditView = listEditView;
        }
    }

    public ListEditView(Context context) {
        super(context);
        init(context);
    }

    public ListEditView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setAdapter(ListEditAdapter adapter) {
        this.adapter = Objects.requireNonNull(adapter);
        adapter.setListEditView(this);
        super.setAdapter(adapter);
        new ItemTouchHelper(adapter.new ItemTouchCallback()).attachToRecyclerView(this);
    }

    private void init(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        super.setHasFixedSize(true);
        super.setLayoutManager(new LinearLayoutManager(context));
        super.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
    }
}

package pl.com.weddingPlanner.view.list;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import pl.com.weddingPlanner.R;

public class ListRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
//        implements SwipeAndDragHelper.ActionCompletionContract
{
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private static final int TYPE_EMPTY = 3;
    private static final int TYPE_ERROR = 4;

    private boolean isLoaderVisible = false;
    private boolean isEmpty = false;
    private boolean isError = false;
    private final OnClickListener itemClickListener;

    @Getter
    private List<ListItem> items;

    private String emptyFieldMessage;
    private Context context;

    @Getter
    private boolean iconEnabled = false;

    private @DrawableRes
    int iconDrawable;

    private int iconVisibility;

//    @Setter
//    private OnClickListener rightIconClickListener;

//    @Setter
//    private Listener itemSwipedListener;

    public ListRecyclerAdapter(Context context, List<ListItem> items, OnClickListener itemClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
        this.emptyFieldMessage = context.getResources().getString(R.string.list_standard_caption_empty);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View v = inflater.inflate(R.layout.list_standard_header, parent, false);
            return new VHHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View v = inflater.inflate(R.layout.list_standard_item, parent, false);
            return new VHItem(v);
        } else if (viewType == TYPE_LOADING) {
            View v = inflater.inflate(R.layout.list_standard_loading, parent, false);
            return new VHLoading(v);
        } else if (viewType == TYPE_EMPTY) {
            View v = inflater.inflate(R.layout.list_standard_empty, parent, false);
            TextView textView = v.findViewById(R.id.empty_message);
            textView.setText(emptyFieldMessage);
            return new VHEmpty(v);
        } else {
            View v = inflater.inflate(R.layout.list_standard_empty, parent, false);
            TextView textView = v.findViewById(R.id.empty_message);
            textView.setText(emptyFieldMessage);
            return new VHEmpty(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader) {
            HeaderItem currentItem = (HeaderItem) items.get(holder.getAdapterPosition());
            VHHeader VHheader = (VHHeader) holder;
            VHheader.txtTitle.setText(currentItem.getHeader());
        } else if (holder instanceof VHItem) {
            ContentItem currentItem = (ContentItem) items.get(holder.getAdapterPosition());
            VHItem VHitem = (VHItem) holder;
//            if (currentItem.isDisabled()) {
//                int colorGray = ContextCompat.getColor(context, R.color.gray_B4B4B4);
//                VHitem.disableClick();
//                VHitem.caption.setText(currentItem.getMainCaption());
//                VHitem.caption.setTextColor(colorGray);
////                VHitem.amountCaption.setText(currentItem.getSubCaption());
////                VHitem.amountCaption.setTextColor(colorGray);
//                VHitem.leftIcon.setImageDrawable(getIcon(currentItem));
//                VHitem.leftIcon.setColorFilter(colorGray, PorterDuff.Mode.SRC_IN);
////                if (isIconEnabled()) {
////                    VHitem.rightIcon.setVisibility(View.VISIBLE);
////                    VHitem.rightIcon.setImageDrawable(ContextCompat.getDrawable(context, iconDrawable));
////                    VHitem.rightIcon.setColorFilter(colorGray, PorterDuff.Mode.SRC_IN);
////                }
//            } else {
                VHitem.bind(currentItem, itemClickListener);
                VHitem.caption.setText(currentItem.getMainCaption());
                VHitem.caption.setTextColor(getColor(currentItem.getMainCaptionColor()));
                if (StringUtils.isNotBlank(currentItem.getSubCaption())) {
                    VHitem.amountCaption.setVisibility(View.VISIBLE);
                    VHitem.amountCaption.setText(currentItem.getSubCaption());
                    VHitem.amountCaption.setTextColor(getColor(currentItem.getSubCaptionColor()));
                }
                VHitem.leftIcon.setImageDrawable(getIcon(currentItem));
//                if (isIconEnabled()) {
//                    VHitem.rightIcon.setImageDrawable(ContextCompat.getDrawable(context, iconDrawable));
//                    VHitem.rightIcon.setColorFilter(ContextCompat.getColor(context, R.color.purple_500), PorterDuff.Mode.SRC_IN);
//                    ButtonHitArea.increaseHitArea(VHitem.itemView, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, context.getResources().getDisplayMetrics()), VHitem.rightIcon);
//                    VHitem.bindRightIcon(currentItem, rightIconClickListener);
//                    VHitem.rightIcon.setVisibility(iconVisibility);
//                }
//            }
        }
    }

    private int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    private Drawable getIcon(ContentItem contentItem) {
        Drawable icon = ContextCompat.getDrawable(context, contentItem.getLeftIconId());
        icon.setColorFilter(ContextCompat.getColor(context, contentItem.getLeftIconColor()), PorterDuff.Mode.SRC_IN);
        return icon;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else if (isLoaderVisible) {
            return position == items.size() - 1 ? TYPE_LOADING : TYPE_ITEM;
        } else if (isEmpty) {
            return items.size() == 1 ? TYPE_EMPTY : TYPE_ITEM;
        } else if (isError) {
            return TYPE_ERROR;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return items.get(position) instanceof HeaderItem;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void addItem(ListItem listItem) {
        if (isEmpty)
            removeEmpty();

        items.add(listItem);
        notifyDataSetChanged();
    }

    public void addItems(List<ListItem> listItems) {
        if (listItems.size() == 0 && !isEmpty && items.size() == 0)
            addEmpty();

        if (listItems.size() > 0 && isEmpty)
            removeEmpty();

        items.addAll(listItems);
        notifyDataSetChanged();
    }

    private void addEmpty() {
        if (!isEmpty) {
            isEmpty = true;
            items.add(new ListItem());
            notifyItemInserted(items.size() - 1);
        }
    }

    public void addLoading() {
        if (isEmpty)
            removeEmpty();

        if (!isLoaderVisible) {
            isLoaderVisible = true;
            items.add(new ListItem());
            notifyItemInserted(items.size() - 1);
        }
    }

    private void removeEmpty() {
        if (isEmpty) {
            isEmpty = false;
            removeLastItem();
        }
    }

    public void removeLoading() {
        if (isLoaderVisible) {
            isLoaderVisible = false;
            removeLastItem();
        }
    }

    private void removeLastItem() {
        int position = items.size() - 1;
        if (position >= 0) {
            ListItem item = items.get(position);
            if (item != null) {
                items.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public void remove(ContentItem item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void clear() {
        isEmpty = false;
        isLoaderVisible = false;

        items.clear();
        notifyDataSetChanged();
    }

    public void setEmptyFieldMessage(@StringRes int msgId) {
        this.emptyFieldMessage = context.getResources().getString(msgId);
    }

    public void setError() {
        isEmpty = false;
        isLoaderVisible = false;
        isError = true;
        notifyDataSetChanged();
    }

//    @Override
//    public void onViewMoved(int oldPosition, int newPosition) {
//        list.add(newPosition, list.remove(oldPosition));
//        notifyItemMoved(oldPosition, newPosition);
//    }

    public List<ContentItem> getContentItems() {
        List<ContentItem> contentItems = new LinkedList<>();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof ContentItem)
                contentItems.add((ContentItem) items.get(i));
        }

        return contentItems;
    }

//    @Override
//    public void onViewSwiped(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//        if (getItemCount() == 1) {
//            notifyItemRangeChanged(0, getItemCount());
//        } else {
//            notifyItemRangeChanged(position, getItemCount());
//        }
//        itemSwipedListener.execute();
//    }

    public void setIconDrawable(int iconDrawable) {
        this.iconDrawable = iconDrawable;
        this.iconEnabled = true;
    }

    public void setIconVisibility(int visibility) {
        this.iconVisibility = visibility;
    }

    static class VHHeader extends RecyclerView.ViewHolder {
        TextView txtTitle;

        VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = itemView.findViewById(R.id.standard_list_view_header_caption);
        }
    }

    public interface OnClickListener {
        void onItemClick(ContentItem item);
    }

    static class VHLoading extends RecyclerView.ViewHolder {
        VHLoading(View itemView) {
            super(itemView);
        }
    }

    static class VHEmpty extends RecyclerView.ViewHolder {
        VHEmpty(View itemView) {
            super(itemView);
        }
    }

    public interface Listener {
        void execute();
    }

    static class VHItem extends RecyclerView.ViewHolder {
        ImageView leftIcon;
        ImageView rightIcon;
        TextView caption;
        TextView amountCaption;

        VHItem(View itemView) {
            super(itemView);
            this.leftIcon = itemView.findViewById(R.id.row_icon);
            this.rightIcon = itemView.findViewById(R.id.right_icon);
            this.caption = itemView.findViewById(R.id.row_caption);
            this.amountCaption = itemView.findViewById(R.id.amount_caption);
        }

        void bind(final ContentItem item, final OnClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }

//        void bindRightIcon(final ContentItem item, final OnClickListener listener) {
//            rightIcon.setOnClickListener(v -> listener.onItemClick(item));
//        }

        void disableClick() {
            itemView.setClickable(false);
        }
    }
}
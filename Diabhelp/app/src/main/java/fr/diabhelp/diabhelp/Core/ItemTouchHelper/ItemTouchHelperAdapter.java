package fr.diabhelp.diabhelp.Core.ItemTouchHelper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by sundava on 21/10/15.
 */
public interface ItemTouchHelperAdapter {
    void onItemDismiss(RecyclerView.ViewHolder viewHolder);//, RecyclerView.ViewHolder viewHolder);
}

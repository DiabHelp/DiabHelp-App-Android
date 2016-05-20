package fr.diabhelp.diabhelp.Core.ItemTouchHelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import fr.diabhelp.diabhelp.Core.ParametresRecyclerAdapter;

/**
 * Created by sundava on 21/10/15.
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter adapter;

    public ItemTouchHelperCallback (ItemTouchHelperAdapter adapter)
    {
        this.adapter = adapter;
    }


    @Override
    public boolean isLongPressDragEnabled()
    {
        return true;
    }
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ParametresRecyclerAdapter.ParametresModuleHolder) {
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(0, swipeFlags);
        }
        Log.d("ModuleManager", "move flag not parametresholder");
        return 0;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d("ModuleManager", "OnSwipe called");
        adapter.onItemDismiss(viewHolder);
    }
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder, float dX, float dY,
                            int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Get RecyclerView item from the ViewHolder
            View itemView = viewHolder.itemView;
            c.drawColor(Color.WHITE);
            Paint p = new Paint();
            p.setColor(Color.YELLOW);
            p.setShader(new LinearGradient((float) itemView.getRight() + dX, (float) itemView.getRight(),
                            (float) 0, (float)0,
                            Color.TRANSPARENT, Color.YELLOW, Shader.TileMode.CLAMP));
                    // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
            c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                            (float) itemView.getRight(), (float) itemView.getBottom(), p);
            p.setColor(Color.BLACK);
            p.setShader(null);
            p.setTextSize(60);
            c.drawText("Supprimer", itemView.getRight() + 10 + (dX / 2), (itemView.getTop() + itemView.getBottom()) / 2, p);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}

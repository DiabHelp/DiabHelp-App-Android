package fr.diabhelp.utilizationguide;

/**
 * Created by Maxime on 28/03/2016.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[]    mDataset;
    private String      parentName;
    private String      currentName;
    private boolean     children;
    private MyArticleHandler articles;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.info_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, String cName, String pName, boolean isChildren, MyArticleHandler articleHandler) {
        mDataset = myDataset;
        currentName = cName;
        parentName = pName;
        children = isChildren;
        articles = articleHandler;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewGroup root = parent;
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //        ...

        v.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent;
                TextView t = (TextView) v.findViewById(R.id.info_text);
                String clicked = t.getText().toString();
                Log.e("item : ", clicked);
                //if the item clicked is a category, we reset the saved parent & curr name
                if (articles.isCategory(clicked)){
                    parentName = null;
                    currentName = null;
                }
                //setting an historic to identify in which category we are
                if (parentName == null){
                    parentName = clicked;
                    currentName = parentName;
                }
                else if (currentName == null)
                    currentName = clicked;
                //if we are already inside a submenu, parent shouldn't change
                else if (!articles.isRubrik(currentName) && !articles.isRubrik(clicked)){
                    parentName = currentName;
                    currentName = clicked;
                }
                else
                    currentName = clicked;

                //if we are on a child, we present the article in it's special view
                if (children)
                    intent = new Intent(root.getContext(), ArticleAdapter.class);
                else
                    intent = new Intent(root.getContext(), ManuelSubmenu.class);
                intent.putExtra("parentName", parentName);
                intent.putExtra("currentItem", currentName);
                intent.putExtra("children", children);
                intent.putExtra("title", t.getText());
                root.getContext().startActivity(intent);
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {return mDataset.length;}
}
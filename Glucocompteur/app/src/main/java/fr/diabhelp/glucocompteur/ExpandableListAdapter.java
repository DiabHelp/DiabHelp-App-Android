package fr.diabhelp.glucocompteur;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 05-Feb-16.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Item>  _data;

    public ExpandableListAdapter(ArrayList<Item> data) { _data = data; }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView     headerTitle;
        public ImageView    buttonExpand;
        public TextView     totalGlucids;
        public Item         refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitle = (TextView) itemView.findViewById(R.id.headerTitle);
            buttonExpand = (ImageView) itemView.findViewById(R.id.buttonExpand);
            totalGlucids = (TextView) itemView.findViewById(R.id.totalGlucids);
        }
    }

    private static  class ListCHildViewHolder extends RecyclerView.ViewHolder {
        public TextView     childTitle;
        public TextView     totalGLucids;

        public ListCHildViewHolder(View itemView) {
            super(itemView);
            childTitle = (TextView) itemView.findViewById(R.id.childTitle);
            totalGLucids = (TextView) itemView.findViewById(R.id.totalGlucids);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        View            view;
        LayoutInflater  inflater;

        switch (type) {
            case HEADER:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.expandable_list_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.expandable_list_child, parent, false);
                ListCHildViewHolder child = new ListCHildViewHolder(view);
                return child;
        }
        return null;
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Item item = _data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder headerHolder = (ListHeaderViewHolder) holder;
                headerHolder.refferalItem = item;
                headerHolder.headerTitle.setText(item.name);
                headerHolder.totalGlucids.setText(String.valueOf(item.glucids));
                if (item.invisibleChildren == null)
                    headerHolder.buttonExpand.setImageResource(R.drawable.circle_minus);
                else
                    headerHolder.buttonExpand.setImageResource(R.drawable.circle_plus);
                headerHolder.buttonExpand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<>();
                            int count = 0;
                            int pos = _data.indexOf(headerHolder.refferalItem);
                            while (_data.size() > pos + 1 && _data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(_data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            headerHolder.buttonExpand.setImageResource(R.drawable.circle_plus);
                        } else {
                            int pos = _data.indexOf(headerHolder.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                _data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            headerHolder.buttonExpand.setImageResource(R.drawable.circle_minus);
                            item.invisibleChildren = null;
                        }
                    }
                });
                headerHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View v) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                            alertDialogBuilder.setTitle("Supprimer le menu");
                            alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer ce menu ?")
                                    .setCancelable(false);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                _data.remove(position);
                                final MenuManager menuManager = new MenuManager(v.getContext().getApplicationInfo().dataDir + "/menus_favoris.json");
                                JSONMenuWriter writer = new JSONMenuWriter(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
                                writer.saveMenu(_data);
                                notifyDataSetChanged();
                            }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        alertDialog.show();

                        return false;
                    }
                });
                break;
            case CHILD:
                final ListCHildViewHolder childHolder = (ListCHildViewHolder) holder;
                childHolder.childTitle.setText(item.name);
                childHolder.totalGLucids.setText(String.valueOf(item.glucids));
                break;
        }
    }

    public static class Item {
        public int              type;
        public String           name;
        public Double           glucids;
        public List<Item>       invisibleChildren = new ArrayList<>();

        public Item(int type, String name, Double glucids) {
            this.type = type;
            this.name = name;
            this.glucids = glucids;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return _data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return _data.size();
    }

}

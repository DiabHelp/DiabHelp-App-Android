package fr.diabhelp.patient.Suivi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import fr.diabhelp.patient.MapActivity;
import fr.diabhelp.patient.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4kito on 07/11/2016.
 */
public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER = 0;
    public static final int CHILD = 1;
    public static final int PHONE = 0;
    public static final int ALERT = 1;
    private View _view;
    private LayoutInflater _inflater;
    ArrayList<Item> _data = new ArrayList<>();

    public ExpandableListAdapter(ArrayList<Item> data) {
        _data = data;
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView surname;
        public ImageView gps;
        public ImageView buttonExpand;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            surname = (TextView) itemView.findViewById(R.id.surname);
            gps = (ImageView) itemView.findViewById(R.id.gps);
            buttonExpand = (ImageView) itemView.findViewById(R.id.buttonExpand);
        }
    }

    private static class ListCHildViewHolder extends RecyclerView.ViewHolder {
        public TextView text;

        public ListCHildViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        switch (type) {
            case HEADER:
                _inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                _view = _inflater.inflate(R.layout.expandable_list_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(_view);
                return header;
            case CHILD:
                _inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                _view = _inflater.inflate(R.layout.expandable_list_child, parent, false);
                ListCHildViewHolder child = new ListCHildViewHolder(_view);
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
                headerHolder.name.setText(item.name);
                headerHolder.surname.setText(item.surname);
                headerHolder.gps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                        alertDialogBuilder.setTitle("Location");
                        alertDialogBuilder.setMessage("Go to map ?")
                                .setCancelable(false);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(_inflater.getContext(), MapActivity.class);
                                _inflater.getContext().startActivity(intent);
                            }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                        return;
                    }
                });
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
                        alertDialogBuilder.setTitle("Supprimer le proche de la liste");
                        alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer ce proche ?")
                                .setCancelable(false);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int pos = _data.indexOf(headerHolder.refferalItem);
                                if (item.invisibleChildren == null) {
                                    item.invisibleChildren = new ArrayList<>();
                                    int count = 0;
                                    while (_data.size() > pos + 1 && _data.get(pos + 1).type == CHILD) {
                                        item.invisibleChildren.add(_data.remove(pos + 1));
                                        count++;
                                    }
                                    notifyItemRangeRemoved(pos + 1, count);
                                }
                                while (_data.size() > pos + 1 && _data.get(pos + 1).type == CHILD) {
                                    item.invisibleChildren.add(_data.remove(pos + 1));
                                }
                                _data.remove(position);
                                //TODO Remove request API
                                notifyDataSetChanged();
                            }
                        });
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertDialog.show();
                        return false;
                    }
                });
                break;
            case CHILD:
                final ListCHildViewHolder childHolder = (ListCHildViewHolder) holder;
                if (item.childType == PHONE) {
                    childHolder.text.setText(item.phone);
                    childHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + item.phone));
                            _view.getContext().startActivity(intent);
                            return;
                        }
                    });
                } else if (item.childType == ALERT) {
                    childHolder.text.setText("Alerte");
                    childHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                            alertDialogBuilder.setTitle(item.name);
                            alertDialogBuilder.setMessage("ALERTE");
                            final AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
                }
                break;
        }
    }

    public static class Item {
        public int              type;
        public int              childType;
        public String           name;
        public String           surname;
        public Location         gps;
        public String           phone;
        public List<Item>       invisibleChildren = new ArrayList<>();

        public Item(int type, int childType, String name, String surname, Location gps, String phone) {

            this.type = type;
            this.childType = childType;
            this.name = name;
            this.surname = surname;
            this.gps = gps;
            this.phone = phone;
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

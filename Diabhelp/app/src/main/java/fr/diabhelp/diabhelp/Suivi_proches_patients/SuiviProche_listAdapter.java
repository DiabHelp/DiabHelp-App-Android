package fr.diabhelp.diabhelp.Suivi_proches_patients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.R;

/**
 * Created by Sumbers on 27/11/2015.
 */
public class SuiviProche_listAdapter extends RecyclerView.Adapter<SuiviProche_listAdapter.ViewHolder> {
    private List<Proche> listProches;
    private ListeProches context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname;
        public ImageView avatar;

        public ViewHolder(final View procheItem) {
            super(procheItem);
            nickname = (TextView) procheItem.findViewById(R.id.proche_nickname);
            avatar = (ImageView) procheItem.findViewById(R.id.proche_avatar);

//            procheItem.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    System.out.println("click !");
////                    if (procheItem.findViewById())
//                }
//            });
        }
    }

    public SuiviProche_listAdapter(List<Proche> pL, ListeProches act) {
        listProches = new ArrayList<>(pL.size());
        listProches.addAll(pL);
        context = act;
    }

    @Override
    public SuiviProche_listAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_proches_item, parent, false);
        final ViewHolder vh = new ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView rv = (RecyclerView) v.getParent();
                Integer position = rv.getChildAdapterPosition(v);
                System.out.println("click sur la position = " + position);
                context.manageActionOnClick(listProches, position, vh.avatar);
            }
        });
        return (vh);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nickname.setText(listProches.get(position).getNickName());
        if (position == (listProches.size() - 1)) {
            holder.avatar.setImageResource(R.drawable.ic_plus_proche);
        }
        else {
            holder.avatar.setImageResource(R.drawable.ic_proche);
        }
        //holder.avatar.setImageBitmap(getImageBitmap("http://3.bp.blogspot.com/-O2OyO_ea4hQ/Ty6ggILpYQI/AAAAAAAAsEA/ZIXPW0ZSVV8/s1600/Smiling+Panda.bmp"));
    }

    @Override
    public int getItemCount() {
        return (listProches.size());
    }
}

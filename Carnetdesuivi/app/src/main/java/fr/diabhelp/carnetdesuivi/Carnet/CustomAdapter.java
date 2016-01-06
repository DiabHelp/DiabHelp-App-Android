package fr.diabhelp.carnetdesuivi.Carnet;

/**
 * Created by naqued on 27/11/15.
 */
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import fr.diabhelp.carnetdesuivi.R;

public class CustomAdapter extends BaseAdapter {
    String[] result;
    Context context;
    int[] imageId;
    private static LayoutInflater inflater = null;

    public CustomAdapter(EntryActivity mainActivity, String[] prgmNameList) {
        // TODO Auto-generated constructor stub
        result = prgmNameList;
        context = mainActivity;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

/*    private int lastFocussedPosition = -1;
    private Handler handler = new Handler();

    public View getView(final int position, View convertView, ViewGroup parent) {

        // ...

        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (lastFocussedPosition == -1 || lastFocussedPosition == position) {
                                lastFocussedPosition = position;
                                edittext.requestFocus();
                            }
                        }
                    }, 200);

                } else {
                    lastFocussedPosition = -1;
                }
            }
        });

        return convertView;
    }*/
    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.listview_entry, null);
        holder.tv = (TextView) rowView.findViewById(R.id.textView1);

        holder.tv.setText(result[position]);

        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }
}
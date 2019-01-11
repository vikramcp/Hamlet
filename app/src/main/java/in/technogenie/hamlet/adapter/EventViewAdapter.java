package in.technogenie.hamlet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import in.technogenie.hamlet.R;
import in.technogenie.hamlet.beans.Event;

public class EventViewAdapter extends ArrayAdapter {
    private Context mContext;

    private String page;
    List<Event> eventList;
    //String bitMapURI;


    public EventViewAdapter(Context c, List<Event> eventList, String page) {
        super(c, R.layout.events_grid_single, eventList);

        mContext = c;
        this.eventList = eventList;
        this.page = page;
    }


    public int getCount() {
        int count = 0;
        if (eventList != null) {
            count = eventList.size();
        }

        return count;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Log.d("EventViewAdapter", "Context :" + page);
        grid = inflater.inflate(R.layout.events_grid_single, null);

        TextView eventDate = (TextView) grid.findViewById(R.id.event_date);
        TextView eventName = (TextView) grid.findViewById(R.id.event_name);
        TextView location = (TextView) grid.findViewById(R.id.location_text);
        TextView eventTime = (TextView) grid.findViewById(R.id.event_time);
        TextView status = (TextView) grid.findViewById(R.id.status);


        if (eventList != null && eventList.get(position) != null) {
            SimpleDateFormat sf = new SimpleDateFormat("d MMM yyyy");
            eventDate.setText(sf.format(eventList.get(position).getEventDate()));

            sf = new SimpleDateFormat("h:mm a");
            String startTime = sf.format(eventList.get(position).getStartTime());
            String endTime = sf.format(eventList.get(position).getEndTime());
            if (startTime != null) {
                eventTime.setText(startTime + " - " + endTime);
            }
            eventName.setText(eventList.get(position).getEventName());
            location.setText(eventList.get(position).getLocation());
            status.setText(eventList.get(position).getStatus());

        }

        return grid;


    }

}
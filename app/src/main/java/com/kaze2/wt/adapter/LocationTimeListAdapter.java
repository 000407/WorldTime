package com.kaze2.wt.adapter;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kaze2.wt.R;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Extends the Adapter class to RecyclerView.Adapter
// and implement the unimplemented methods
public class LocationTimeListAdapter
    extends RecyclerView.Adapter<LocationTimeListAdapter.ViewHolder> {
  private final List<Pair<String, ZonedDateTime>> worldTimeData;

  // Constructor for initialization
  public LocationTimeListAdapter(List<Pair<String, ZonedDateTime>> worldTimeData) {
    this.worldTimeData = worldTimeData;
  }

  @NonNull
  @Override
  public LocationTimeListAdapter.ViewHolder onCreateViewHolder(
      @NonNull ViewGroup parent, int viewType) {
    // Inflating the Layout(Instantiates list_item.xml
    // layout file into View object)
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

    // Passing view to ViewHolder
    return new ViewHolder(view);
  }

  // Binding data to the into specified position
  @Override
  public void onBindViewHolder(@NonNull LocationTimeListAdapter.ViewHolder holder, int position) {
    // TypeCast Object to int type
    final Pair<String, ZonedDateTime> timeData = worldTimeData.get(position);
    final String townName = timeData.first;
    final ZonedDateTime time = timeData.second;

    holder.townName.setText(townName);
    holder.timeDiff.setText(time.format(DateTimeFormatter.ofPattern("xxx")));
    holder.time.setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
  }

  @Override
  public int getItemCount() {
    // Returns number of items currently available in Adapter
    return worldTimeData.size();
  }

  // Initializing the Views
  public static class ViewHolder extends RecyclerView.ViewHolder {
    private final TextView townName;
    private final TextView timeDiff;
    private final TextView time;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      this.townName = itemView.findViewById(R.id.txtTownName);
      this.timeDiff = itemView.findViewById(R.id.txtTimeDiff);
      this.time = itemView.findViewById(R.id.txtTime);
    }
  }
}

package finalproject.ggcfinal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.spectate_list_layout.view.*

/**
 * Created by nelson on 22/3/18.
 */
class SpectateAdapter(val cardList: ArrayList<SpectateCard>) : RecyclerView.Adapter<SpectateAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpectateAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.spectate_list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: SpectateAdapter.ViewHolder, position: Int) {
        holder.bindItems(cardList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cardList.size
    }


    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(specCard: SpectateCard) {
            val index = itemView.insindex
            val inname = itemView.insname
            val gi = itemView.insgindex
            val rating = itemView.rating
            index.text = specCard.indx.toString()
            inname.text = specCard.name
            gi.text = specCard.gi.toString()
            rating.text = specCard.rating
        }
    }
}
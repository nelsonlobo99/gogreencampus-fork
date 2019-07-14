package finalproject.ggcfinal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.list_layout.view.*

/**
 * Created by nelson on 20/3/18.
 */
class CustomAdapter(val cardList: ArrayList<DashCard>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(cardList[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return cardList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(dashCard: DashCard) {
            val waste = itemView.waste
            val cardNote = itemView.CardNote
            val type = itemView.type
            val date = itemView.date
            val vis = itemView.vis
            val volume = itemView.vol
            waste.text = dashCard.waste
            cardNote.text = dashCard.cardnote
            type.text = dashCard.type
            date.text = dashCard.date
            vis.text = dashCard.vis
            volume.text = dashCard.vol
        }
    }
}
package ke.co.hometoctor.homedoctor.adapters


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ke.co.hometoctor.homedoctor.R
import ke.co.hometoctor.homedoctor.utility.procedure
import ke.co.hometoctor.homedoctor.utility.symptom

class symptom_item(){
    var items=ArrayList<String>()
}


class symtom_view_holder(v:View):RecyclerView.ViewHolder(v){
    val text=v.findViewById<TextView>(R.id.text_symptom)
}
class symptom_adapter(val item:symptom_item):RecyclerView.Adapter<symtom_view_holder>(){
    lateinit var fragment: Fragment
    override fun onBindViewHolder(p0: symtom_view_holder, p1: Int) {

        p0.text.setText(item.items.get(p1))
        p0.text.setOnClickListener {
            updateUISymtom(item.items.get(p1))
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): symtom_view_holder {

        return symtom_view_holder(LayoutInflater.from(p0.context).inflate(R.layout.recycle_view_symptom,p0,false))
    }

    override fun getItemCount(): Int {

        return if (item==null) 0 else item.items.size
    }

    fun updateUISymtom(symptom:String){
        val transaction=fragment.fragmentManager!!.beginTransaction()
        val st= procedure()
        val bundle=Bundle()
        bundle.putString("symptom",symptom)
        st.setArguments(bundle)

        transaction.replace(R.id.symptom_view, st,"symptom")
        transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
        //transaction.addToBackStack(null)
        transaction.commit()
    }

}
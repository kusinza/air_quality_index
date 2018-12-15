package ke.co.hometoctor.homedoctor.adapters

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ke.co.hometoctor.homedoctor.R
import ke.co.hometoctor.homedoctor.utility.procedure
import ke.co.hometoctor.homedoctor.utility.symptom

class talk_item(){
    var items=ArrayList<Map<String,String>>()
}


class talk_view_holder(v: View): RecyclerView.ViewHolder(v){
    val my_text=v.findViewById<TextView>(R.id.my_text)
    val his_text=v.findViewById<TextView>(R.id.his_text)
}
class talk_adapter(val item:talk_item): RecyclerView.Adapter<talk_view_holder>(){
    lateinit var fragment: Fragment
    override fun onBindViewHolder(p0: talk_view_holder, p1: Int) {

       for(a in item.items.get(p1))
       {
           Log.d("talk_",a.toString())
           if(a.key=="1")
           {

               p0.his_text.setText(a.value)
               p0.my_text.visibility=View.GONE
           }
           else{

               Log.d(a.key,a.value)
               p0.my_text.setText(a.value)
               p0.his_text.visibility=View.GONE
           }
       }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): talk_view_holder {

        return talk_view_holder(LayoutInflater.from(p0.context).inflate(R.layout.talk_to_us_re,p0,false))
    }

    override fun getItemCount(): Int {

        return if (item==null) 0 else item.items.size
    }


}
package ke.co.clinicktest.clinick.app.adapters

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


class UserFoundItem(val id:Int, val username:String, val location:String){



}

class SearchViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val username=view.findViewById<TextView>(R.id.username)
    val acc_type=view.findViewById<TextView>(R.id.acc_type)

}
class AdapterSearch(val item:ArrayList <UserFoundItem>, val cont: Context, val user: Bundle?, val mMap: GoogleMap?, val vv:Array<View>?, val caller:String?) : RecyclerView.Adapter<SearchViewHolder>(){


    var searched : Marker? = null;
    //var alldrinks=ArrayList<drink_adapter_content_buy>()


    override fun onBindViewHolder(p0: SearchViewHolder, p1: Int) {

        p0.username.text =item.get(p1).username


        p0.acc_type.text=item.get(p1).location

        p0.username.setOnClickListener(View.OnClickListener {
            if(caller==""){


            }
            else if(caller=="getrate"){
            }
            else{


                var perform=false
                val zoom = CameraUpdateFactory.zoomTo(15f)
                    if(searched!=null)
                        searched!!.remove();

                    val loc=item.get(p1).location
                    if(loc!=""){



                        val latin= LatLng(loc.substring(0,loc.indexOf("/")).toDouble(),loc.substring(loc.indexOf("/")+1).toDouble())
                        searched=mMap!!.addMarker(MarkerOptions().position(latin).title(item.get(p1).username))
                        this.searched!!.showInfoWindow()

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latin))
                        mMap.animateCamera(zoom)
                        mMap.setOnInfoWindowClickListener {


                        }
                        mMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener{
                            override fun onMarkerClick(p0: Marker?): Boolean {
                                Log.d("map_again","test")
                              //  util.perform=1
                                if(vv!![0].visibility== View.VISIBLE){
                                    vv!![0].performClick()
                                }
                                return true
                            }
                        })





                    }



            }

        })


    }

    override fun getItemCount(): Int {

        return if (item == null) 0 else item.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SearchViewHolder {

        return SearchViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.search_recycle,p0, false));
        //LayoutInflater.from(context).inflate(R.layout.drink_adapter_photo, parent,false)



    }

}
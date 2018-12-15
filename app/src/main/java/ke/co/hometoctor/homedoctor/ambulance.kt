package ke.co.hometoctor.homedoctor


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import ke.co.hometoctor.homedoctor.R
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.maps.android.SphericalUtil
import ke.co.clinicktest.clinick.app.adapters.AdapterSearch
import ke.co.clinicktest.clinick.app.adapters.UserFoundItem
import ke.co.hometoctor.homedoctor.adapters.symptom_adapter
import ke.co.hometoctor.homedoctor.adapters.symptom_item
import ke.co.hometoctor.homedoctor.utility.map_taskJ
import java.text.NumberFormat
import java.util.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class item(){
    var items=ArrayList<HashMap<String,GeoPoint>>()
}
class ambulance : Fragment(), OnMapReadyCallback {
    lateinit var mMap: GoogleMap
    lateinit var mapView: MapView
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var v: View

    lateinit var sphericalUtil: SphericalUtil
    lateinit var re: RecyclerView
    internal var items: ArrayList<UserFoundItem> = ArrayList<UserFoundItem>()

    var item=ke.co.hometoctor.homedoctor.item()
    lateinit var user: Bundle
    lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_ambulance, container, false)
       // username_search = v.findViewById<View>(R.id.search_text) as SearchView
      //  user = arguments
//        sphericalUtil=SphericalUtil()
        db= FirebaseFirestore.getInstance()




        val docRef = db.collection("clinics").document("clinic_location")
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null || document!!.data!=null ) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: " + document.data)
                        if(document.data==null){

                        }
                        else{
                            item=document.toObject(ke.co.hometoctor.homedoctor.item::class.java) as item

                            Log.d(ContentValues.TAG, "Value is: " +document.data)


                        }

                    } else {
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }


        this.v=v

        mapView = v.findViewById<View>(R.id.mapview) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        //  getActivity().findViewById(R.id.bottom_panel_home_main).setVisibility(View.GONE);

        mapView.getMapAsync(this)


        return v
    }

    internal fun search(username: String, user: Bundle) {
        /*val util = UtilityTask(context, activity, "search_bar", this.user, null, re, null)

        util.execute("search_bar", username)*/

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

            }
        }
    }

    override fun onResume() {
        //re.getAdapter().notifyDataSetChanged();

        super.onResume()
    }

    override fun onPause() {

        super.onPause()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener { marker ->
            marker.title
            false
        }


        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        val zoom = CameraUpdateFactory.zoomTo(15f)


        locationListener = object : LocationListener {

            internal var me: Marker? = null
            override fun onLocationChanged(location: Location) {
                if (me != null)
                    me!!.remove()

                val userLocation = LatLng(location.latitude, location.longitude)

                me = mMap.addMarker(MarkerOptions().position(userLocation).title("My location."))
                if(shortestDistance(userLocation)!=null) {
                    var shortest=shortestDistance(userLocation)!!
                    for (a in shortest)
                    mMap.addMarker(MarkerOptions().position(LatLng(a.value.latitude,a.value.longitude)).title(a.key)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.clinic3))
                    // me!!.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.name))


                    mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation))
                    mMap.animateCamera(zoom)

                    val jj = map_taskJ()
                    jj.mMap=mMap
                    for (a in shortest)
                    jj.DownloadTask().execute(jj.getDirectionsUrl(userLocation, LatLng(a.value.latitude,a.value.longitude)))

                }

            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

                Log.d("gps failed", "status changed$s")

            }

            override fun onProviderEnabled(s: String) {

                Log.d("gps failed", "enabled$s")

            }

            override fun onProviderDisabled(s: String) {
                Log.d("gps failed", "disbled$s")

            }


        }


        if (Build.VERSION.SDK_INT < 23) {

            if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        } else if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)

            var lastKnownLocation: Location? = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)


            if (lastKnownLocation == null) {
                Log.d("gps failed", "network on")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

                lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            }
            // Log.d("gps failed","network on"+lastKnownLocation.getLatitude()+"  "+lastKnownLocation.getLongitude());
            /* LatLng userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            LatLng userLocation1 =new LatLng(-1.2145493,36.8869868);
            mMap.clear();
           mMap.addMarker(new MarkerOptions().position(userLocation1).title("Innocent_bar").icon(BitmapDescriptorFactory.fromResource(R.drawable.vir4trans2)));
           mMap.addMarker(new MarkerOptions().flat(true).icon(BitmapDescriptorFactory.fromResource(R.drawable.vir4trans2)).anchor(0.5f, 0.5f)
                            .position(userLocation));
1.1924306/36.9344705
-1.192496/36.9313146
 	-1.192496/36.9313146

            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
            mMap.animateCamera(zoom);*/

            /* LatLng userLocation1 =new LatLng(-1.192496,36.9313146);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation1).title("Innocent_bar"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation1));
            mMap.animateCamera(zoom);*/

        } else {

            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 1)

        }
    }


    fun shortestDistance(point1: LatLng?): HashMap<String,GeoPoint>? {
        if (point1==null)
            return null
        if(item.items.size==0)
            return null
        var point2=item.items.get(0)
        var dinstance=0
        for(a in item.items.get(0))
            dinstance=nonFormatedD(LatLng(a.value.latitude,a.value.longitude),point1)



        for(i in item.items){
            for(a in i) {
                if (nonFormatedD(LatLng(a.value.latitude, a.value.longitude), point1) < dinstance) {
                    point2 = i
                    dinstance = nonFormatedD(LatLng(a.value.latitude, a.value.longitude), point1)
                }
                else
                    mMap.addMarker(MarkerOptions().position(LatLng(a.value.latitude,a.value.longitude)).title(a.key)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.clinic4))

            }


        }
        return point2
    }

    fun nonFormatedD(point1: LatLng?, point2: LatLng?): Int {

        val distanceint: Int
        val numberFormat = NumberFormat.getNumberInstance()
        val distance = Math.round(SphericalUtil.computeDistanceBetween(point1, point2)).toDouble()

        return distance.toInt()
    }

    fun formatDistanceBetween(point1: LatLng?, point2: LatLng?): String? {
        if (point1 == null || point2 == null) {
            return null
        }
        val distanceint: Int
        val numberFormat = NumberFormat.getNumberInstance()
        val distance = Math.round(SphericalUtil.computeDistanceBetween(point1, point2)).toDouble()

        // Adjust to KM if M goes over 1000 (see javadoc of method for note
        // on only supporting metric)
        if (distance >= 1000) {
            numberFormat.setMaximumFractionDigits(1)

            val dis = (distance / 1000).toString()
            if (dis.length >= 6) {
                distanceint = (distance / 1000).toInt()
                return numberFormat.format(distanceint) + "KM"
            } else


                return numberFormat.format(distance / 1000) + "KM"
        }
        return numberFormat.format(distance) + "M"
    }

}

package ke.co.hometoctor.homedoctor.utility


import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore

import ke.co.hometoctor.homedoctor.R
import ke.co.hometoctor.homedoctor.adapters.talk_adapter
import ke.co.hometoctor.homedoctor.adapters.talk_item

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class emergency : Fragment() {

    lateinit var item: talk_item
    public lateinit var  mAuth: FirebaseAuth
    lateinit var mUser: user_info_
    lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_emergency, container, false)

        db= FirebaseFirestore.getInstance()



        db.collection("emergency").document(mAuth.currentUser!!.uid).set(mapOf(Pair("New","0"))).addOnSuccessListener {
            val docRef = db.collection("emergency").document(mAuth.currentUser!!.uid)

            docRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@EventListener
                }

                if (snapshot != null && snapshot.exists()) {
                    var str = ""
                    var str2="0"
                    for (a in snapshot.data!!)
                    {
                        str = a.key + " : " + a.value + "\n"
                        str2=""+a.value
                    }
                    if(!str2.contains("0"))

                        v.findViewById<TextView>(R.id.procedure).text = str2



                } else {
                    Log.d(ContentValues.TAG, "Current data: null")
                }
            })
        }

        return  v
    }


}

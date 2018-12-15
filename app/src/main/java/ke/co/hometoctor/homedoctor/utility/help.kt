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
import com.google.firebase.firestore.FirebaseFirestore

import ke.co.hometoctor.homedoctor.R
import ke.co.hometoctor.homedoctor.adapters.symptom_item

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class help : Fragment() {

    lateinit var item: symptom_item
    public lateinit var  mAuth: FirebaseAuth
    lateinit var mUser: user_info_
    lateinit var db: FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_help, container, false)
        db= FirebaseFirestore.getInstance()

        val docRef = db.collection("help").document("help_item")
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null || document!!.data != null) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: " + document.data)
                        if (document.data == null) {

                        } else {
                            var str = ""
                            for (a in document.data!!)
                                str = a.key + " : " + a.value + "\n"
                            v.findViewById<TextView>(R.id.procedure).text = str

                            Log.d(ContentValues.TAG, "Value is: " + document.data)

                        }

                    } else {
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }


        return v
    }


}

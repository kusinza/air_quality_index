package ke.co.hometoctor.homedoctor.utility


import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import ke.co.hometoctor.homedoctor.R
import ke.co.hometoctor.homedoctor.adapters.symptom_adapter
import ke.co.hometoctor.homedoctor.adapters.symptom_item

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class symptom : Fragment() {

    lateinit var item:symptom_item
    public lateinit var  mAuth: FirebaseAuth
    lateinit var mUser: user_info_
    lateinit var db: FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_symptom, container, false)


        val re=v.findViewById<RecyclerView>(R.id.symptom)
        db= FirebaseFirestore.getInstance()


        re.layoutManager=LinearLayoutManager(context)


        val docRef = db.collection("symptom").document("symptomsd")
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null || document!!.data!=null ) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: " + document.data)
                        if(document.data==null){

                        }
                        else{
                            item=document.toObject(symptom_item::class.java) as symptom_item

                            Log.d(ContentValues.TAG, "Value is: " +document.data)
                            val adapter=symptom_adapter(item)
                            adapter.fragment=this
                            re.adapter=adapter


                            re.adapter!!.notifyDataSetChanged()
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

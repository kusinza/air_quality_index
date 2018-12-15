package ke.co.hometoctor.homedoctor.utility


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore

import ke.co.hometoctor.homedoctor.R
import ke.co.hometoctor.homedoctor.adapters.symptom_adapter
import ke.co.hometoctor.homedoctor.adapters.symptom_item
import ke.co.hometoctor.homedoctor.adapters.talk_adapter
import ke.co.hometoctor.homedoctor.adapters.talk_item
import kotlinx.android.synthetic.main.fragment_talk_to_us.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class talk_to_us : Fragment() {

    lateinit var item: talk_item
    public lateinit var  mAuth: FirebaseAuth
    lateinit var mUser: user_info_
    lateinit var db: FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_talk_to_us, container, false)

        val re=v.findViewById<RecyclerView>(R.id.symptom)
        db= FirebaseFirestore.getInstance()


        re.layoutManager= LinearLayoutManager(context)


        var no_talk=false
        val docRef = db.collection("talk").document(mAuth.currentUser!!.uid)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null || document!!.data!=null ) {
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: " + document.data)
                        if(document.data==null){
                           no_talk=true
                        }
                        else{
                            Log.d(ContentValues.TAG, "Value is: " +document.data)
                        }

                    } else {
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }

        val docRef2 = db.collection("talk").document(mAuth.currentUser!!.uid)
        docRef.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@EventListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: " + snapshot.data)
                item=snapshot.toObject(talk_item::class.java) as talk_item


                val adapter= talk_adapter(item)
                adapter.fragment=this
                re.adapter=adapter


                re.adapter!!.notifyDataSetChanged()

            } else {
                Log.d(TAG, "Current data: null")
            }
        })
        v.findViewById<EditText>(R.id.talk)
        v.findViewById<TextView>(R.id.send).setOnClickListener {
            if(no_talk==true){
                item=talk_item()
                val adapter= talk_adapter(item)
                adapter.fragment=this
                re.adapter=adapter


                db.collection("talk").document(mAuth.currentUser!!.uid).set(item)
                        .addOnSuccessListener { documentReference ->

                            Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: "+ documentReference)

                            item.items.add(mapOf(Pair("1",v.findViewById<EditText>(R.id.talk).text.toString())))
                            db.collection("talk").document(mAuth.currentUser!!.uid).update(mapOf(Pair("items",item.items))).addOnSuccessListener {
                                re.adapter!!.notifyDataSetChanged()
                                v.findViewById<EditText>(R.id.talk).setText("")
                            }

                        }                    .addOnFailureListener { e->
                            Log.w(ContentValues.TAG, "Error adding document", e)

                        }
            }
            else if(v.findViewById<EditText>(R.id.talk).text.toString().length>0){
                val map:Map<String,String>

                item.items.add(mapOf(Pair("1",v.findViewById<EditText>(R.id.talk).text.toString())))
                db.collection("talk").document(mAuth.currentUser!!.uid).update(mapOf(Pair("items",item.items))).addOnSuccessListener {
                    re.adapter!!.notifyDataSetChanged()
                    v.findViewById<EditText>(R.id.talk).setText("")
                }



            }
        }

        return v
    }


}

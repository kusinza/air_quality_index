package ke.co.hometoctor.homedoctor.utility


import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import ke.co.hometoctor.homedoctor.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import ke.co.hometoctor.homedoctor.ambulance
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */

class MainPage : Fragment() {

    lateinit var date_of_birth: TextView
    public lateinit var  mAuth: FirebaseAuth
    lateinit var mUser: user_info_
    lateinit var v:View
    lateinit var myRef:DatabaseReference
    lateinit var db:FirebaseFirestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_main_page, container, false)

        this.v=v
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

       // val database = FirebaseDatabase.getInstance()


         db= FirebaseFirestore.getInstance()
   //     myRef = database.getReference("users")

        //myRef.setValue(mAuth.currentUser!!.uid)



        activity!!.findViewById<TextView>(R.id.log_out).visibility=View.VISIBLE
        activity!!.findViewById<TextView>(R.id.log_out).setOnClickListener {
            mAuth.signOut()
            val transaction=fragmentManager!!.beginTransaction()
            val st=login()
            st.mAuth=mAuth
            transaction.replace(R.id.main_layout, st,"login")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            //transaction.addToBackStack(null)
            transaction.commit()
        }
        val docRef = db.collection("users").document(mAuth.currentUser!!.uid)
        docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null || document!!.data!=null ) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.data)
                        if(document.data==null){
                            updateUI(mAuth.currentUser)
                        }
                        else{
                            mUser=document.toObject(user_info_::class.java) as user_info_

                            Log.d(TAG, "Value is: " )
                            init_user()
                        }

                    } else {
                        updateUI(mAuth.currentUser)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }


/*
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(mAuth.currentUser!!.uid)) {
                   updateUI(mAuth.currentUser)
                }
                 else{
                    myRef.child(mAuth.currentUser!!.uid)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    // This method is called once with the initial value and again
                                    // whenever data at this location is updated.
                                    if(dataSnapshot==null){
                                        updateUI(mAuth.currentUser)
                                    }
                                    else{
                                        val hashMap=HashMap<String,user_info_>()
                                        val value = dataSnapshot.getValue(user_info_::class.java)
                                        if(value==null){

                                        }
                                        else{
                                            mUser=value as user_info_

                                            Log.d(TAG, "Value is: " )
                                            init_user()
                                        }

                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Failed to read value
                                    Log.w(TAG, "Failed to read value.", error.toException())
                                }
                            })

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
*/









        val button=v.findViewById<Button>(R.id.button_don)


        button.setOnClickListener {
            val transaction=fragmentManager!!.beginTransaction()
            val st=ambulance()
            transaction.replace(R.id.main_layout, st,"ambulance")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return v
    }
    fun init_user(){
        v.findViewById<TextView>(R.id.talk_to_us).setOnClickListener {
            updateUITalk()

        }
        date_of_birth=v.findViewById<TextView>(R.id.date_of_birth)
        val user_name=v.findViewById<TextView>(R.id.name_user)
        val health=v.findViewById<SeekBar>(R.id.health)
        val state_health=v.findViewById<TextView>(R.id.state_health)
        user_name.setText("Hello ${mUser.name}")
        val date=Calendar.getInstance()
        val c = Calendar.getInstance()
//Set time in milliseconds

        c.timeInMillis = (mUser.date_of_birth)
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)
        val hr = c.get(Calendar.HOUR)
        val min = c.get(Calendar.MINUTE)
        val sec = c.get(Calendar.SECOND)


        Log.d("time_in_mili","date.timeInMillis-c.timeInMillis"+(date.timeInMillis-c.timeInMillis)+"")
        date.timeInMillis=date.timeInMillis-c.timeInMillis
        val mYear1 = date.get(Calendar.YEAR)
        val mMonth1 = date.get(Calendar.MONTH)
        val mDay1 = date.get(Calendar.DAY_OF_MONTH)
        val hr1 = date.get(Calendar.HOUR)
        val min1 = date.get(Calendar.MINUTE)
        val sec1 = date.get(Calendar.SECOND)


        val graph = v.findViewById<GraphView>(R.id.graph)
        var b=0
        val my_array= arrayOfNulls<DataPoint>(mUser.healthState.size)
        for(a in mUser.healthState){
            val c=b.toDouble()
            Log.d("canotcast",""+a.value)
            my_array.set(b,DataPoint(c as Double, a.value.toString().toDouble()))
            b++
        }

        val series = LineGraphSeries(my_array)
        graph.addSeries(series)
        if(mMonth1>1)
            date_of_birth.setText("Pragnancy is : ${mMonth1} months and ${mDay1} days")
        else
            date_of_birth.setText("Pragnancy is : ${mMonth1} month and ${mDay1} days")

        health.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

                val date=Calendar.getInstance()
               Log.d("date++++",date.time.toString()+"_____"+date.timeInMillis)

                val c = Calendar.getInstance()
//Set time in milliseconds
                c.timeInMillis = (date.timeInMillis)
                val mYear = c.get(Calendar.YEAR)
                val mMonth = c.get(Calendar.MONTH)
                val mDay = c.get(Calendar.DAY_OF_MONTH)
                val hr = c.get(Calendar.HOUR)
                val min = c.get(Calendar.MINUTE)
                val sec = c.get(Calendar.SECOND)



                val hashMap=HashMap<String,Any>()


                val hashMap1=HashMap<String,Any>()
                hashMap1.put(p0!!.progress.toString(),FieldValue.serverTimestamp())

                mUser.healthState.put("_${mMonth}${mDay}${hr}${min}_",p0!!.progress)
                hashMap.put("healthState",mUser.healthState)
                db.collection("users").document(mAuth.currentUser!!.uid).update(hashMap as Map<String, Any>)
                when {
                    (p0.progress>80)->{
                        state_health.setText("Excelent")

                    }
                    (p0.progress>60 && p0.progress<=80)->{

                        state_health.setText("Good")
                    }
                    (p0.progress>40 && p0.progress<=60)->{

                        updateUISymtom()
                        state_health.setText("Bad")
                    }
                    else->{
                        updateUISymtom()
                        state_health.setText("Very bad")
                    }
                }
            }

        })

    }

    fun updateUISymtom(){
        val transaction=fragmentManager!!.beginTransaction()
        val st=symptom()
        st.mAuth=mAuth
        st.mUser=mUser
        transaction.replace(R.id.symptom_view, st,"symptom")
        transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
        transaction.addToBackStack(null)
        transaction.commit()
    }
    fun updateUITalk(){
        val transaction=fragmentManager!!.beginTransaction()
        val st=talk_to_us()
        st.mAuth=mAuth
        st.mUser=mUser
        transaction.replace(R.id.symptom_view, st,"talk_to_us")
        transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun updateUI(user: FirebaseUser?){
        if(user==null){

        }
        else{
            val transaction=fragmentManager!!.beginTransaction()
            val st=user_info()
            st.mAuth=mAuth

            transaction.replace(R.id.main_layout, st,"main_page")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

}

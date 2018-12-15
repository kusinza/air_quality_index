package ke.co.hometoctor.homedoctor.utility


import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.app.AlertDialog.THEME_TRADITIONAL
import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.app.AlertDialog.THEME_HOLO_DARK
import android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
import android.app.AlertDialog.THEME_DEVICE_DEFAULT_DARK
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.support.v4.app.DialogFragment
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import ke.co.hometoctor.homedoctor.R
import kotlinx.android.synthetic.main.fragment_user_info.*
import java.util.*
import kotlin.collections.HashMap
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class user_info_{
    lateinit var name:String
     var date_of_birth:Long=0
    var health=0.0
    var healthState=HashMap<String,Any>()
    var time_state=ArrayList<Any>()
    init {

    }
}
class time_state{
    var state=0
    lateinit var time:Any
}
class user_info : Fragment() {

    lateinit var v:View
    lateinit var date_of_birth:EditText
    public lateinit var  mAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_user_info, container, false)

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)


        this.v=v

        set_adapters()
        val database = FirebaseDatabase.getInstance()
        val db=FirebaseFirestore.getInstance()


        val myRef = database.getReference("users")

        //myRef.setValue(mAuth.currentUser!!.uid)


        date_of_birth=v.findViewById<EditText>(R.id.date_of_birth)
        val user_name=v.findViewById<EditText>(R.id.name_user)
        val health=v.findViewById<SeekBar>(R.id.health)

        date_of_birth.keyListener=null
        val button=v.findViewById<Button>(R.id.button_done)

        date_of_birth.setOnClickListener {
            val datePickerDialogTheme=DatePickerDialogTheme()
            datePickerDialogTheme.date_of_birth=date_of_birth
            datePickerDialogTheme.show(fragmentManager,"Theme")
            //https://clinick-dd6fe.firebaseio.com
        }
        button.setOnClickListener {

            val hashMap=HashMap<String,user_info_>()
            val user_info__=user_info_()
            user_info__.name=user_name.text.toString()

            val myDate = "2014/10/29 18:10:45"
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val date = sdf.parse(date_of_birth.text.toString()+" 00:00:00")
            val millis = date.getTime()
            user_info__.date_of_birth=millis
            user_info__.health=health.progress.toDouble()
            hashMap.put(mAuth.currentUser!!.uid, user_info__)
      //      myRef.child(mAuth.currentUser!!.uid).setValue(user_info__)

            db.collection("users").document(mAuth.currentUser!!.uid).set(user_info__)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: "+ documentReference)
                    }                    .addOnFailureListener { e->
                        Log.w(TAG, "Error adding document", e)

                    }
            Log.d("user_id",mAuth.currentUser!!.uid)
            updateUI(mAuth.currentUser)

        }

        return v
    }

    class DatePickerDialogTheme : DialogFragment(), DatePickerDialog.OnDateSetListener {


        lateinit var date_of_birth:EditText
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            date_of_birth.setText(day.toString() + ":" + (month + 1) + ":" + year)

            val datepickerdialog = DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK, this, year, month, day)
/*

            //for three
            val datepickerdialog = DatePickerDialog(getActivity(),
                    AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, this, year, month, day)

            // for four

            val datepickerdialog = DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_DARK, this, year, month, day)

            //for five

            val datepickerdialog = DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, year, month, day)

            //for six

            val datepickerdialog = DatePickerDialog(getActivity(),
                    AlertDialog.THEME_TRADITIONAL, this, year, month, day)
*/


            return datepickerdialog
        }



        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {



            date_of_birth.setText(day.toString() + "/" + (month + 1) + "/" + year)

        }
    }
    fun updateUI(user: FirebaseUser?){
        if(user==null){

        }
        else{
            val transaction=fragmentManager!!.beginTransaction()
            val st=menu_home()
            st.mAuth=mAuth
            transaction.replace(R.id.main_layout, st,"menu_home")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }
    fun set_adapters(){

        val days=v.findViewById<Spinner>(R.id.select_days)
        val weeks=v.findViewById<Spinner>(R.id.select_weeks)
        val months=v.findViewById<Spinner>(R.id.select_months)

        val days_st= arrayListOf<String>()
        for (i in 0..29)
        {
            days_st.add(i.toString())
            if(i==9)
                break
        }
        val weeks_st= arrayListOf<String>()
        for (i in 0..4)
        {
            weeks_st.add(i.toString())
            if(i==9)
                break
        }
        val months_st= arrayListOf<String>()
        for (i in 0..9)
        {
            months_st.add(i.toString())
            if(i==9)
                break
        }

        val days_adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, days_st)
        days.adapter=days_adapter
        val weeks_adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, weeks_st)
        days.adapter=weeks_adapter
        val months_adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, months_st)
        days.adapter=months_adapter
    }

}

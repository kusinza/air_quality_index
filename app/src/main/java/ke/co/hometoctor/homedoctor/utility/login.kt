package ke.co.hometoctor.homedoctor.utility


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import ke.co.hometoctor.homedoctor.R
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import kotlinx.android.synthetic.main.fragment_login.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class login : Fragment() {

    public lateinit var  mAuth:FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_login, container, false)

        val sign_in_button=v.findViewById<Button>(R.id.sign_in_button)
        val email=v.findViewById<EditText>(R.id.email)
        val password=v.findViewById<EditText>(R.id.password)
        val sing_up_=v.findViewById<TextView>(R.id.sign_up)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
        FirebaseFirestore.getInstance().firestoreSettings=settings
        sing_up_.setOnClickListener {
            val transaction=fragmentManager!!.beginTransaction()
            val st=ke.co.hometoctor.homedoctor.utility.sign_up()
            st.mAuth=mAuth
            transaction.replace(R.id.main_layout, st,"sing_up")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            transaction.addToBackStack(null)
            transaction.commit()
        }
        val utilityAnimation=UtilityAnimation()
        sign_in_button.setOnClickListener {

            sign_in_button.startAnimation(utilityAnimation.getscaleAnimation(200,false))
            if(email.text.toString().length<4){
                email.error="Check your email"
            }
            else if(password.text.toString().length<4){
                password.error="Check your password"
            }
            else{
                sign_in_func(email.text.toString(),password = password.text.toString())
            }
        }

        return  v
    }
    fun sign_in_func(email:String, password:String){


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            val task=it
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                val user = mAuth.getCurrentUser()
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                updateUI(null)
            }
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

    override fun onResume() {
        super.onResume()
        activity!!.findViewById<TextView>(R.id.log_out).visibility=View.GONE
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
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity!!.findViewById<TextView>(R.id.log_out).visibility=View.GONE
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
    }




}

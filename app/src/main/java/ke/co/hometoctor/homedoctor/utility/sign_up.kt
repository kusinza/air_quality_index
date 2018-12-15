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
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_sign_up.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class sign_up : Fragment() {

    lateinit var mAuth:FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_sign_up, container, false)


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
        val sign_up_button=v.findViewById<Button>(R.id.sign_up_button)
        val email=v.findViewById<EditText>(R.id.email)
        val password=v.findViewById<EditText>(R.id.password)
        val c_password=v.findViewById<EditText>(R.id.c_password)

        val utilityAnimation=UtilityAnimation()
        sign_up_button.setOnClickListener {
            sign_up_button.startAnimation(utilityAnimation.getscaleAnimation(200,false))
            if(email.text.toString().isEmpty()){
                email.error="Input email"
            }
            else if(password.text.toString()!=c_password.text.toString()){
                password.error="the passwords are not the same"
            }
            else if(password.text.toString().length<4){
                password.error="Password must be greater than 4 characters "
            }
            else{
                sign_up_func(email.text.toString(),password = password.text.toString())
            }
        }


        return v
    }


    fun sign_up_func(email:String, password:String){

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

            if(it.isSuccessful){
                Log.d(TAG, "createUserWithEmail:success")
                val user = mAuth.getCurrentUser()
                updateUI(user)
            }
            else{
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", it.exception)
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
            val st=user_info()
            st.mAuth=mAuth
            transaction.replace(R.id.main_layout, st,"main_page")
            transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

}

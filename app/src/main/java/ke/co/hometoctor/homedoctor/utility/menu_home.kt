package ke.co.hometoctor.homedoctor.utility


import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.*
import android.transition.TransitionSet.ORDERING_TOGETHER
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import ke.co.hometoctor.homedoctor.R
import ke.co.hometoctor.homedoctor.ambulance

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class menu_home : Fragment() {

    var lastPressTime=System.currentTimeMillis()
    var press=false
    public lateinit var  mAuth: FirebaseAuth
    lateinit var coordinat:IntArray

    var str=""
    private val mDelayHideTouchListener = View.OnTouchListener { v, motionEvent ->



        coordinat=IntArray(2)
        str="motionEvent.x "+motionEvent.x +"_motionEvent.y "+motionEvent.y
        Log.d("touch_listner","T"+str)
        v.getLocationInWindow(coordinat)

        str="v.x"+coordinat[0]+"v.y"+coordinat[1]+"v.x+v.width"+(coordinat[0]+v.width)+"v.y+v.height"+(coordinat[1]+v.height)
        Log.d("touch_listner2","T"+str)



        when(motionEvent.action) {
            0->{
                v.startAnimation(UtilityAnimation().getscaleAnimation2(1000,true))

            }
            2-> {

                if(!press){
                    lastPressTime=System.currentTimeMillis()
                    press=true
                }
            }



            1->{
                if(press){
                    press=false
                    if((System.currentTimeMillis()-lastPressTime)<250){
                        when(v.id){
                            R.id.home->{

                                val st=MainPage()
                                st.mAuth=mAuth
                                updateUI(st,"home",v)
                            }
                            R.id.clinic->{

                                val st=ambulance()
                                updateUI(st,"clinic",v)
                            }
                            R.id.emergency->{

                            }
                            R.id.help->{

                            }
                        }
                    }

                }
                v.startAnimation(UtilityAnimation().getscaleAnimation2back(2000,true))

            }
        /* else->{
             buydrink.setBackgroundResource(R.color.nothing)
             user.putString("work", "buy")
             home()
         }*/







        }
        return@OnTouchListener true

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_menu_home, container, false)
        val home=v.findViewById<RelativeLayout>(R.id.home)
        val emergency=v.findViewById<RelativeLayout>(R.id.emergency)
        val help=v.findViewById<RelativeLayout>(R.id.help)
        val clinic=v.findViewById<RelativeLayout>(R.id.clinic)

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

        home.setOnTouchListener(mDelayHideTouchListener)
        home.startAnimation(UtilityAnimation().getscaleAnimation2back(2000,true))
        emergency.startAnimation(UtilityAnimation().getscaleAnimation2back(2000,true))
        help.startAnimation(UtilityAnimation().getscaleAnimation2back(2000,true))
        clinic.startAnimation(UtilityAnimation().getscaleAnimation2back(2000,true))

        emergency.setOnTouchListener(mDelayHideTouchListener)
        help.setOnTouchListener(mDelayHideTouchListener)
        clinic.setOnTouchListener(mDelayHideTouchListener)




        return v

    }

    fun updateUI(st:Fragment,title:String,transition:View){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            st.setSharedElementEnterTransition(DetailsTransition());
            st.enterTransition = Fade()
            exitTransition = Fade()
            st.sharedElementReturnTransition = DetailsTransition()
        }
        val transaction=fragmentManager!!.beginTransaction()

            transaction.addSharedElement(transition,title)
            //transaction.addSharedElement(transition, "home")
            transaction.addToBackStack(null)
            transaction.replace(R.id.main_layout, st,"main_page")
            transaction.commit()
         // transaction.setCustomAnimations(R.anim.translateend1,R.anim.translateend1);



    }


}

public class DetailsTransition : TransitionSet() {
    init {
        setOrdering(ORDERING_TOGETHER)
        addTransition(ChangeBounds())
        addTransition(ChangeTransform())
        addTransition(ChangeImageTransform())
    }
}
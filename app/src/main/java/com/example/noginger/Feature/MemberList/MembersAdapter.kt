package com.example.noginger.Feature.MemberList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noginger.R
import com.example.noginger.db.Member.MemberEntity

class MembersAdapter(private val mMembers: List<MemberEntity>): RecyclerView.Adapter<MembersAdapter.ViewHolder>() {

    var members = mMembers
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val nameTextView = itemView.findViewById<TextView>(R.id.member_name)
        val dietTextView = itemView.findViewById<TextView>(R.id.member_diet)
        val cantEatTextView = itemView.findViewById<TextView>(R.id.member_cant_eat)
        val intoleranceTextView = itemView.findViewById<TextView>(R.id.member_intolerances)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val memberView = inflater.inflate(R.layout.member_row, parent, false)
        return ViewHolder(memberView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member: MemberEntity = members[position]
        val memberTextView = holder.nameTextView
        memberTextView.setText(member.name)
        val dietTextView = holder.dietTextView
        dietTextView.setText(member.diet)
        val cantEatTextView = holder.cantEatTextView
        cantEatTextView.setText(member.cantEat)
        val intoleranceTextView = holder.intoleranceTextView
        intoleranceTextView.setText(member.intolerances)
    }

    override fun getItemCount(): Int {
        return members.size
    }
}
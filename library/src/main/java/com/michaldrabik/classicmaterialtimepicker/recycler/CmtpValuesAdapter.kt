package com.michaldrabik.classicmaterialtimepicker.recycler

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

internal class CmtpValuesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val items = mutableListOf<String>()

  fun setItems(items: List<String>) {
    this.items.clear()
    this.items.addAll(items)
    notifyDataSetChanged()
  }

  fun getItems() = items

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    ValueViewHolder(
      CmtpValueView(
        parent.context
      )
    )

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ValueViewHolder).view.bind(items[position])
  }

  override fun getItemCount() = items.size

  internal class ValueViewHolder(val view: CmtpValueView) : RecyclerView.ViewHolder(view)
}


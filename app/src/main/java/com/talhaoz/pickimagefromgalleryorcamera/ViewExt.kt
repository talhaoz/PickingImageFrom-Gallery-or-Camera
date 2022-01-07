package com.talhaoz.pickimagefromgalleryorcamera

import android.view.View

fun View.visible()
{
    this.visibility = View.VISIBLE
}

fun View.invisibile()
{
    this.visibility = View.INVISIBLE
}

fun View.gone()
{
    this.visibility = View.GONE
}
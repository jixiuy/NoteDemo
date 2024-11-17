package com.example.node_swift.utils

import android.content.Context
import android.widget.Toast

fun Context.ToastUtils(content:String){
     Toast.makeText(MyApp.getContext(),content,Toast.LENGTH_SHORT).show()
}
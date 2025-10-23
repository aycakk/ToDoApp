package com.example.todo.data.entity

data class DayItem ( var dayName : String, // "Mon"
                     var dateMillis: Long ,// zaman damgası
                     var isSelected: Boolean = false){

}
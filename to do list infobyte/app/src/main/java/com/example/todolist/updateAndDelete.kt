package com.example.todolist

interface updateAndDelete {
    fun modifyItem(itemUID: String,isDone :Boolean)
    fun onItemDelete(itemUID: String)
}
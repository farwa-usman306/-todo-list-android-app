package com.example.todolist

import android.R.attr.background
import android.R.id.background
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults.colors
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDecoration.Companion.LineThrough
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@Preview(showSystemUi = true)
fun mytask() {
    var isshow by remember { mutableStateOf(false) }
    var scope= rememberCoroutineScope()
    var recentlyDeletedTask by remember { mutableStateOf<Pair<Int, String>?>(null) }
    var recentlyDeletedStatus by remember { mutableStateOf<Boolean?>(null) }
  var visi = remember { mutableStateListOf<Boolean>() }
    var tasklist = remember { mutableStateListOf<String>() }
    var snake=remember { SnackbarHostState() }
    var iscomplete = remember { mutableStateListOf<Boolean>() }
    var t =tasklist.size
    var c =iscomplete.count{it}
    var r =t-c

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My task",
                    modifier = Modifier.fillMaxWidth().
                    wrapContentWidth(Alignment.CenterHorizontally),
                    color =Color.White, fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Serif, fontSize = 30.sp)},
                colors= TopAppBarDefaults.
                topAppBarColors(colorResource(R.color.purple_500))
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {isshow=true}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "task")
            }
        },
        bottomBar={  BottomAppBar(modifier = Modifier,containerColor=colorResource(R.color.purple_500), contentColor = Color.White)
        {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {   Column{
             Text("Total", modifier = Modifier, fontWeight = FontWeight.ExtraBold)
       Text(t.toString(), modifier = Modifier, fontWeight = FontWeight.ExtraBold) }
            Column {
                Text("Completed", modifier = Modifier, fontWeight = FontWeight.ExtraBold)
              Text( c.toString(), modifier = Modifier, fontWeight = FontWeight.ExtraBold ) }
            Column {
                Text("Remaining", modifier = Modifier, fontWeight = FontWeight.ExtraBold)
                Text( r.toString() , modifier = Modifier, fontWeight = FontWeight.ExtraBold) }
        }}
        },
        snackbarHost = { SnackbarHost(snake) },
        content = {paddingValues ->
            LazyColumn(modifier = Modifier.fillMaxWidth().padding(paddingValues).padding(10.dp)) {
                items(tasklist.size){ count->
                    var  work=tasklist[count]
                    var wisi=visi.getOrNull(count)?:true
                    AnimatedVisibility(visible =wisi,
                        exit = fadeOut(animationSpec = tween(durationMillis = 2000)),
                        enter = fadeIn(animationSpec = tween(durationMillis = 3000))
                    ) {
                        Card(modifier = Modifier.fillMaxWidth().padding(10.dp).combinedClickable(onClick = {}, onLongClick = {
                            CoroutineScope(Dispatchers.Main).launch { snake.showSnackbar("Selected") }
                        })
                        ){
                            Row(modifier = Modifier.fillMaxSize().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween) {

                                Text(text = work, modifier = Modifier.padding(10.dp),
                                    style = TextStyle(textDecoration = if (!iscomplete[count] ) TextDecoration.None else TextDecoration.LineThrough,
                                        color = if (!iscomplete[count] ) Color.Black else Color.Blue))
                                IconButton(onClick = {
                                    visi[count]=false
                                    scope.launch { delay(1000)
                                    var deletedtask=tasklist[count]
                                    var status=iscomplete[count]
                                     tasklist.removeAt(count)
                                    iscomplete.removeAt(count)
                                    visi.removeAt(count)
                                    recentlyDeletedTask=count to deletedtask
                                    recentlyDeletedStatus=status
                                    scope.launch {
                                        val result=snake.showSnackbar(
                                            message = "Task deleted",
                                            actionLabel = "Undo",
                                            duration = SnackbarDuration.Short
                                        )
                                        if (result== SnackbarResult.ActionPerformed){
                                            recentlyDeletedTask?.let { (index,task)->tasklist.add(index,task)
                                            iscomplete.add(index,recentlyDeletedStatus?:false)}
                                            recentlyDeletedTask=null
                                            recentlyDeletedStatus=null
                                        }
                                    }}
                                }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Task"
                                    )
                                }
                                IconButton(onClick = {iscomplete[count]=true})
                                {Icon(imageVector = Icons.Default.Done, contentDescription = "complete") }

                        }
                    }
                }}}})

if (isshow){
    dialog(onadd = {task->
        if (tasklist.contains(task)) CoroutineScope(Dispatchers.Main).
        launch { snake.showSnackbar("This task already exists") } else
            tasklist.add(task)
        visi.add(true)
         isshow=false
        iscomplete.add(false)}, oncancel = {isshow=false})}
        }


@Composable
fun dialog(onadd:(String)-> Unit,oncancel:()-> Unit){
var text by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var Visi by remember { mutableStateOf(true) }
    Row {
    AlertDialog(
        onDismissRequest = oncancel,
        dismissButton ={
            Button(onClick = {oncancel()} ) {
                Text("Cancel")
            }},
        confirmButton ={
             Button(onClick ={if (text.isNotBlank())  onadd(text) else
                oncancel()})
                 {
                Text("Add")
            }},
        title = {Text("Enter new task")},
        text = {
               OutlinedTextField(
                   value = text,
                   onValueChange = { text = it },
                   label = { Text("Task") },
                   placeholder = { Text("Enter task here") },
                  maxLines = Int.MAX_VALUE
               )
           })}}




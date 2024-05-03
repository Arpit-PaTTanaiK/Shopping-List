package com.example.wishlistapp.ui.theme

import android.media.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


data class ShoppingList(val Id : Int ,
                        var Name : String,
                        var Quantity : Int,
                        var isEditing : Boolean = false
)
  //this is used as alertdialog is in beta stage
@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingList>()) }//agar error hoga yahan hoga
    var showDialog by remember{ mutableStateOf(false) }
    var itemname by remember{ mutableStateOf("") }
    var itemquantity by remember{ mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),//THIS WILL FILL THE WHOLE SCREEN TO ONE COLUMN I GUESS
        verticalArrangement = Arrangement.Center

    ) {
        Button(
            onClick = {showDialog=true},
            modifier = Modifier.align(Alignment.CenterHorizontally)//ALIGNMENT IS AN INTERFACE
        )

        {
            Text(text = "Add Items")

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItems) {
               item ->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete ={
                      editedName,editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}
                        //finding out which element we clickled or used
                        val editeditem =sItems.find { it.Id==item.Id }
                        editeditem?.let {
                            it.Name=editedName
                            it.Quantity=editedQuantity
                        }
                    } )
                    
                }
                else{
                    ShoppingItem(item = item,
                        onEditClick = {
                        sItems=sItems.map{ it.copy(isEditing = it.Id==item.Id ) }
                    },
                        onDeleteClick = {
                            sItems=sItems-item

                        }
                        )

                    
                }

            }

        }


    }
    if(showDialog){
        AlertDialog(onDismissRequest = { showDialog = false },
            confirmButton = {
                            Row(modifier= Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween){
                                Button(onClick = {
                                    if(itemname.isNotBlank()){
                                        val newItem = ShoppingList(
                                            Id=sItems.size+1,
                                            Name=itemname,
                                            Quantity = itemquantity.toInt()
                                        )
                                        sItems=sItems+newItem
                                        showDialog=false
                                        itemname=""
                                        itemquantity=""
                                    }



                                }) {
                                    Text(text = "Add")
                                }
                                Button(onClick = { showDialog=false}) {
                                    Text(text = "Cancel")
                                    
                                }

                            }
            },
            title = { Text(text = "Add items!!!!!")},
            text = {
                Column {

                    OutlinedTextField(value = itemname,
                        onValueChange ={itemname = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    OutlinedTextField(value = itemquantity,
                        onValueChange ={itemquantity = it},
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        )

            
        }
    }
@Composable
fun ShoppingItemEditor(
    item: ShoppingList,
    onEditComplete: (String,Int)-> Unit
){
    var editedName by remember{ mutableStateOf(item.Name) }
    var editedQunatity by remember{ mutableStateOf(item.Quantity.toString()) }
    var isediting by remember{ mutableStateOf(item.isEditing) }
    Row (modifier= Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement =Arrangement.SpaceEvenly){
        Column{
            BasicTextField(value = editedName ,
                onValueChange =  {editedName=it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(value = editedQunatity ,
                onValueChange =  {editedQunatity=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            Button(onClick = {
                isediting=false
                onEditComplete(editedName,editedQunatity.toIntOrNull() ?:1)
            }) {
                Text(text = "Save")
            }

            }

        }

    }


@Composable
fun ShoppingItem(
    item : ShoppingList,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
)
{
    Row(
        modifier= Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFFF5DEB3)),
                shape = RoundedCornerShape(20)
            ), horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Text(text = item.Name, modifier = Modifier.padding(8.dp))
        Text(text = " Qty: ${item.Quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = {onEditClick()}) {
                Icon(imageVector = Icons.Default.Edit, contentDescription ="" )
                
            }
            IconButton(onClick = {onDeleteClick()}) {
                Icon(imageVector = Icons.Default.Delete, contentDescription ="" )

            }
        }

    }
}



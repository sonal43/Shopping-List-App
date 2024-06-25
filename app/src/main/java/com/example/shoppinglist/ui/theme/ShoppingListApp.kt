package com.example.shoppinglist.ui.theme

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingListItem(var id: Int, var name: String, var qty: Int, var isEdit: Boolean = false)

@Composable
fun ShoppingApp(){
    var sItems by remember { mutableStateOf(listOf<ShoppingListItem>())    }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("")}
    var itemQty by remember { mutableStateOf("1")}
    val context = LocalContext.current

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(20.dp)
            ) {
                Text("Add Item")
        }

        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)){
            items(sItems){
                item ->
                if(item.isEdit){
                    ShoppingItemEdit(item = item, onEditComplete = {
                        editedName, editedQty ->
                        sItems = sItems.map{it.copy(isEdit = false)}
                        val editedItem = sItems.find{it.id==item.id}
                        editedItem?.let{
                            it.name = editedName
                            it.qty = editedQty
                        }
                    } )
                }
                else{
                    ShoppingList(item = item,
                        onEditClick = { sItems = sItems.map{it.copy(isEdit = it.id==item.id)} },
                        onDeleteClick = {
                            sItems = sItems - item
                            Toast.makeText(context, "Item Deleted!", Toast.LENGTH_SHORT).show()
                        })
                }
            }
        }

        if( showDialog == true ) {
            AlertDialog(onDismissRequest = { showDialog = false },
                title = { Text("Add New Item", fontSize = 20.sp) },
                text = {
                    Column {
                        OutlinedTextField(value = itemName, onValueChange = { itemName = it}, singleLine = true)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = itemQty, onValueChange = { itemQty = it }, singleLine = true )
                    }
                },
                confirmButton = {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Button(onClick = {
                            showDialog = false
                            if (itemName.isNotBlank()) {
                                var newItem = ShoppingListItem(
                                    id = sItems.size + 1,
                                    name = itemName,
                                    qty = itemQty.toInt()
                                )
                                sItems = sItems + newItem
                            }
                            itemName = ""
                        }) {
                            Text("Add")
                        }
                        Button(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ShoppingItemEdit(
    item: ShoppingListItem,
    onEditComplete: (String, Int) -> Unit){
    var editedName by remember({ mutableStateOf(item.name) })
    var editedQty by remember({ mutableStateOf(item.qty.toString()) })
    var isEditing by remember({ mutableStateOf(item.isEdit) })

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding((6.dp))
            .background(Color.LightGray, shape = RoundedCornerShape(15))
            .border(
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(15)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Column() {
            BasicTextField(value = editedName,
                onValueChange = {editedName=it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))

            BasicTextField(value = editedQty,
                onValueChange = {editedQty = it},
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp))
        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQty.toIntOrNull() ?: 1)
        }) {
            Text(text = "Save")
        }
    }

}

@Composable
fun ShoppingList(
    item: ShoppingListItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .border(
                border = BorderStroke(2.dp, Color(0xFF00668A)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ){
        Text("${item.name}", modifier = Modifier.padding(20.dp))
        Text("Qty: ${item.qty}", modifier = Modifier.padding(2.dp))
        Row(

        ){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color(0xFF225A96)
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color(0xFF963422))
            }
        }
    }
}
package com.example.myshoppinglist

import android.app.AlertDialog
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
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

data class ShoppingItem(val id:Int,
                        var Name:String ,
                        var quantity:Int,
                        var isEdithing: Boolean = false
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var sItems by remember {mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }


    Column( modifier = Modifier.fillMaxSize(),

        verticalArrangement = Arrangement.Center
    ){
        Button(onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 29.dp)
        ) {

            Text("Add Items")
        }
        LazyColumn ( modifier  = Modifier
            .fillMaxSize()
            .padding(18.dp)
        ){
            items(sItems){
               item ->
                if( item.isEdithing){
                    ShoppingItemEditor(item =item, onEditComplete = {
                        editedName, editedQuantity ->
                        sItems = sItems.map { it.copy(  isEdithing = false   ) }
                        val editedItem = sItems.find { it.id == item.id }
                        editedItem?.let{
                            it.Name = editedName
                            it.quantity = editedQuantity
                        }
                    } ) }

                else{
                    ShoppingListItem(item =item , onEditclick = {
                        sItems = sItems.map{it.copy(isEdithing = it.id == item.id)}
                    }, onDeleteclick =  {
                        sItems = sItems- item
                    })

                    }


            }

        }
    }
    if(showDialog){
      AlertDialog(onDismissRequest = {showDialog = false},
          confirmButton = {
              Row(
                  modifier = Modifier
                      .fillMaxWidth()
                      .padding(8.dp),
                  horizontalArrangement = Arrangement
                      .SpaceBetween){ 
                  Button(onClick = {
                      if(itemName.isNotBlank()){
                          val newitem = ShoppingItem(
                              id=sItems.size + 1,
                             Name =  itemName,
                             quantity =  itemQuantity.toInt()

                          )
                          sItems = sItems + newitem
                          showDialog  = false
                          itemName = ""

                      }
                  }) {
                      Text(text = "Add")
                  }
                  Button(onClick = { showDialog =false }) {
                      Text(text = "Cancel")
                      
                  }

              }
              
          },
          title = { Text(text = "Add Shopping Items")},
          text = {
              Column {
                  OutlinedTextField(value = itemName,
                      onValueChange ={ itemName = it},
                      singleLine = true,
                      modifier = Modifier
                          .fillMaxWidth()
                          .padding(8.dp)
                  )
                  OutlinedTextField(value = itemQuantity,
                      onValueChange ={ itemQuantity = it},
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
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String , Int) -> Unit){
    var editedname by remember { mutableStateOf(item.Name)}
    var editedQuantity by remember { mutableStateOf(item.quantity.toString())}
    var isEditing by remember { mutableStateOf(item.isEdithing) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column{
            BasicTextField(value = editedname, onValueChange ={editedname = it} ,
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                )
            BasicTextField(value = editedQuantity,
                onValueChange ={editedQuantity = it} ,
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }
        Button(onClick = {

            isEditing = false
            onEditComplete(editedname, (editedQuantity.toIntOrNull() ?: 1))
        }
        ) {
            Text(text = "Save")
        }
    }

}



@Composable
fun  ShoppingListItem(
    item: ShoppingItem,
    onEditclick: () -> Unit,
    onDeleteclick:() -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFF11D2D2)),
                shape = RoundedCornerShape(20)
            ),

    ) {
       Text(text = item.Name, modifier = Modifier.padding(8.dp) )
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(10.dp   ) )

        Row ( modifier =Modifier.padding(8.dp)){
            IconButton(onClick = onEditclick ) {
                Icon(imageVector =   Icons.Default.Edit, contentDescription = null)
                
            }
            IconButton(onClick = onDeleteclick ) {
                Icon(imageVector =   Icons.Default.Delete, contentDescription = null)

            }

        }
    }


}
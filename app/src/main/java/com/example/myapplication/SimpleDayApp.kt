package com.example.myapplication

import android.app.Dialog
import android.app.RemoteInput
import android.icu.util.LocaleData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.PrimitiveIterator

data class Day(
    val title: String,
    val date: LocalDate
)
private val dayFormatter = DateTimeFormatter.ofPattern("yyyy. MM. dd")
private fun formatDate(date: LocalDate): Pair<String, String> {
    val today = LocalDate.now()
    val diff = ChronoUnit.DAYS.between(today, date)

    val label = when {
        diff > 0 -> "D-$diff"
        diff == 0L -> "D-Day"
        else -> "D+${-diff}"
    }
    val day = date.format(dayFormatter)
    return day to label
}
private fun parseDate(input: String): LocalDate? = runCatching {
    LocalDate.parse(input, dayFormatter)
}.getOrNull()

private fun isValidDate(input: String): Boolean {
    return input.length == 12 && parseDate(input) != null
}

@Composable
fun SimpleDayApp() {
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false)}
    val items = remember { mutableStateListOf<Day>() }

    if (showDialog) {
        DayDialog(onDismiss = { showDialog = false },
            onSubmit = { title, dateStr ->
                val date = parseDate(dateStr)
                if (date != null) {
                    items += Day(title, date)
                    showDialog = false
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .height(58.dp)
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.Black)
                    .clickable { showDialog = true },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "새로운 디데이 만들기",
                    color = Color.White,
                    style = GStyles.Medium,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    )
    {
        innerPadding ->
        Column(
        modifier = Modifier
            .fillMaxSize()
            .paint(painterResource(id = R.drawable.bg_paper), contentScale = ContentScale.Crop)
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)

        ) {
            Spacer(modifier = Modifier.height(266.dp))
            Text(
                modifier = Modifier,

                text = "디데이 목록",
                color = Color.Black,
                fontSize = 24.sp,
                style = GStyles.Medium
                )
            Spacer(modifier = Modifier.height(12.dp))

            for (item in items){
                DayItem(item)
            }
        }
    }
}

@Composable
private fun DayItem(item: Day) {
    val (date, label) = formatDate(item.date)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(83.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                BorderStroke(1.dp, Color(0xFFD8D8D8)),
                RoundedCornerShape(12.dp)
            )
            .background(Color.White)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = item.title,
                color = Color.Black ,
                style = GStyles.Medium,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(9.dp))
            Text(
                text = date,
                color = Color(0xFF7A7A7A),
                style = GStyles.Medium,
                fontSize = 14.sp
            )
        }

        Text(
            text = label,
            color =Color.Black,
            style = GStyles.Bold,
            fontSize = 24.sp
        )
    }
}
@Composable
private fun DayDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String) -> Unit

) {
    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
          Column {
              Text(
                   modifier = Modifier.padding(vertical = 10.dp),
                  text = " 새로운 디데이",
                  style = GStyles.Medium,
                  fontSize = 24.sp,
                  color = Color.Black
              )
              Spacer(modifier = Modifier.height(25.dp))
              DayInput(title = "이름",
                  placeholder = "이름을 입력해주세요.",
                  value = title,
                  onValueChange = {title = it})

              Spacer(modifier = Modifier.height(24.dp))

              DayInput(title = "닐짜",
                  placeholder = "날짜를 입력해주세요.",
                  value = date,
                  onValueChange = {date = it }
              )
              Spacer(modifier = Modifier.height(25.dp))


              Row( modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.End
              ) {
                  Box(
                      modifier = Modifier
                          .size(69.dp, 47.dp)
                          .clip(RoundedCornerShape(14.dp))
                          .background(Color.White)
                          .border(1.dp, Color.Black, RoundedCornerShape(14.dp))
                          .clickable
                          {
                              if (isValidDate(date) && title.isNotEmpty()) {
                                  onSubmit(title, date)
                              }
                          },

                      contentAlignment = Alignment.Center
                  ){
                      Text(text = "취소",
                          color = Color.Black,
                          style = GStyles.Medium,
                          fontSize = 16.sp
                      )
                  }


                  Spacer(Modifier.width(8.dp))

                  Box(
                      modifier = Modifier
                          .size(118.dp, 47.dp)
                          .clip(RoundedCornerShape(14.dp))
                          .background(Color.Black)
                          .clickable {
                              if (isValidDate(date) && title.isNotEmpty()) {
                                  onSubmit(title, date)
                              }
                          },

                      contentAlignment = Alignment.Center
                  ){
                      Text(text = "디데이 생성",
                          color = Color.White,
                          style = GStyles.Medium,
                          fontSize = 16.sp
                      )
                  }
              }
          }
        }
    }
}

@Composable
private fun DayInput(
    title: String,
    placeholder: String,

    value: String,
    onValueChange: (String) -> Unit
) {
    Text(
        modifier = Modifier.padding(horizontal = 4.dp),
        text = title,
        style = GStyles.Medium,
        fontSize = 16.sp,
        color = Color.Black
    )

    Spacer(modifier = Modifier.height(12.dp))

    TextField(value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = GStyles.Medium,
                fontSize = 16.sp,
                color = Color(0x42000000)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedContainerColor =  Color(0xFFF0F0F0),
            unfocusedContainerColor = Color(0xFFF0F0F0),

            errorContainerColor = Color(0xFFF0F0F0),
            disabledContainerColor = Color(0xFFF0F0F0),


            )
    )}
@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        SimpleDayApp()
    }
}
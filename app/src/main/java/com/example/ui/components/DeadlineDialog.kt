package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeadlineDialog(
    onDismiss: () -> Unit,
    onSave: (
        caseNumber: String,
        title: String,
        type: String,
        servedDate: String,
        days: Int,
        legalBasis: String,
        notes: String
    ) -> Unit
) {
    val deadlineTypes = listOf(
        "تجدیدنظرخواهی (۲۰ روز)",
        "واخواهی (۲۰ روز)",
        "فرجام‌خواهی (۲۰ روز)",
        "اعتراض به نظریه کارشناسی (۷ روز)",
        "تبادل لوایح (۱۰ روز)",
        "اعتراض به دستور موقت (۱۰ روز)",
        "اعاده دادرسی (۲۰ روز)",
        "پرداخت هزینه کارشناسی (۷ روز)"
    )

    var selectedType by remember { mutableStateOf(deadlineTypes[0]) }
    var typeExpanded by remember { mutableStateOf(false) }

    var caseNumber by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("مهلت تجدیدنظرخواهی از دادنامه") }
    var servedDate by remember { mutableStateOf("۱۴۰۳/۰۶/۱۵") }
    var daysText by remember { mutableStateOf("20") }
    var legalBasis by remember { mutableStateOf("ماده ۳۳۶ قانون آیین دادرسی مدنی") }
    var notes by remember { mutableStateOf("") }

    fun updateDefaultsForType(type: String) {
        selectedType = type
        when {
            type.startsWith("تجدیدنظر") -> {
                title = "مهلت ۲۰ روزه تجدیدنظرخواهی از دادنامه"
                daysText = "20"
                legalBasis = "ماده ۳۳۶ قانون آیین دادرسی مدنی (۲۰ روز از تاریخ ابلاغ)"
            }
            type.startsWith("واخواهی") -> {
                title = "مهلت ۲۰ روزه واخواهی از رای غیابی"
                daysText = "20"
                legalBasis = "ماده ۳۰۵ قانون آیین دادرسی مدنی (۲۰ روز از تاریخ ابلاغ واقعی)"
            }
            type.startsWith("فرجام") -> {
                title = "مهلت ۲۰ روزه فرجام‌خواهی در دیوان عالی کشور"
                daysText = "20"
                legalBasis = "ماده ۳۹۷ قانون آیین دادرسی مدنی"
            }
            type.startsWith("اعتراض به نظریه کارشناسی") -> {
                title = "مهلت ۷ روزه اعتراض به نظریه کارشناس رسمی"
                daysText = "7"
                legalBasis = "ماده ۲۶۰ قانون آیین دادرسی مدنی (یک هفته از تاریخ ابلاغ)"
            }
            type.startsWith("تبادل لوایح") -> {
                title = "مهلت ۱۰ روزه پاسخ به لایحه تجدیدنظرخوانده"
                daysText = "10"
                legalBasis = "ماده ۳۴۶ قانون آیین دادرسی مدنی"
            }
            type.startsWith("اعتراض به دستور موقت") -> {
                title = "مهلت ۱۰ روزه اعتراض به قرار دستور موقت"
                daysText = "10"
                legalBasis = "ماده ۳۲۵ قانون آیین دادرسی مدنی"
            }
            type.startsWith("پرداخت هزینه کارشناسی") -> {
                title = "مهلت پرداخت دستمزد کارشناس رسمی دادگستری"
                daysText = "7"
                legalBasis = "ماده ۲۵۹ قانون آیین دادرسی مدنی"
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Alarm, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("محاسبه و افزودن موعد قضایی", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("نوع موعد و مهلت قانونی") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        deadlineTypes.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t) },
                                onClick = {
                                    updateDefaultsForType(t)
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("عنوان موعد قضایی*") },
                    modifier = Modifier.fillMaxWidth().testTag("input_deadline_title")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = caseNumber,
                        onValueChange = { caseNumber = it },
                        label = { Text("شماره پرونده مربوطه") },
                        modifier = Modifier.weight(1f).testTag("input_deadline_case_num"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = servedDate,
                        onValueChange = { servedDate = it },
                        label = { Text("تاریخ ابلاغ") },
                        modifier = Modifier.weight(1f).testTag("input_deadline_date"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = daysText,
                    onValueChange = { daysText = it },
                    label = { Text("تعداد روزهای مهلت قانونی") },
                    modifier = Modifier.fillMaxWidth().testTag("input_deadline_days"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = legalBasis,
                    onValueChange = { legalBasis = it },
                    label = { Text("مستند قانونی") },
                    modifier = Modifier.fillMaxWidth().testTag("input_deadline_legal_basis")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("توضیحات و یادداشت تکمیلی") },
                    modifier = Modifier.fillMaxWidth().testTag("input_deadline_notes"),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val daysInt = daysText.toIntOrNull() ?: 20
                    val cleanType = selectedType.substringBefore(" (")
                    onSave(
                        if (caseNumber.isBlank()) "پرونده عمومی" else caseNumber,
                        title,
                        cleanType,
                        servedDate,
                        daysInt,
                        legalBasis,
                        notes
                    )
                },
                modifier = Modifier.testTag("btn_confirm_add_deadline"),
                enabled = title.isNotBlank()
            ) {
                Text("محاسبه و ذخیره")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

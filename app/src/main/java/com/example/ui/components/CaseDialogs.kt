package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.Surface
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
import com.example.data.model.LegalCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCaseDialog(
    onDismiss: () -> Unit,
    onSave: (
        caseNumber: String,
        archiveClass: String,
        courtBranch: String,
        title: String,
        client: String,
        role: String,
        opponent: String,
        status: String,
        priority: String,
        summary: String,
        strategy: String
    ) -> Unit
) {
    var caseNumber by remember { mutableStateOf("") }
    var archiveClass by remember { mutableStateOf("") }
    var courtBranch by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var client by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("خواهان") }
    var opponent by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("در جریان رسیدگی") }
    var priority by remember { mutableStateOf("فوری") }
    var summary by remember { mutableStateOf("") }
    var strategy by remember { mutableStateOf("") }

    val roleOptions = listOf("خواهان", "خوانده", "شاکی", "مشتکی‌عنه / متهم", "تجدیدنظرخواه", "تجدیدنظرخوانده")
    val statusOptions = listOf("در جریان رسیدگی", "در حال تجدیدنظر", "اجرای احکام", "مختومه")
    val priorityOptions = listOf("بحرانی", "فوری", "عادی")

    var roleExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }
    var priorityExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Folder, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ثبت پرونده قضایی جدید", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("موضوع پرونده (مثلاً مطالبه وجه چک)*") },
                    modifier = Modifier.fillMaxWidth().testTag("input_case_title"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = caseNumber,
                        onValueChange = { caseNumber = it },
                        label = { Text("شماره پرونده*") },
                        modifier = Modifier.weight(1f).testTag("input_case_number"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = archiveClass,
                        onValueChange = { archiveClass = it },
                        label = { Text("کلاسه بایگانی*") },
                        modifier = Modifier.weight(1f).testTag("input_case_archive"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = courtBranch,
                    onValueChange = { courtBranch = it },
                    label = { Text("مرجع رسیدگی (شعبه دادگاه / دادسرا)*") },
                    modifier = Modifier.fillMaxWidth().testTag("input_case_court"),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = client,
                        onValueChange = { client = it },
                        label = { Text("نام موکل*") },
                        modifier = Modifier.weight(1f).testTag("input_case_client"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = opponent,
                        onValueChange = { opponent = it },
                        label = { Text("طرف مقابل دعوا*") },
                        modifier = Modifier.weight(1f).testTag("input_case_opponent"),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Role Dropdown
                ExposedDropdownMenuBox(
                    expanded = roleExpanded,
                    onExpandedChange = { roleExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("سمت و نقش موکل") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = roleExpanded,
                        onDismissRequest = { roleExpanded = false }
                    ) {
                        roleOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    role = opt
                                    roleExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Status Dropdown
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("وضعیت پرونده") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt) },
                                onClick = {
                                    status = opt
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = summary,
                    onValueChange = { summary = it },
                    label = { Text("خلاصه ادعا و شرح پرونده") },
                    modifier = Modifier.fillMaxWidth().testTag("input_case_summary"),
                    minLines = 2,
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = strategy,
                    onValueChange = { strategy = it },
                    label = { Text("استراتژی دفاعی و مستندات قانونی") },
                    modifier = Modifier.fillMaxWidth().testTag("input_case_strategy"),
                    minLines = 2,
                    maxLines = 4
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && caseNumber.isNotBlank()) {
                        onSave(
                            caseNumber,
                            if (archiveClass.isBlank()) "۰۳۰۰۱۰۱" else archiveClass,
                            if (courtBranch.isBlank()) "شعبه عمومی حقوقی" else courtBranch,
                            title,
                            if (client.isBlank()) "موکل محترم" else client,
                            role,
                            if (opponent.isBlank()) "خوانده" else opponent,
                            status,
                            priority,
                            summary,
                            strategy
                        )
                    }
                },
                modifier = Modifier.testTag("btn_confirm_add_case"),
                enabled = title.isNotBlank() && caseNumber.isNotBlank()
            ) {
                Text("ثبت پرونده")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

@Composable
fun CaseDetailDialog(
    legalCase: LegalCase,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = legalCase.caseTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "شماره: ${legalCase.caseNumber} | کلاسه: ${legalCase.archiveClassNumber}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "مرجع رسیدگی: ${legalCase.courtBranch}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "موکل: ${legalCase.clientName} (${legalCase.clientRole})",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "طرف دعوا: ${legalCase.oppositeParty}",
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "وضعیت پرونده: ${legalCase.caseStatus} | اولویت: ${legalCase.priority}",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "شرح و خلاصه پرونده:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = legalCase.summary.ifBlank { "شرحی ثبت نشده است." },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "استراتژی دفاعی و استنادات قانونی:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = legalCase.defenseStrategy.ifBlank { "استراتژی مشخصی ثبت نشده است." },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("بستن")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDelete()
                    onDismiss()
                }
            ) {
                Text("حذف پرونده", color = MaterialTheme.colorScheme.error)
            }
        }
    )
}

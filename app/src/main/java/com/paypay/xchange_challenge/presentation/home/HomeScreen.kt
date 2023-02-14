/**
 * Project Name: Currency Xchange
 * Created by: MUTABAZI Charles on 12/02/2023
 */
package com.paypay.xchange_challenge.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.paypay.xchange_challenge.R
import com.paypay.xchange_challenge.presentation.home.components.CurrencyList
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenComposable(
    viewModel: HomeViewModel = getViewModel()
) {
    var amount by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val currencies = viewModel.getCurrentListing.currencies
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Currency Xchange",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Card(modifier = Modifier.padding(bottom = 16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                OutlinedTextField(
                    placeholder = {
                        Text(
                            text = "Enter amount",
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    value = amount,
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                    onValueChange = { amount = it; viewModel.amount = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    leadingIcon = {
                        Box {

                        }
                        TextButton(onClick = { expanded = true }) {
                            Text(
                                text = viewModel.selectedCurrency,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                                contentDescription = "currency symbol",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(MaterialTheme.shapes.small)
                            )
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            currencies.forEach { currency ->
                                DropdownMenuItem(text = { Text(currency.symbol) }, onClick = {
                                    viewModel.selectedCurrency = currency.symbol
                                    expanded = false
                                })
                            }
                        }
                    },
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onDone = { viewModel.convertCurrency() }
                    ),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Column {
                        Text(text = "Base Currency")
                        Text(
                            text = "USD", //hardcoded for now
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { viewModel.convertCurrency() }) {
                        Text("Get Rates")
                    }
                }
            }
        }
        CurrencyList(vm = viewModel)
    }
}
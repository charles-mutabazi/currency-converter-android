package com.paypay.xchange_challenge.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paypay.xchange_challenge.R
import com.paypay.xchange_challenge.presentation.home.HomeViewModel
import com.paypay.xchange_challenge.presentation.theme.CurrencyXchangeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyXchangeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExchangeRateScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeRateScreen(
    viewModel: HomeViewModel = hiltViewModel()
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
                    }
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

@Composable
fun CurrencyList(vm: HomeViewModel) {

    // LazyColumn of currencies
    Column {
        LazyColumn {
            if (vm.convertedAmountList.isEmpty()) {

                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Please enter amount and click Get Rates",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
                return@LazyColumn
            }

            items(items = vm.convertedAmountList) { currency ->
                // Row of currency
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = currency.symbol,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currency.name,
                            style = MaterialTheme.typography.labelLarge,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "%.2f".format(currency.rate), //<-- formatted to 2 decimal places
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (currency != vm.convertedAmountList.last()) {
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyXchangeTheme {
        ExchangeRateScreen()
    }
}